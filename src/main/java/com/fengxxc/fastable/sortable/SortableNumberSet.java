package com.fengxxc.fastable.sortable;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * SortableNumberSet
 */
public class SortableNumberSet implements ISortableSet {

    private BitSet nIntSet; // negativeIntegerSet
    private BitSet pIntSet; // positiveIntegerSet
    private Set<Double> dblSet; // doubleSet

    public SortableNumberSet() {
        pIntSet = new BitSet();
        nIntSet = new BitSet();
        dblSet = new TreeSet<Double>();
    }

    public SortableNumberSet(BitSet nIntSet, BitSet pIntSet, TreeSet<Double> dblSet) {
        this.nIntSet = nIntSet;
        this.pIntSet = pIntSet;
        this.dblSet = dblSet;
    }

    @Override
    public void forEach(IteratorFun iteratorFun) {
        forEach(iteratorFun, null, null, true, true);
    }

    @Override
    public void forEach(IteratorFun iteratorFun, Integer start, Integer end, boolean includeStart, boolean includeEnd) {
        if (start == null) {
            if (nIntSet.cardinality() == 0) {
                if (pIntSet.cardinality() == 0) {
                    return;
                }
                start = 0;
            } else {
                start = 0 - nIntSet.length() - 1;
            }
        }
        end = end == null ? Integer.MAX_VALUE : end;
        if (start > end)
            return;

        int index = 0;

        // 浮点数部分准备 start
        Iterator<Double> dblIter = dblSet.iterator();
        double dblVal;
        // 浮点数部分准备 end

        // 迭代负数
        if (start < 0) {
            if (nIntSet.cardinality() > 0) {
                int n = nIntSet.previousSetBit(0 - start);
                // 迭代小于n的负浮点数 start
                /*
                 * while (dblIter.hasNext() && (dblVal = dblIter.next()) < (0 - n)) { if
                 * (!iteratorFun.accept(dblVal, index++)) return; }
                 */
                // 迭代小于n的负浮点数 end
                if (n != -1) {
                    if (!((0 - n) == start && !includeStart)) {
                        if (!iteratorFun.accept((0 - n), index++))
                            return;
                    }
                    while ((n = nIntSet.previousSetBit(n - 1)) >= 0 && (0 - n) < end) {
                        if (!iteratorFun.accept((0 - n), index++))
                            return;
                    }
                    if (n != -1) {
                        if (includeEnd && (0 - nIntSet.previousSetBit(n)) == end) {
                            iteratorFun.accept((0 - n), index++);
                            return;
                        }
                    }
                }
            } else {
                // 负数不包含整数时，迭代所有负浮点数
                /*
                 * while (dblIter.hasNext() && (dblVal = dblIter.next()) < 0) { if
                 * (!iteratorFun.accept(dblVal, index++)) return; }
                 */
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
        if (p == -1)
            return;
        if (!(p == pStart && !pIndludeStart))
            if (!iteratorFun.accept(p, index++))
                return;
        for (p = pIntSet.nextSetBit(p + 1); p >= 0 && p < end; p = pIntSet.nextSetBit(p + 1))
            if (!iteratorFun.accept(p, index++))
                return;
        if (p == -1)
            return;
        if (includeEnd && pIntSet.nextSetBit(p) == end)
            iteratorFun.accept(p, index++);

    }

    private int tempN;
    private int tempP;
    private double tempDb;
    private Number curNum;

    public Number next() {
        Number res = null;
        if (curNum == null) {
            if (nIntSet.cardinality() == 0) {

            } else {
                int minN = nIntSet.previousSetBit(0 - nIntSet.length() - 1);

            }
        }
        return res;
    }

    public void add(Integer val) {
        if (val < 0) {
            nIntSet.set(0 - val);
        } else {
            pIntSet.set(val);
        }
    }

    public void add(Double val) {
        dblSet.add(val);
    }

    @Override
    public void add(Object val) {
        if (val instanceof Integer) {
            add((int) val);
        } else if (val instanceof Float) {
            add((double) val);
        } else if (val instanceof Double) {
            add((double) val);
        } else {
            // throw new Exception();
            System.err.println("参数类型错误：必须是int、float、double");
        }

    }

    @Override
    public int count() {
        return nIntSet.cardinality() + pIntSet.cardinality();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("{");
        for (int i = nIntSet.length(); (i = nIntSet.previousSetBit(i - 1)) >= 0;) {
            sb.append((0 - i) + ", ");
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
        ISortableSet sbs = new SortableNumberSet();
        sbs.add(3);
        sbs.add(8);
        sbs.add(3);
        sbs.add(123);
        sbs.add(32);
        sbs.add(11);
        sbs.add(7);
        sbs.add(37);

        /*
         * int[] res = new int[sbs.count()]; sbs.forEach((e, i, c) -> { res[i++] = e;
         * return true; }); System.out.println(Arrays.toString(res));
         */

        int[] res1 = new int[sbs.count()];
        sbs.forEach((e, i) -> {
            res1[i++] = (int) e;
            return true;
        }, 37, 37, true, true);
        System.out.println(Arrays.toString(res1));

    }

}