package com.fengxxc.fastable;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fengxxc.fastable.exception.RepeatKeyException;

/**
 * com.fengxxc.fastable.Fastable
 */
public class Fastable<T> {

    private Class<T> classT;
    
    private PV2LinkedMap pv2linkedMap;
    private Prop2IntValIndexer prop2IntValIndexer;
    private String uniqueProperty;
    private long tempRowIndex = 0;
    private String rawDataType;
    private Map<String, Method> propRMethods; // 属性名: 方法类

    private final static String DFT_ROWID = ".ROWID";
    public final static String MAP = "MAP";
    public final static String BEAN = "BEAN";

    public Fastable(List<T> data) {
        this(data, null);
    }
    
    @SuppressWarnings("unchecked")
    public Fastable(List<T> data, String uniqueProperty) {
        if (Utils.NullOrZeroSize(data)) return;
        this.classT = (Class<T>) data.get(0).getClass();
        this.uniqueProperty = Utils.NullOrEmptyStr(uniqueProperty)? DFT_ROWID : uniqueProperty;
        if (Map.class.isAssignableFrom(this.classT)) {
            // initForMap
            this.rawDataType = MAP;
            List<Map<String, Object>> _data = (List<Map<String, Object>>) data;
            int capacity = _data.get(0).size() * _data.size();
            
            this.pv2linkedMap = new PV2LinkedMap(capacity);
            for (Map<String, Object> m : _data) {
                addData(m);
            }
        } else {
            // initForJavaBean
            this.rawDataType = BEAN;
            List<T> _data = data;
            try {
                propRMethods = Utils.GetPropReadMethods(Utils.GetBeanPropDesc(this.classT));
            } catch (IntrospectionException e) {
                e.printStackTrace();
                return;
            }
            int capacity = propRMethods.size() * _data.size();
            this.pv2linkedMap = new PV2LinkedMap(capacity);
            for (Object bean : _data) {
                addData(bean);
            }
        }
    }

    public void addData(Map<String, Object> m) {
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
            // return;
        }
        int indexEntryId = this.pv2linkedMap.pvSize() - 1;

        // System.out.println(uniqueProperty + " :: " + indexEntry.getVal());
        for (Map.Entry<String, Object> pv : m.entrySet()) {
            String prop = pv.getKey(); // 属性
            if (prop.equals(this.uniqueProperty))
                continue;
            Object val = pv.getValue();
            PVEntry pvEntry = new PVEntry(prop, val);
            putRepeatableData(indexEntry, indexEntryId, pvEntry);
            // System.out.println(prop + " :: " + val);
        }
        // System.out.println("----------------------------------");
    }

    public void addData(Object bean) {
        // 唯一列（索引列）的值
        Object indexVal = null;
        if (!DFT_ROWID.equals(this.uniqueProperty)) {
            try {
                indexVal = propRMethods.get(Utils.FristChartoLower(this.uniqueProperty)).invoke(bean);
                if (indexVal == null) 
                    System.err.println("{" + this.uniqueProperty + "}唯一列值出现空值");
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
                e2.printStackTrace();
                return;
            }
        } else {
            indexVal = this.tempRowIndex;
            this.tempRowIndex++;
        }
            
        PVEntry indexEntry = new PVEntry(Utils.FristChartoLower(this.uniqueProperty), indexVal, true);
        try {
            putUniqueData(indexEntry);
        } catch (RepeatKeyException e1) {
            e1.printStackTrace();
            return;
        }
        int indexEntryId = this.pv2linkedMap.pvSize() - 1;

        // System.out.println(uniqueProperty + " :: " + indexEntry.getVal());
        for (Map.Entry<String, Method> pm : propRMethods.entrySet()) {
            String prop = pm.getKey(); // 属性
            if (prop.equals(Utils.FristChartoLower(this.uniqueProperty)))
                continue;
            Object val = null; // 属性值
            Method method = pm.getValue();
            try {
                val = method.invoke(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
            PVEntry pvEntry = new PVEntry(prop, val);
            putRepeatableData(indexEntry, indexEntryId, pvEntry);
            // System.out.println(prop + " :: " + val);
        }
        // System.out.println("----------------------------------");
    }

    public String getRawDataType() {
        return this.rawDataType;
    }

    public PV2LinkedMap getPv2linkedMap() {
        return this.pv2linkedMap;
    }

    public Prop2IntValIndexer getProp2IntValIndexer() {
        return this.prop2IntValIndexer;
    }

    public String getUniqueProperty() {
        return uniqueProperty;
    }

    private void putRepeatableData(PVEntry indexEntry, int indexEntryId, PVEntry pvEntry) {
        if (this.pv2linkedMap.containsPV(pvEntry)) {
            LinkedIdSet dataLinkedIds = this.pv2linkedMap.add(pvEntry, indexEntryId);
            this.pv2linkedMap.add(indexEntry, dataLinkedIds.getId());
        } else {
            LinkedIdSet dataLinkedIds = this.pv2linkedMap.put(pvEntry, indexEntryId);
            this.pv2linkedMap.add(indexEntry, dataLinkedIds.getId());
            putSortableIndex(pvEntry);
        }
    }

    private void putUniqueData(PVEntry pvEntry) throws RepeatKeyException {
        if (this.pv2linkedMap.containsPV(pvEntry)) {
            throw new RepeatKeyException("{" + this.uniqueProperty + ": " + pvEntry.getVal().toString() + "} 唯一列值出现重复");
        } else {
            this.pv2linkedMap.put(pvEntry);
            putSortableIndex(pvEntry);
        }
    }

    private void putSortableIndex(PVEntry pvEntry) {
        if (this.prop2IntValIndexer == null) {
            this.prop2IntValIndexer = new Prop2IntValIndexer();
        }
        if (pvEntry.getVal() instanceof Integer) {
            this.prop2IntValIndexer.add(pvEntry.getKey(), (int) pvEntry.getVal());
        }
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

    public Finder<T> queryRange(String sortableProperty, int start, int end, boolean includeStart, boolean includeEnd) {
        Finder<T> finder = new Finder<>(this);
        return finder.andRange(sortableProperty, start, end, includeStart, includeEnd);
    }

    @SuppressWarnings("unchecked")
    public T generateObj(int[] entryIds) throws InstantiationException, IllegalAccessException {
        if (MAP.equals(this.rawDataType)) {
            Map<String, Object> resObj = new HashMap<String, Object>((int) (entryIds.length/0.75F + 1.0F));
            for (Integer eid : entryIds) {
                PVEntry pvEntry = this.pv2linkedMap.getPV(eid);
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
                PVEntry pvEntry = this.pv2linkedMap.getPV(eid);
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