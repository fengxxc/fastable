package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import main.Fastable;

/**
 * Test_03
 * List<? 实现了 Map>
 */
public class Test_03 {

    private static Date date(String dateStr) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
    }
    
    public static void main(String[] args) throws ParseException {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        if (Map.class.isAssignableFrom(hashMap.getClass())) {
            System.out.println("********hashMap实现了map********");
        }
        Map<String, Object> treeMap = new TreeMap<String, Object>();
        if (Map.class.isAssignableFrom(treeMap.getClass())) {
            System.out.println("********treeMap实现了map********");
        }
        Map<String, Object> hashtable = new Hashtable<String, Object>();
        if (Map.class.isAssignableFrom(hashtable.getClass())) {
            System.out.println("********hashtable实现了map********");
        }

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L; {
            put("name", "坂田银时"); put("old", 38); put("birth", new Date()); put("gender", 'M');
        }});
        list.add(new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L; {
            put("name", "神乐"); put("old", 14); put("birth", date("1999-03-09")); put("gender", 'F');
        }});
        list.add(new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L; {
            put("name", "志村新八"); put("old", 14); put("birth", date("1999-03-09")); put("gender", 'M');
        }});

        long start = System.currentTimeMillis(); // 获取开始时间

        Fastable<Map<String, Object>> tp = new Fastable<Map<String, Object>>(list);
        System.out.println(tp.toString());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println("-------name: 神乐-------");
        List<Map<String, Object>> query0 = tp.fetchQuery("name", "神乐");
        for (Map<String, Object> p0 : query0)
            System.out.println(p0.toString());

        System.out.println("-------birth: 1999-03-09-------");
        List<Map<String, Object>> query1 = tp.fetchQuery("birth", date("1999-03-09"));
        for (Map<String, Object> p1 : query1)
            System.out.println(p1.toString());

        System.out.println("-------gender: 'M' and birth: 1999-03-09-------");
        List<Map<String, Object>> query2 = tp.query("gender", 'M').query("birth", date("1999-03-09")).fetch();
        for (Map<String, Object> p2 : query2)
            System.out.println(p2.toString());

        long end = System.currentTimeMillis(); // 获取结束时间
        System.out.println("程序运行时间： " + (end - start) + "ms");
    }
}