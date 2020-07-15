package com.fengxxc.fastable.analysis;

import com.carrotsearch.sizeof.RamUsageEstimator;
import com.fengxxc.fastable.DataUtils;
import com.fengxxc.fastable.Fastable;
import com.fengxxc.fastable.bean.People;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Complexity {

    @Test
    void timeAndSpace() {
        final List<People> list = DataUtils.getDataFromCsv();

        long start = System.currentTimeMillis(); // 获取开始时间
        Fastable<People> tp = new Fastable<>(list);
        long end = System.currentTimeMillis(); // 获取结束时间
        Runtime runtime = Runtime.getRuntime();
        System.out.println("程序运行空间: " + (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024 + " M");
        System.out.println(" 原始数据占用空间: " + (RamUsageEstimator.sizeOf(list)) + " B");
        System.out.println("fastbale占用空间: " + (RamUsageEstimator.sizeOf(tp)) + " B");
        System.out.println("程序运行时间： " + (end - start) + "ms");
    }
}
