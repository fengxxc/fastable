package com.fengxxc.fastable;

import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

public class FieldsRefSetTest {
    @Test
    void test() {
        FieldsRefSet frs = new FieldsRefSet(1);
        assertEquals(1, frs.getPrimaryKeyRef());
        assertEquals(new BitSet(), frs.getFieldRefSet());

        frs.addFieldRefs(5, 8, 3, 49, 4);
        assertArrayEquals(new int[]{3, 4, 5, 8, 49}, Utils.BitSetToIntegerArray(frs.getFieldRefSet()));

        assertEquals("{1: {3, 4, 5, 8, 49}}", frs.toString());

        assertArrayEquals(new int[]{1, 3, 4, 5, 8, 49}, Utils.BitSetToIntegerArray(frs.getAllRefs()));
    }
}
