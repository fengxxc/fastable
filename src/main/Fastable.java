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
import java.util.TreeSet;

import exception.RepeatKeyException;

/**
 * Fastable
 */
public class Fastable<T> {

    private Class<T> classT;
    List<PVEntry> pvEntrys;
    private Map<PVEntry, LinkedIdSet> pv2linkedMap;
    private String uniqueProperty;
    private long tempRowIndex = 0;
    private String rawDataType;

    private final static String DFT_ROWID = ".ROWID";
    public final static String MAP = "MAP";
    public final static String BEAN = "BEAN";

    public Fastable(List<T> data) {
        this(data, null, false);
    }

    public Fastable(List<T> data, String uniqueProperty) {
        this(data, uniqueProperty, false);
    }

    public Fastable(List<T> data, boolean order) {
        this(data, null, order);
    }
    
    @SuppressWarnings("unchecked")
    public Fastable(List<T> data, String uniqueProperty, boolean order) {
        if (Utils.nullOrZeroSize(data)) return;
        this.classT = (Class<T>) data.get(0).getClass();
        this.uniqueProperty = Utils.nullOrEmptyStr(uniqueProperty)? DFT_ROWID : uniqueProperty;
        if (Map.class.isAssignableFrom(this.classT)) {
            this.rawDataType = MAP;
            initForMap((List<Map<String, Object>>) data, order);
        } else {
            this.rawDataType = BEAN;
            initForJavaBean(data, order);
        }
    }

    private void initForMap(List<Map<String, Object>> maps, boolean order) {
        int capacity = maps.get(0).size() * maps.size();
        this.pvEntrys = new ArrayList<PVEntry>((int) (capacity * 0.75F));
        this.pv2linkedMap = new HashMap<PVEntry, LinkedIdSet>(capacity + 1);
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
            PVEntry indexEntry = new PVEntry(this.uniqueProperty, indexVal, true);
            try {
                putUniqueData(indexEntry);
            } catch (RepeatKeyException e1) {
                e1.printStackTrace();
                return;
            }
            int indexEntryId = getPVEntrySize() - 1;

            // System.out.println(uniqueProperty + " :: " + indexEntry.getVal());
            for (Map.Entry<String,Object> pv : m.entrySet()) {
                String prop = pv.getKey(); // 属性
                if (prop.equals(this.uniqueProperty))
                    continue;
                Object val = pv.getValue();
                PVEntry pvEntry = new PVEntry(prop, val);
                putRepeatableData(indexEntry, indexEntryId, pvEntry, order);
                // System.out.println(prop + " :: " + val);
            }
            // System.out.println("----------------------------------");
        }
    }

    private void initForJavaBean(List<T> beans, boolean order) {
        Map<String, Method> propRMethods; // 属性名: 方法类
        try {
            propRMethods = Utils.getPropReadMethods(Utils.getBeanPropDesc(this.classT));
        } catch (IntrospectionException e1) {
            e1.printStackTrace(); return;
        }
        int capacity = propRMethods.size() * beans.size();
        this.pvEntrys = new ArrayList<PVEntry>((int) (capacity * 0.75F));
        this.pv2linkedMap = new HashMap<PVEntry, LinkedIdSet>(capacity + 1);
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
            PVEntry indexEntry = new PVEntry(Utils.fristChartoLower(this.uniqueProperty), indexVal, true);
            try {
                putUniqueData(indexEntry);
            } catch (RepeatKeyException e1) {
                e1.printStackTrace(); return;
            }
            int indexEntryId = getPVEntrySize() - 1;

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
                PVEntry pvEntry = new PVEntry(prop, val);
                putRepeatableData(indexEntry, indexEntryId, pvEntry, order);
                // System.out.println(prop + " :: " + val);
            }
            // System.out.println("----------------------------------");
        }
    }

    public String getRawDataType() {
        return this.rawDataType;
    }

    public Map<PVEntry, LinkedIdSet> getPv2linkedMap() {
        return this.pv2linkedMap;
    }

    public List<PVEntry> getPVEntrys() {
        return pvEntrys;
    }

    public String getUniqueProperty() {
        return uniqueProperty;
    }

    private void putRepeatableData(PVEntry indexEntry, int indexEntryId, PVEntry pvEntry, boolean order) {
        LinkedIdSet dataLinkedIds = null;
        if (this.pv2linkedMap.containsKey(pvEntry)) {
            dataLinkedIds = this.pv2linkedMap.get(pvEntry);
            dataLinkedIds.addLinkedIds(indexEntryId);
        } else {
            this.pvEntrys.add(pvEntry);
            Set<Integer> linkedIds = order? new TreeSet<Integer>() : new HashSet<Integer>();
            linkedIds.add(indexEntryId);
            dataLinkedIds = new LinkedIdSet(getPVEntrySize() - 1, linkedIds);
            this.pv2linkedMap.put(pvEntry, dataLinkedIds);
        }
        this.pv2linkedMap.get(indexEntry).addLinkedIds(dataLinkedIds.getId());
    }

    private void putUniqueData(PVEntry pvEntry) throws RepeatKeyException {
        if (this.pv2linkedMap.containsKey(pvEntry)) {
            throw new RepeatKeyException("{" + this.uniqueProperty + ": " + pvEntry.getVal().toString() + "} 唯一列值出现重复");
        } else {
            this.pvEntrys.add(pvEntry);
            this.pv2linkedMap.put(pvEntry, new LinkedIdSet(getPVEntrySize() - 1, new HashSet<Integer>()));
        }
    }

    private int getPVEntrySize() {
        return this.pvEntrys.size();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<PVEntry, LinkedIdSet> gm : this.pv2linkedMap.entrySet()) {
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
                PVEntry pvEntry = this.pvEntrys.get(eid);
                String prop = pvEntry.getKey();
                if (DFT_ROWID.equals(this.uniqueProperty) && DFT_ROWID.equals(prop)) {
                    continue;
                }
                Object val = pvEntry.getVal();
                resObj.put(prop, val);
            }
            return (T) resObj;
        } else {
            T resObj = this.classT.newInstance();
            for (Integer eid : entryIds) {
                PVEntry pvEntry = this.pvEntrys.get(eid);
                String prop = pvEntry.getKey();
                if (DFT_ROWID.equals(this.uniqueProperty) && DFT_ROWID.equals(prop)) {
                    continue;
                }
                Object val = pvEntry.getVal();
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