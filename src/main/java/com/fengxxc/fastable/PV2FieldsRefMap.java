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

    private List<KV> pv;
    private Map<KV, FieldsRefSet> pv2FieldsRef;

    public List<KV> getPVs() {
        return pv;
    }

    public KV getPV(int index) {
        return pv.get(index);
    }
    
    public int pvSize() {
        return pv.size();
    }

    public PV2FieldsRefMap() {
        pv = new ArrayList<>();
        pv2FieldsRef = new HashMap<>();
    }

    public PV2FieldsRefMap(int initialCapacity) {
        pv = new ArrayList<>((int) (initialCapacity * 0.75F));
        pv2FieldsRef = new HashMap<>(initialCapacity + 1);
    }

    public boolean containsPV(KV pv) {
        return pv2FieldsRef.containsKey(pv);
    }

    public FieldsRefSet getFieldsRef(KV pv) {
        return pv2FieldsRef.get(pv);
    }

    public FieldsRefSet getFieldsRef(String property, Object value) {
        return getFieldsRef(new KV(property, value));
    }

    public FieldsRefSet put(KV pv, int... fieldRefs) {
        FieldsRefSet fieldsRefSet = new FieldsRefSet(pvSize(), fieldRefs);
        this.pv.add(pv);
        pv2FieldsRef.put(pv, fieldsRefSet);
        return fieldsRefSet;
    }

    public FieldsRefSet add(KV pv, int... fieldRefs) {
        return getFieldsRef(pv).addFieldRefs(fieldRefs);
    }

    public Set<Entry<KV, FieldsRefSet>> entrySet() {
        return pv2FieldsRef.entrySet();
    }
}