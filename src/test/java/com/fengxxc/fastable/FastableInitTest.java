package com.fengxxc.fastable;

import org.junit.jupiter.api.Test;
import com.fengxxc.fastable.bean.People;

import java.text.ParseException;
import java.util.*;

import static com.fengxxc.fastable.Utils.date;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FastableInitTest {

    /**
     * 有唯一列的List<bean>数据集
     * @throws ParseException
     */
    @Test
    void testInit_1() throws ParseException {
        final List<People> list = DataUtils.getBeanData();

        long start = System.currentTimeMillis(); // 获取开始时间
        Fastable<People> tp = new Fastable<People>(list, "name");
        // name: 神乐
        List<People> query0 = tp.fetchQuery("name", "神乐");
        assertEquals(1, query0.size());
        assertEquals(list.get(1).toString(), query0.get(0).toString());

        // birth: 1999-03-09
        List<People> query1 = tp.fetchQuery("birth", date("1999-03-09"));
        assertEquals(2, query1.size());
        assertEquals(list.get(1).toString(), query1.get(0).toString());
        assertEquals(list.get(2).toString(), query1.get(1).toString());

    }

    /**
     * 无唯一列的List<bean>数据集
     * @throws ParseException
     */
    @Test
    void testInit_2() throws ParseException {
        final List<People> list = DataUtils.getBeanData();
        Fastable<People> tp = new Fastable<>(list);
        // name: 神乐
        List<People> query0 = tp.fetchQuery("name", "神乐");
        assertEquals(1, query0.size());
        assertEquals(list.get(1).toString(), query0.get(0).toString());

        // birth: 1999-03-09
        List<People> query1 = tp.fetchQuery("birth", date("1999-03-09"));
        assertEquals(2, query1.size());
        assertEquals(list.get(1).toString(), query1.get(0).toString());
        assertEquals(list.get(2).toString(), query1.get(1).toString());

    }

    /**
     * List<数据源 实现了 Map>
     * @throws ParseException
     */
    @Test
    void testInit_3() throws ParseException {
        final List<Map<String, Object>> list = DataUtils.getMapData();
        Fastable<Map<String, Object>> tp = new Fastable<>(list);
        // name: 神乐
        List<Map<String, Object>> query0 = tp.fetchQuery("name", "神乐");
        assertEquals(1, query0.size());
        assertEquals(list.get(1).toString(), query0.get(0).toString());

        // birth: 1999-03-09
        List<Map<String, Object>> query1 = tp.fetchQuery("birth", date("1999-03-09"));
        assertEquals(2, query1.size());
        assertEquals(list.get(1).toString(), query1.get(0).toString());
        assertEquals(list.get(2).toString(), query1.get(1).toString());

        // gender: 'M' and birth: 1999-03-09
        List<Map<String, Object>> query2 = tp.query("gender", 'M').and("birth", date("1999-03-09")).fetch();
        assertEquals(1, query2.size());
        assertEquals(list.get(2).toString(), query2.get(0).toString());
    }

    /**
     * 多一点的数据
     * @throws ParseException
     */
    @Test
    void testInit_4() throws ParseException {
        final List<People> list = DataUtils.getDataFromCsv();
        Fastable<People> tp = new Fastable<>(list);
        // name: 神乐
        List<People> query0 = tp.fetchQuery("name", "神乐");
        assertEquals(1, query0.size());
        assertEquals(list.get(2).toString(), query0.get(0).toString());

        // birth: 1971-02-06
        List<People> query1 = tp.fetchQuery("birth", date("1971-02-06"));
        assertEquals(1, query1.size());
        assertEquals(list.get(14).toString(), query1.get(0).toString());

    }

}