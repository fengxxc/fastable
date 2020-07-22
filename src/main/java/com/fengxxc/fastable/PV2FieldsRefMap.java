package com.fengxxc.fastable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * com.fengxxc.fastable.PV2FieldsRefMap
 */
public class PV2FieldsRefMap {

    private List<PVEntry> pvEntrys;
    private Map<PVEntry, FieldsRefSet> pv2FieldsRef;

    public List<PVEntry> getPVEntrys() {
        return pvEntrys;
    }

    public PVEntry getPV(int index) {
        return pvEntrys.get(index);
    }
    
    public int pvSize() {
        return pvEntrys.size();
    }

    public PV2FieldsRefMap() {
        pvEntrys = new ArrayList<PVEntry>();
        pv2FieldsRef = new HashMap<PVEntry, FieldsRefSet>();
    }

    public PV2FieldsRefMap(int initialCapacity) {
        pvEntrys = new ArrayList<PVEntry>((int) (initialCapacity * 0.75F));
        pv2FieldsRef = new HashMap<PVEntry, FieldsRefSet>(initialCapacity + 1);
    }

    public boolean containsPV(PVEntry pv) {
        return pv2FieldsRef.containsKey(pv);
    }

    public FieldsRefSet getFieldsRef(PVEntry pv) {
        return pv2FieldsRef.get(pv);
    }

    public FieldsRefSet getFieldsRef(String property, Object value) {
        return getFieldsRef(new PVEntry(property, value));
    }

    public FieldsRefSet put(PVEntry pv, int... fieldRefs) {
        FieldsRefSet fieldsRefSet = new FieldsRefSet(pvSize(), fieldRefs);
        pvEntrys.add(pv);
        pv2FieldsRef.put(pv, fieldsRefSet);
        return fieldsRefSet;
    }

    public FieldsRefSet add(PVEntry pv, int... fieldRefs) {
        return getFieldsRef(pv).addFieldRefs(fieldRefs);
    }

    public Set<Entry<PVEntry, FieldsRefSet>> entrySet() {
        return pv2FieldsRef.entrySet();
    }
}