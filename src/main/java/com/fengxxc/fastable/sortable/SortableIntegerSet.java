package com.fengxxc.fastable.sortable;

import java.util.Arrays;
import java.util.BitSet;

/**
 * SortableIntegerSet
 */
public class SortableIntegerSet implements ISortableSet<Integer> {

    private BitSet nIntSet; // negativeIntegerSet
    private BitSet pIntSet; // positiveIntegerSet

    public SortableIntegerSet() {
        pIntSet = new BitSet();
        nIntSet = new BitSet();
    }

    public SortableIntegerSet(BitSet nIntSet, BitSet pIntSet) {
        setNIntSet(nIntSet);
        setPIntSet(pIntSet);
    }

    public BitSet getNIntSet() {
        return nIntSet;
    }

    public void setNIntSet(BitSet nIntSet) {
        this.nIntSet = nIntSet;
    }

    public BitSet getPIntSet() {
        return pIntSet;
    }

    public void setPIntSet(BitSet pIntSet) {
        this.pIntSet = pIntSet;
    }

    @Override
    public void forEach(IteratorFun<Integer> iteratorFun) {
        forEach(iteratorFun, null, null, true, true);
    }

    @Override
    public void forEach(IteratorFun<Integer> iteratorFun, Integer start, Integer end, boolean includeStart, boolean includeEnd) {
        if (start == null) {
            if (nIntSet.cardinality() == 0) {
                if (pIntSet.cardinality() == 0) {
                    return;
                }
                start = 0;
            } else {
                start = 0 - nIntSet.length()-1;
            }
        }
        end = end == null? Integer.MAX_VALUE : end;
        if (start > end) return;

        int index = 0;
        // 迭代负数
        if (nIntSet.cardinality() > 0 && start < 0) {
            int n = nIntSet.previousSetBit(0-start);
            if (n != -1) {
                if (!((0-n) == start && !includeStart))
                    if (!iteratorFun.accept((0-n), index++))
                        return;
                for (; (n = nIntSet.previousSetBit(n - 1)) >= 0 && (0-n) < end;) {
                    // if ((0-n) < end) {
                        if (!iteratorFun.accept((0 - n), index++))
                            return;
                    // }
                }
                if (n != -1) {
                    if (includeEnd && (0-nIntSet.previousSetBit(n)) == end) {
                        iteratorFun.accept((0 - n), index++);
                        return;
                    }
                }
            }
        }
        // 迭代正数
        int pStart = start;
        boolean pIndludeStart = includeStart;
        if (start < 0) {
            pStart = 0;
            pIndludeStart = true;
        }
        int p = pIntSet.nextSetBit(pStart);
        if (p == -1) return;
        if (!(p == pStart && !pIndludeStart))
            if (!iteratorFun.accept(p, index++))
                return;
        for (p = pIntSet.nextSetBit(p + 1); p >= 0 && p < end; p = pIntSet.nextSetBit(p + 1))
            if (!iteratorFun.accept(p, index++))
                return;
        if (p == -1) return;
        if (includeEnd && pIntSet.nextSetBit(p) == end)
            iteratorFun.accept(p, index++);
        
    }
    
    @Override
    public void add(Integer i) {
        if (i < 0) {
            nIntSet.set(0-i);
        } else {
            pIntSet.set(i);
        }
    }

    @Override
    public int count() {
        return nIntSet.cardinality() + pIntSet.cardinality();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("{");
        for (int i = nIntSet.length(); (i = nIntSet.previousSetBit(i-1)) >= 0; ) {
            sb.append((0-i) + ", ");
        }
        String ps = pIntSet.toString();
        if (!"{}".equals(ps)) {
            sb.append(ps.substring(1));
        } else {
            sb.delete(sb.length() - 2, sb.length()).append("}");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        ISortableSet<Integer> sbs = new SortableIntegerSet();
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
        sbs.forEach((e, i) -> {
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