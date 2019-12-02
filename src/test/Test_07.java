package test;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import main.Fastable;
import test.bean.People;

/**
 * Test_07
 * 查询结果的顺序与原始数据的顺序
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

        System.out.println("-------unit: '真选组' or unit: '万事屋阿银' -> 无序-------");
        Fastable<People> tp = new Fastable<People>(list);
        List<People> query0 = tp.query("unit", "真选组").or("unit", "万事屋阿银").fetch();
        for (People p0 : query0)
            System.out.println(p0.toString());
       
        System.out.println("-------unit: '真选组' or unit: '万事屋阿银' -> 按原始顺序-------");
        Fastable<People> tp1 = new Fastable<People>(list, true);
        List<People> query1 = tp1.query("unit", "真选组").or("unit", "万事屋阿银").fetch();
        for (People p1 : query1)
            System.out.println(p1.toString());
        
        

        long end = System.currentTimeMillis(); // 获取结束时间
        System.out.println("程序运行时间： " + (end - start) + "ms");
    }
}