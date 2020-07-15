package com.fengxxc.fastable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * com.fengxxc.fastable.PV2LinkedMap
 */
public class PV2LinkedMap {

    List<PVEntry> pvEntrys;
    private Map<PVEntry, LinkedIdSet> pv2Linkeds;

    public List<PVEntry> getPVEntrys() {
        return pvEntrys;
    }

    public PVEntry getPV(int index) {
        return pvEntrys.get(index);
    }
    
    public int pvSize() {
        return pvEntrys.size();
    }

    public PV2LinkedMap() {
        pvEntrys = new ArrayList<PVEntry>();
        pv2Linkeds = new HashMap<PVEntry, LinkedIdSet>();
    }

    public PV2LinkedMap(int initialCapacity) {
        pvEntrys = new ArrayList<PVEntry>((int) (initialCapacity * 0.75F));
        pv2Linkeds = new HashMap<PVEntry, LinkedIdSet>(initialCapacity + 1);
    }

    public boolean containsPV(PVEntry pv) {
        return pv2Linkeds.containsKey(pv);
    }

    public LinkedIdSet getLinked(PVEntry pv) {
        return pv2Linkeds.get(pv);
    }

    public LinkedIdSet getLinked(String property, Object value) {
        return getLinked(new PVEntry(property, value));
    }

    public LinkedIdSet put(PVEntry pv, int... linkedIds) {
        LinkedIdSet linkedIdSet = new LinkedIdSet(pvSize(), linkedIds);
        pvEntrys.add(pv);
        pv2Linkeds.put(pv, linkedIdSet);
        return linkedIdSet;
    }

    public LinkedIdSet add(PVEntry pv, int... linkedEntryId) {
        return getLinked(pv).addLinkedIds(linkedEntryId);
    }

    public Set<Entry<PVEntry, LinkedIdSet>> entrySet() {
        return pv2Linkeds.entrySet();
    }
}