package com.fengxxc.fastable;

import com.fengxxc.fastable.sortable.ISortableSet;
import com.fengxxc.fastable.sortable.SortableIntegerSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * com.fengxxc.fastable.Prop2IntValIndexer
 */
public class Prop2IntValIndexer {

    /**
     * 数值型的
     * 属性名: [排好序的数值]
     */
    private Map<String, ISortableSet<Integer>> intM;

    public Prop2IntValIndexer() {
        intM = new HashMap<String, ISortableSet<Integer>>();
    }

    public Prop2IntValIndexer(int initialCapacity) {
        intM = new HashMap<String, ISortableSet<Integer>>(initialCapacity);
    }

    public boolean containsKey(String property) {
        return intM.containsKey(property);
    }

    public ISortableSet<Integer> get(String property) {
        return intM.get(property);
    }

    /*
     * public ISortableSet<Integer> put(String propert return
     * intM.put(property, integerSet); }
     */

    public void add(String property, int integer) {
        if (!this.containsKey(property)) {
            intM.put(property, new SortableIntegerSet());
        }
        intM.get(property).add(integer);
    }

    public ISortableSet<Integer> range(String property, int start, int end, boolean includeStart, boolean includeEnd) {
        ISortableSet<Integer> obs = get(property);
        if (obs == null) {
            return new SortableIntegerSet();
        }
        ISortableSet<Integer> nbs = new SortableIntegerSet();
        obs.forEach((e, i) -> {
            nbs.add(e); return true;
        }, start, end, includeStart, includeEnd);
        return nbs;
    }

    public ISortableSet<Integer> greater(String property, int start, boolean includeStart) {
        ISortableSet<Integer> obs = get(property);
        if (obs == null) {
            return new SortableIntegerSet();
        }
        ISortableSet<Integer> nbs = new SortableIntegerSet();
        obs.forEach((e, i) -> {
            nbs.add(e); return true;
        }, start, obs.count(), includeStart, true);
        return nbs;
    }

    public ISortableSet<Integer> less(String property, int end, boolean includeEnd) {
        ISortableSet<Integer> obs = get(property);
        ISortableSet<Integer> nbs = new SortableIntegerSet();
        obs.forEach((e, i) -> {
            nbs.add(e); return true;
        }, 0, end, true, includeEnd);
        return nbs;
    }

    public Set<Entry<String, ISortableSet<Integer>>> entrySet() {
        return intM.entrySet();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("{\n");
        for (Entry<String, ISortableSet<Integer>> e : this.intM.entrySet()) {
            sb.append("    " + e.getKey() + ": " + e.getValue().toString() + ",\n");
        }
        sb.append("}");
        return sb.toString();
    }
}