package main.sortable;

import java.util.Arrays;
import java.util.BitSet;

/**
 * SortableIntegerSet
 */
public class SortableIntegerSet extends BitSet implements ISortableSet<BitSet, Integer> {

    private static final long serialVersionUID = 1L;

    public SortableIntegerSet() {
        super();
    }

    public SortableIntegerSet(int cardinality) {
        super(cardinality);
	}

    @Override
    public void forEach(IteratorFun<BitSet, Integer> iteratorFun) {
        forEach(iteratorFun, -1, -1, true, true);
    }

    @Override
    public void forEach(IteratorFun<BitSet, Integer> iteratorFun, int start, int end, boolean includeStart, boolean includeEnd) {
        if (start > end) return;
        start = start < 0? 0 : start;
        end = end < 0? Integer.MAX_VALUE : end;
        int index = 0;
        int item = this.nextSetBit(start);
        if (item == -1) return;
        if (!(item == start && !includeStart))
            if (!iteratorFun.accept(item, index++, this))
                return;
        for (item = this.nextSetBit(item + 1); item >= 0 && item < end; item = this.nextSetBit(item + 1))
            if (!iteratorFun.accept(item, index++, this))
                return;
        if (item == -1) return;
        if (includeEnd && this.nextSetBit(item) == end)
            iteratorFun.accept(item, index++, this);
        
    }
    
    @Override
    public void add(Integer i) {
        super.set(i);
    }
    
    @Override
    public BitSet toAnd(BitSet other) {
        BitSet r = (BitSet) this.clone();
        r.and(other);
        return r;
    }

    @Override
    public BitSet toOr(BitSet other) {
        BitSet r = (BitSet) this.clone();
        r.or(other);
        return r;
    }

    @Override
    public BitSet toNot(BitSet other) {
        BitSet r = (BitSet) this.clone();
        BitSet t = (BitSet) r.clone();
        t.and(other);
        int i = t.nextSetBit(0);
        if (i == -1)
            return r;
        r.set(i, false);
        for (i = t.nextSetBit(i + 1); i >= 0; i = t.nextSetBit(i + 1)) {
            int endOfRun = t.nextClearBit(i);
            do {
                r.set(i, false);
            } while (++i < endOfRun);
        }
        return r;
    }

    @Override
    public int count() {
        return this.cardinality();
    }

    public static void main(String[] args) {
        ISortableSet<BitSet, Integer> sbs = new SortableIntegerSet();
        sbs.add(3);
        sbs.add(8);
        sbs.add(3);
        sbs.add(123);
        sbs.add(32);
        sbs.add(11);
        sbs.add(7);
        sbs.add(37);

        /* int[] res = new int[sbs.count()];
        sbs.forEach((e, i, c) -> {
            res[i++] = e;
            return true;
        });
        System.out.println(Arrays.toString(res)); */

        int[] res1 = new int[sbs.count()];
        sbs.forEach((e, i, c) -> {
            res1[i++] = e;
            return true;
        }, 37, 37, true, true);
        System.out.println(Arrays.toString(res1));

        // System.out.println("test 'and' 'or' 'not'");
        // BitSet bs = new BitSet();
        // bs.set(7);
        // bs.set(8);
        // bs.set(32);
        // bs.set(1);
        // System.out.println("and: " + sbs.toAnd(bs)); // 7, 8, 32
        // System.out.println("or: " + sbs.toOr(bs)); // 1, 3, 7, 8, 11, 32, 37, 123
        // System.out.println("not: " + sbs.toNot(bs)); // 3, 11, 37, 123
        // System.out.println(sbs); // 3, 7, 8, 11, 32, 37, 123
    }
}