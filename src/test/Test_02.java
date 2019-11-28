package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.Fastable;
import test.bean.People;

/**
 * Test_02
 * 无唯一列的List<bean>数据集
 */
public class Test_02 {

    private static Date date(String dateStr) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
    }

    public static void main(final String[] args) throws ParseException {
        long start = System.currentTimeMillis(); // 获取开始时间

        final List<People> list = new ArrayList<People>();
        list.add(new People("坂田银时", 38, new Date(), 'M'));
        list.add(new People("神乐", 14, date("1999-03-09"), 'F'));
        list.add(new People("志村新八", 14, date("1999-03-09"), 'M'));
        Fastable<People> tp = new Fastable<People>(list, People.class);
        System.out.println(tp.toString());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println("-------name: 神乐-------");
        List<People> query0 = tp.fetchQuery("name", "神乐");
        for (People p0 : query0) System.out.println(p0.toString());

        System.out.println("-------birth: 1999-03-09-------");
        List<People> query1 = tp.fetchQuery("birth", date("1999-03-09"));
        for (People p1 : query1) System.out.println(p1.toString());

        System.out.println("-------gender: 'M' and birth: 1999-03-09-------");
        List<People> query2 = tp.query("gender", 'M').query("birth", date("1999-03-09")).fetch();
        for (People p2 : query2) System.out.println(p2.toString());

        long end = System.currentTimeMillis(); // 获取结束时间
        System.out.println("程序运行时间： " + (end - start) + "ms");

    }
}