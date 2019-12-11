package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.sortable.ISortableSet;
import main.sortable.SortableIntegerSet;

/**
 * Test_SortableIntegerSet_forEach
 */
public class Test_SortableIntegerSet_forEach {

    public static void main(String[] args) {
        /* SortableIntegerSet sbs = new SortableIntegerSet();

        // ------------------------大量数据---------------------------
        int max = 999999999;
        int min = 0;
        Random random = new Random();
        for (int i = 0; i <= 10000000; i++) {
            int n = random.nextInt(max) % (max - min + 1) + min;
            sbs.add(n);
        }
        // System.out.println(sbs.toString());

        long start = System.currentTimeMillis(); // 获取开始时间
        sbs.forEach((e, i, c) -> {
            return true;
        }, -1, -1, true, true);
        long end = System.currentTimeMillis(); // 获取结束时间
        System.out.println("forEach程序运行时间： " + (end - start) + "ms"); */

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