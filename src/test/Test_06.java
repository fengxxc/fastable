package test;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import main.Fastable;
import test.bean.People;

/**
 * Test_06
 * 查询对象的属性值为空
 */
public class Test_06 {

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
        System.out.println("-------birth: null-------");
        List<People> query0 = tp.fetchQuery("birth", null);
        for (People p0 : query0)
            System.out.println(p0.toString());
        
        

        long end = System.currentTimeMillis(); // 获取结束时间
        System.out.println("程序运行时间： " + (end - start) + "ms");
    }
}