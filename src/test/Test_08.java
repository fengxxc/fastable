package test;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import main.Fastable;
import test.bean.People;

/**
 * Test_08
 * 可排序属性值（Integer）
 */
public class Test_08 {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws ParseException {
        List<People> list = (List<People>) fileUtils.readCSVToBeans(
                    new File("").getAbsolutePath() + File.separator + "src//test//test-data.csv",
                    People.class
                );
        for (People people : list) {
            System.out.println(people.toString());
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        long start = System.currentTimeMillis(); // 获取开始时间

        Fastable<People> tp = new Fastable<People>(list);
        System.out.println("-------prop2IntValMap-------");
        System.out.println(tp.getProp2IntValIndexer().toString());
        System.out.println("-------find: -100 <= weight <= 60-------");
        List<People> query0 = tp.queryRange("weight", -100, 60, true, true).fetch();
        for (People p : query0)
            System.out.println(p.toString());
        
        long end = System.currentTimeMillis(); // 获取结束时间
        System.out.println("程序运行时间： " + (end - start) + "ms");
    }
}