package main;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import main.sortable.ISortableSet;
import main.sortable.SortableIntegerSet;

/**
 * Prop2IntValMap
 */
public class Prop2IntValMap {

    /**
     * 数值型的
     * 属性名: [排好序的数值]
     */
    private Map<String, ISortableSet<BitSet, Integer>> intM;

    public Prop2IntValMap() {
        intM = new HashMap<String, ISortableSet<BitSet, Integer>>();
    }

    public Prop2IntValMap(int initialCapacity) {
        intM = new HashMap<String, ISortableSet<BitSet, Integer>>(initialCapacity);
    }

    public boolean containsKey(String property) {
        return intM.containsKey(property);
    }

    public ISortableSet<BitSet, Integer> get(String property) {
        return intM.get(property);
    }

    /*
     * public ISortableSet<BitSet, Integer> put(String propert return
     * intM.put(property, integerSet); }
     */

    public void add(String property, int integer) {
        if (!this.containsKey(property)) {
            intM.put(property, new SortableIntegerSet());
        }
        intM.get(property).add(integer);
    }

    public ISortableSet<BitSet, Integer> range(String property, int start, int end, boolean includeStart, boolean includeEnd) {
        ISortableSet<BitSet, Integer> obs = get(property);
        if (obs == null) {
            return new SortableIntegerSet();
        }
        ISortableSet<BitSet, Integer> nbs = new SortableIntegerSet(obs.count());
        obs.forEach((e, i, c) -> {
            nbs.add(e); return true;
        }, start, end, includeStart, includeEnd);
        return nbs;
    }

    public ISortableSet<BitSet, Integer> greater(String property, int start, boolean includeStart) {
        ISortableSet<BitSet, Integer> obs = get(property);
        if (obs == null) {
            return new SortableIntegerSet();
        }
        ISortableSet<BitSet, Integer> nbs = new SortableIntegerSet(obs.count());
        obs.forEach((e, i, c) -> {
            nbs.add(e); return true;
        }, start, obs.count(), includeStart, true);
        return nbs;
    }

    public ISortableSet<BitSet, Integer> less(String property, int end, boolean includeEnd) {
        ISortableSet<BitSet, Integer> obs = get(property);
        ISortableSet<BitSet, Integer> nbs = new SortableIntegerSet(obs.count());
        obs.forEach((e, i, c) -> {
            nbs.add(e); return true;
        }, 0, end, true, includeEnd);
        return nbs;
    }

    public Set<Entry<String, ISortableSet<BitSet, Integer>>> entrySet() {
        return intM.entrySet();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("{\n");
        for (Entry<String, ISortableSet<BitSet, Integer>> e : this.intM.entrySet()) {
            sb.append("    " + e.getKey() + ": " + e.getValue().toString() + ",\n");
        }
        sb.append("}");
        return sb.toString();
    }
}