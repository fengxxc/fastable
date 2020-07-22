package com.fengxxc.fastable;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BitSetTest {
    @Test
    void traversalTest() {
        BitSet bs1 = new BitSet();
        bs1.set(2);
        bs1.set(5);
        bs1.set(7);

        List<Integer> list = new ArrayList<Integer>();
        int i = bs1.nextSetBit(0);
        if (i != -1) {
            list.add((Integer) i);
            for (i = bs1.nextSetBit(i + 1); i >= 0; i = bs1.nextSetBit(i + 1)) {
                int endOfRun = bs1.nextClearBit(i);
                do {
                    list.add(i);
                } while (++i < endOfRun);
            }
        }
        System.out.println("bs1: " + list.toString());
        assertArrayEquals(new Integer[]{2, 5, 7}, list.toArray(new Integer[list.size()]));
    }

    @Test
    void size() {
        BitSet bs2 = new BitSet();
        bs2.set(1);
        bs2.set(5);
        bs2.set(8);
        bs2.set(832);
        bs2.set(1528);
        // System.out.println("bs2: " + bs2.toString());
        System.out.println("bs2.cardinality: " + bs2.cardinality());
        assertEquals(5, bs2.cardinality());
    }

    @Test
    void test2() {
        BitSet bs1 = new BitSet();
        bs1.set(2);
        bs1.set(5);
        bs1.set(7);

        BitSet bs2 = new BitSet();
        bs2.set(1);
        bs2.set(5);
        bs2.set(8);
        bs2.set(832);
        bs2.set(1528);

        BitSet bs1clone = (BitSet) bs1.clone();
        bs1clone.and(bs2);
        // System.out.println("logical 'and': " + bs1clone.toString());
        assertEquals("{5}", bs1clone.toString());

        BitSet bs1clone2 = (BitSet) bs1.clone();
        bs1clone2.or(bs2);
        // System.out.println("logical 'or': " + bs1clone2.toString());
        assertEquals("{1, 2, 5, 7, 8, 832, 1528}", bs1clone2.toString());
    }
}
