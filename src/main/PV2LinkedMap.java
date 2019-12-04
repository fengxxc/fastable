package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * PV2LinkedMap
 */
public class PV2LinkedMap {

    private Map<PVEntry, LinkedIdSet> m;


    public PV2LinkedMap() {
        m = new HashMap<PVEntry, LinkedIdSet>();
    }

    public PV2LinkedMap(int initialCapacity) {
        m = new HashMap<PVEntry, LinkedIdSet>(initialCapacity);
    }

    public boolean containsKey(PVEntry pvEntry) {
        return m.containsKey(pvEntry);
    }

    public LinkedIdSet get(PVEntry pvEntry) {
        return m.get(pvEntry);
    }

    public LinkedIdSet get(String property, Object value) {
        return get(new PVEntry(property, value));
    }

    public LinkedIdSet put(PVEntry pvEntry, LinkedIdSet linkedIdSet) {
        return m.put(pvEntry, linkedIdSet);
    }

    public Set<Entry<PVEntry, LinkedIdSet>> entrySet() {
        return m.entrySet();
    }
}