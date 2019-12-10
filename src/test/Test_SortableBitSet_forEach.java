package test;

import java.util.Random;

import main.sortable.SortableIntegerSet;

/**
 * Test_SortableBitSet_forEach
 */
public class Test_SortableBitSet_forEach {

    public static void main(String[] args) {
        SortableIntegerSet sbs = new SortableIntegerSet();

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
        System.out.println("forEach程序运行时间： " + (end - start) + "ms");

        /* SortableBitSet sbs2 = (SortableBitSet) sbs.clone();
        long start2 = System.currentTimeMillis(); // 获取开始时间
        sbs2.forEach2((e, i, c) -> {
            return true;
        }, -1, -1, false, false);
        long end2 = System.currentTimeMillis(); // 获取结束时间
        System.out.println("forEach2程序运行时间： " + (end2 - start2) + "ms"); */
    }
}