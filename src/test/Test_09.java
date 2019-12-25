package test;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import main.Fastable;
import test.bean.People;

/**
 * Test_09
 * 初始化后再添加数据
 */
public class Test_09 {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws ParseException {
        List<People> list = (List<People>) fileUtils.readCSVToBeans(
                    new File("").getAbsolutePath() + File.separator + "src//test//test-data.csv",
                    People.class
                );
        /* for (People people : list) {
            System.out.println(people.toString());
        } */

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        long start = System.currentTimeMillis(); // 获取开始时间

        Fastable<People> tp = new Fastable<People>(list);
        People bean = new People("空之英秋", "JUMP", null, 'M');
        tp.addData(bean);
        System.out.println("-------birth: null-------");
        List<People> query0 = tp.fetchQuery("birth", null);
        for (People p : query0)
            System.out.println(p.toString());
        
        long end = System.currentTimeMillis(); // 获取结束时间
        System.out.println("程序运行时间： " + (end - start) + "ms");
    }
}