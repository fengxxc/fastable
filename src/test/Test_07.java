package test;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import main.Fastable;
import test.bean.People;

/**
 * Test_07
 * 可排序属性值（Integer）
 */
public class Test_07 {

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
        System.out.println(tp.getProp2IntValMap().toString());
        System.out.println("-------find: 100 <=stature <= 170-------");
        List<People> query0 = tp.queryRange("stature", 100, 170, true, true).fetch();
        for (People p : query0)
            System.out.println(p.toString());
        System.out.println("-------find: 170 < stature <= 190-------");
        List<People> query1 = tp.queryRange("stature", 170, 190, false, true).fetch();
        for (People p : query1)
            System.out.println(p.toString());
        System.out.println("-------find: 160 <= stature <= 180 and 45 <= weight < 60 and gender = F-------");
        List<People> query2 = tp.queryRange("stature", 160, 180, true, true)
                                .andRange("weight", 45, 60, true, false)
                                .and("gender", 'F')
                                .fetch();
        for (People p : query2)
            System.out.println(p.toString());

        long end = System.currentTimeMillis(); // 获取结束时间
        System.out.println("程序运行时间： " + (end - start) + "ms");
    }
}