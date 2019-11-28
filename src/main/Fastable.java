package main;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exception.NullUniquePropertyValueException;
import exception.RepeatKeyException;

/**
 * Fastable
 */
public class Fastable {

    List<RawDataEntry> dataEntrys;
    private Map<RawDataEntry, LinkedIdSet> graphMap;
    private String uniqueProperty;
    private Set<Integer> tempFindIds;
    private final static String DFT_ROWID = ".ROWID";
    private int tempRowIndex = 0;

    public Fastable(List<?> javabeans, Class<?> clazz) {
        this(javabeans, clazz, null);
    }

    public Fastable(List<?> javabeans, Class<?> clazz, String uniqueProperty) {
        if (javabeans.size() == 0) {
            return;
        }
        if ("".equals(uniqueProperty)) {
            System.err.println("唯一列不可以是空字符");
            return;
        }
        this.uniqueProperty = uniqueProperty == null? DFT_ROWID : Utils.toUpperFristChar(uniqueProperty);
        Map<String, Method> propRMethods; // 属性名: 方法类
        try {
            propRMethods = filterPropReadMethods(Utils.getBeanPropDesc(clazz));
        } catch (IntrospectionException e1) {
            e1.printStackTrace();
            return;
        }
        int capacity = propRMethods.size() * javabeans.size();
        this.dataEntrys = new ArrayList<RawDataEntry>((int) (capacity * 0.75F));
        this.graphMap = new HashMap<RawDataEntry, LinkedIdSet>(capacity + 1);
        for (Object bean : javabeans) {
            // 唯一列（索引列）的值
            Object indexVal;
            try {
                indexVal = getIndexValue(propRMethods, bean);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NullUniquePropertyValueException e2) {
                e2.printStackTrace(); return;
            }
            RawDataEntry indexEntry = new RawDataEntry(this.uniqueProperty, indexVal, true);
            try {
                putUniqueData(indexEntry);
            } catch (RepeatKeyException e1) {
                e1.printStackTrace(); return;
            }
            int indexEntryId = getDataEntrySize() - 1;

            // System.out.println(uniqueProperty + " :: " + indexEntry.getVal());
            for (Map.Entry<String, Method> propMethod : propRMethods.entrySet()) {
                String prop = propMethod.getKey(); // 属性
                if (prop.equals(this.uniqueProperty))
                    continue;
                Object val = null; // 属性值
                Method m = propMethod.getValue();
                try {
                    val = m.invoke(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RawDataEntry dataEntry = new RawDataEntry(prop, val);
                putRepeatableData(indexEntry, indexEntryId, dataEntry);

                System.out.println(prop + " :: " + val);
            }
            // System.out.println("----------------------------------");
        }
    }

    private Object getIndexValue(Map<String, Method> propRMethods, Object bean)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NullUniquePropertyValueException {
        Object indexVal = null;
        if (!DFT_ROWID.equals(this.uniqueProperty)) {
            indexVal = propRMethods.get(this.uniqueProperty).invoke(bean);
        } else {
            indexVal = this.tempRowIndex;
            this.tempRowIndex ++;
        }
        
        if (indexVal == null) {
            throw new NullUniquePropertyValueException("{" + this.uniqueProperty + "}唯一列值出现空值");
        }
        return indexVal;
    }

    private void putRepeatableData(RawDataEntry indexEntry, int indexEntryId, RawDataEntry dataEntry) {
        LinkedIdSet dataLinkedIds = null;
        if (this.graphMap.containsKey(dataEntry)) {
            dataLinkedIds = this.graphMap.get(dataEntry);
            dataLinkedIds.addLinkedIds(indexEntryId);
        } else {
            this.dataEntrys.add(dataEntry);
            Set<Integer> linkedIds = new HashSet<Integer>();
            linkedIds.add(indexEntryId);
            dataLinkedIds = new LinkedIdSet(getDataEntrySize() - 1, linkedIds);
            this.graphMap.put(dataEntry, dataLinkedIds);
        }
        this.graphMap.get(indexEntry).addLinkedIds(dataLinkedIds.getId());
    }

    private void putUniqueData(RawDataEntry rDEntry) throws RepeatKeyException {
        if (this.graphMap.containsKey(rDEntry)) {
            throw new RepeatKeyException("{" + rDEntry.getKey() + ": " + rDEntry.getVal().toString() + "} 唯一列值出现重复");
        } else {
            this.dataEntrys.add(rDEntry);
            this.graphMap.put(rDEntry, new LinkedIdSet(getDataEntrySize() - 1, new HashSet<Integer>()));
        }
    }

    private int getDataEntrySize() {
        return this.dataEntrys.size();
    }

    private Map<String, Method> filterPropReadMethods(PropertyDescriptor[] propDesc) {
        Map<String, Method> prm = new HashMap<String, Method>();
        for (PropertyDescriptor pd : propDesc) {
            Method rm = pd.getReadMethod();
            if (rm.getName().equals("getClass"))
                continue;
            prm.put(rm.getName().substring(3), rm);
        }
        return prm;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<RawDataEntry, LinkedIdSet> gm : this.graphMap.entrySet()) {
            sb.append(gm.getKey().toString() + ": " + gm.getValue().toString() + "\n");
        }
        return sb.toString();
    }

    public Fastable query(String property, Object value) {
        property = Utils.toUpperFristChar(property);
        RawDataEntry qEntry = new RawDataEntry(property, value);
        Set<Integer> findIds = new HashSet<Integer>();
        if (this.uniqueProperty.equals(property)) {
            // 根据唯一列的查找
            findIds.add(this.graphMap.get(qEntry).getId());
        } else {
            // 根据非唯一列的查找
            findIds = this.graphMap.get(qEntry).getLinkedIds();
        }
        if (this.tempFindIds != null) 
            this.tempFindIds.retainAll(findIds);
        else 
            this.tempFindIds = findIds;
        return this;
    }

    public List<? extends Object> fetch(Class<?> clazz) {
        List<Object> res = new ArrayList<>();
        for (Integer indexId : this.tempFindIds) {
            RawDataEntry iEntry = this.dataEntrys.get(indexId);
            Set<Integer> dEntryIds = this.graphMap.get(iEntry).getAllIds();
            Object resObj = null;
            try {
                resObj = generateObj(clazz, dEntryIds);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                // return null;
            }
            res.add(resObj);
        }
        this.tempFindIds = null;
        return res;
    }

    public List<? extends Object> fetchQuery(Class<?> clazz, String property, Object value) {
        return this.query(property, value).fetch(clazz);
    }

    private Object generateObj(Class<?> clazz, Set<Integer> entryIds) throws InstantiationException, IllegalAccessException {
        Object resObj;
        resObj = clazz.newInstance();
        for (Integer eid : entryIds) {
            RawDataEntry dEntry = this.dataEntrys.get(eid);
            String prop = dEntry.getKey();
            if (DFT_ROWID.equals(this.uniqueProperty) && DFT_ROWID.equals(prop)) {
                continue;
            }
            Object val = dEntry.getVal();
            PropertyDescriptor propDesc;
            try {
                propDesc = new PropertyDescriptor(prop, clazz);
                propDesc.getWriteMethod().invoke(resObj, val);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resObj;
    }
}