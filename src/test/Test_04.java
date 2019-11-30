package test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import main.Fastable;
import test.bean.People;

/**
 * Test_04 大量数据
 */
public class Test_04 {

    private static Date date(String dateStr) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws ParseException {
        List<People> list = (List<People>) fileUtils.readCSVToBeans(
                    new File("").getAbsolutePath() + File.separator + "bin//test//test-data.csv",
                    People.class
                );
        for (People people : list) {
            System.out.println(people.toString());
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        long start = System.currentTimeMillis(); // 获取开始时间

        Fastable<People> tp = new Fastable<People>(list);
        System.out.println("-------name: 神乐-------");
        List<People> query0 = tp.fetchQuery("name", "神乐");
        for (People p0 : query0)
            System.out.println(p0.toString());
        
        System.out.println("-------birth: 1907-11-04-------");
        List<People> query1 = tp.fetchQuery("birth", date("1907-11-04"));
        for (People p1 : query1)
            System.out.println(p1.toString());

        System.out.println("-------gender: 'M' and birth: 1999-03-09-------");
        List<People> query2 = tp.query("gender", 'M').query("birth", date("1999-03-09")).fetch();
        for (People p2 : query2)
            System.out.println(p2.toString());

        long end = System.currentTimeMillis(); // 获取结束时间
        System.out.println("程序运行时间： " + (end - start) + "ms");
    }
}