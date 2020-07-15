package com.fengxxc.fastable;

import com.fengxxc.fastable.sortable.ISortableSet;
import com.fengxxc.fastable.sortable.SortableIntegerSet;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortableIntegerSetTest {
    @Test
    void forEachTest() {
        ISortableSet<Integer> sbs1 = new SortableIntegerSet();
        sbs1.add(-5);
        sbs1.add(-4);
        sbs1.add(-2);
        sbs1.add(-1);
        sbs1.add(0);
        sbs1.add(5);
        sbs1.add(6);
        sbs1.add(321);
        System.out.println(sbs1.toString());
        List<Integer> l = new ArrayList<Integer>();
        sbs1.forEach((e, i) -> {
            l.add(e);
            return true;
        }, null, 5, true, true);
        System.out.println(Arrays.toString(l.toArray(new Integer[l.size()])));
    }
}
