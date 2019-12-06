package main;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Prop2IntValMap
 */
public class Prop2IntValMap {

    /**
     * 数值型的
     * 属性名: [排好序的数值]
     */
    private Map<String, BitSet> m;

    public Prop2IntValMap() {
        m = new HashMap<String, BitSet>();
    }

    public Prop2IntValMap(int initialCapacity) {
        m = new HashMap<String, BitSet>(initialCapacity);
    }

    public boolean containsKey(String property) {
        return m.containsKey(property);
    }

    public BitSet get(String property) {
        return m.get(property);
    }

    public BitSet put(String property, BitSet integerSet) {
        return m.put(property, integerSet);
    }

    public void add(String property, int integer) {
        if (!this.containsKey(property)) {
            this.put(property, new BitSet());
        }
        m.get(property).set(integer);
    }

    public BitSet range(String property, int start, int end, boolean includeStart, boolean includeEnd) {
        BitSet obs = get(property);
        if (obs == null) {
            return new BitSet();
        }
        BitSet nbs = Utils.BitSet_range(obs, start, end, includeStart, includeEnd);
        return nbs;
    }

    public BitSet greater(String property, int start, boolean includeStart) {
        BitSet obs = get(property);
        BitSet nbs = Utils.BitSet_range(obs, start, obs.size()-1, includeStart, true);
        return nbs;
    }

    public BitSet less(String property, int end, boolean includeEnd) {
        BitSet obs = get(property);
        BitSet nbs = Utils.BitSet_range(obs, 0, end, true, includeEnd);
        return nbs;
    }

    public Set<Entry<String, BitSet>> entrySet() {
        return m.entrySet();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("{\n");
        for (Entry<String, BitSet> e : this.m.entrySet()) {
            sb.append("    " + e.getKey() + ": " + Arrays.toString(Utils.BitSetToBecimalArray(e.getValue())) + ",\n");
        }
        sb.append("}");
        return sb.toString();
    }
}