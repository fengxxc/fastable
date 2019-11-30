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

import exception.RepeatKeyException;

/**
 * Fastable
 */
public class Fastable<T> {

    private Class<T> classT;
    List<RawDataEntry> dataEntrys;
    private Map<RawDataEntry, LinkedIdSet> graphMap;
    private String uniqueProperty;
    private int tempRowIndex = 0;
    private String rawDataType;

    private final static String DFT_ROWID = ".ROWID";
    public final static String MAP = "MAP";
    public final static String BEAN = "BEAN";

    public Fastable(List<T> data) {
        this(data, null);
    }
    
    @SuppressWarnings("unchecked")
    public Fastable(List<T> data, String uniqueProperty) {
        if (Utils.nullOrZeroSize(data)) return;
        this.classT = (Class<T>) data.get(0).getClass();
        this.uniqueProperty = Utils.nullOrEmptyStr(uniqueProperty)? DFT_ROWID : uniqueProperty;
        if (Map.class.isAssignableFrom(this.classT)) {
            this.rawDataType = MAP;
            initForMap((List<Map<String, Object>>) data);
        } else {
            this.rawDataType = BEAN;
            initForJavaBean(data);
        }
    }

    private void initForMap(List<Map<String, Object>> maps) {
        int capacity = maps.get(0).size() * maps.size();
        this.dataEntrys = new ArrayList<RawDataEntry>((int) (capacity * 0.75F));
        this.graphMap = new HashMap<RawDataEntry, LinkedIdSet>(capacity + 1);
        for (Map<String,Object> m : maps) {
            // 唯一列（索引列）的值
            Object indexVal;
            if (!DFT_ROWID.equals(this.uniqueProperty)) {
                indexVal = m.get(this.uniqueProperty);
                if (indexVal == null)
                    System.err.println("{" + this.uniqueProperty + "}唯一列值出现空值");
            } else {
                indexVal = this.tempRowIndex;
                this.tempRowIndex++;
            }
            RawDataEntry indexEntry = new RawDataEntry(this.uniqueProperty, indexVal, true);
            try {
                putUniqueData(indexEntry);
            } catch (RepeatKeyException e1) {
                e1.printStackTrace();
                return;
            }
            int indexEntryId = getDataEntrySize() - 1;

            // System.out.println(uniqueProperty + " :: " + indexEntry.getVal());
            for (Map.Entry<String,Object> pv : m.entrySet()) {
                String prop = pv.getKey(); // 属性
                if (prop.equals(this.uniqueProperty))
                    continue;
                Object val = pv.getValue();
                RawDataEntry dataEntry = new RawDataEntry(prop, val);
                putRepeatableData(indexEntry, indexEntryId, dataEntry);
                // System.out.println(prop + " :: " + val);
            }
            // System.out.println("----------------------------------");
        }
    }

    private void initForJavaBean(List<T> beans) {
        Map<String, Method> propRMethods; // 属性名: 方法类
        try {
            propRMethods = Utils.getPropReadMethods(Utils.getBeanPropDesc(this.classT));
        } catch (IntrospectionException e1) {
            e1.printStackTrace(); return;
        }
        int capacity = propRMethods.size() * beans.size();
        this.dataEntrys = new ArrayList<RawDataEntry>((int) (capacity * 0.75F));
        this.graphMap = new HashMap<RawDataEntry, LinkedIdSet>(capacity + 1);
        for (Object bean : beans) {
            // 唯一列（索引列）的值
            Object indexVal;
            try {
                if (!DFT_ROWID.equals(this.uniqueProperty)) {
                    indexVal = propRMethods.get(Utils.fristChartoLower(this.uniqueProperty)).invoke(bean);
                    if (indexVal == null) 
                        System.err.println("{" + this.uniqueProperty + "}唯一列值出现空值");
                } else {
                    indexVal = this.tempRowIndex;
                    this.tempRowIndex++;
                }
                
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
                e2.printStackTrace(); return;
            }
            RawDataEntry indexEntry = new RawDataEntry(Utils.fristChartoLower(this.uniqueProperty), indexVal, true);
            try {
                putUniqueData(indexEntry);
            } catch (RepeatKeyException e1) {
                e1.printStackTrace(); return;
            }
            int indexEntryId = getDataEntrySize() - 1;

            // System.out.println(uniqueProperty + " :: " + indexEntry.getVal());
            for (Map.Entry<String, Method> pm : propRMethods.entrySet()) {
                String prop = pm.getKey(); // 属性
                if (prop.equals(Utils.fristChartoLower(this.uniqueProperty)))
                    continue;
                Object val = null; // 属性值
                Method method = pm.getValue();
                try {
                    val = method.invoke(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RawDataEntry dataEntry = new RawDataEntry(prop, val);
                putRepeatableData(indexEntry, indexEntryId, dataEntry);
                // System.out.println(prop + " :: " + val);
            }
            // System.out.println("----------------------------------");
        }
    }

    public String getRawDataType() {
        return this.rawDataType;
    }

    public Map<RawDataEntry, LinkedIdSet> getGraphMap() {
        return this.graphMap;
    }

    public List<RawDataEntry> getDataEntrys() {
        return dataEntrys;
    }

    public String getUniqueProperty() {
        return uniqueProperty;
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

    private void putUniqueData(RawDataEntry dEntry) throws RepeatKeyException {
        if (this.graphMap.containsKey(dEntry)) {
            throw new RepeatKeyException("{" + this.uniqueProperty + ": " + dEntry.getVal().toString() + "} 唯一列值出现重复");
        } else {
            this.dataEntrys.add(dEntry);
            this.graphMap.put(dEntry, new LinkedIdSet(getDataEntrySize() - 1, new HashSet<Integer>()));
        }
    }

    private int getDataEntrySize() {
        return this.dataEntrys.size();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<RawDataEntry, LinkedIdSet> gm : this.graphMap.entrySet()) {
            sb.append(gm.getKey().toString() + ": " + gm.getValue().toString() + "\n");
        }
        return sb.toString();
    }

    public Finder<T> query(String property, Object value) {
        return new Finder<T>(this, property, value);
    }

    @SuppressWarnings("unchecked")
    public T generateObj(Set<Integer> entryIds) throws InstantiationException, IllegalAccessException {
        if (MAP.equals(this.rawDataType)) {
            Map<String, Object> resObj = new HashMap<String, Object>((int) (entryIds.size()/0.75F + 1.0F));
            for (Integer eid : entryIds) {
                RawDataEntry dEntry = this.dataEntrys.get(eid);
                String prop = dEntry.getKey();
                if (DFT_ROWID.equals(this.uniqueProperty) && DFT_ROWID.equals(prop)) {
                    continue;
                }
                Object val = dEntry.getVal();
                resObj.put(prop, val);
            }
            return (T) resObj;
        } else {
            T resObj = this.classT.newInstance();
            for (Integer eid : entryIds) {
                RawDataEntry dEntry = this.dataEntrys.get(eid);
                String prop = dEntry.getKey();
                if (DFT_ROWID.equals(this.uniqueProperty) && DFT_ROWID.equals(prop)) {
                    continue;
                }
                Object val = dEntry.getVal();
                PropertyDescriptor propDesc;
                try {
                    propDesc = new PropertyDescriptor(prop, this.classT);
                    propDesc.getWriteMethod().invoke(resObj, val);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            return resObj;
        }
    }

    public List<T> fetchQuery(String property, Object value) {
        return this.query(property, value).fetch();
    }
}