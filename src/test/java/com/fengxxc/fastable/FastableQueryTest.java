package com.fengxxc.fastable;

import com.fengxxc.fastable.bean.People;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static com.fengxxc.fastable.Utils.date;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FastableQueryTest {

    /**
     * 逻辑“或”
     * @throws ParseException
     */
    @Test
    void logicOrTest() throws ParseException {
        final List<People> list = DataUtils.getDataFromCsv();
        Fastable<People> tp = new Fastable<>(list);

        // unit: '快援队' and birth: 1987-07-07
        List<People> query0 = tp.query("unit", "快援队")
                                .and("birth", date("1987-07-07")).fetch();
        assertEquals(1, query0.size());
        assertEquals("陆奥", query0.get(0).getName());

        // unit: '登势酒吧' or unit: '万事屋阿银'
        List<People> query1 = tp.query("unit", "登势酒吧")
                                .or("unit", "万事屋阿银").fetch();
        assertEquals(7, query1.size());
        assertArrayEquals(
                new String[]{"坂田银时", "志村新八", "神乐", "定春", "登势", "凯瑟琳", "小玉"}
                , query1.stream().map(e -> e.getName()).toArray()
        );


    }

    /**
     * 逻辑“非”
     * @throws ParseException
     */
    @Test
    void logicNotTest() throws ParseException {
        final List<People> list = DataUtils.getDataFromCsv();
        Fastable<People> tp = new Fastable<>(list);
        // (unit: '登势酒吧' or unit: '万事屋阿银') not gender: 'M'
        List<People> query1 = tp.query("unit", "登势酒吧")
                                .or("unit", "万事屋阿银")
                                .not("gender", 'M').fetch();
        assertEquals(4, query1.size());
        assertArrayEquals(
                new String[]{"神乐", "登势", "凯瑟琳", "小玉"}
                , query1.stream().map(e -> e.getName()).toArray()
        );
    }

    /**
     * 查询对象的属性值为空
     */
    @Test
    void NullTest() {
        final List<People> list = DataUtils.getDataFromCsv();
        Fastable<People> tp = new Fastable<>(list);
        // birth: null
        List<People> query0 = tp.fetchQuery("birth", null);
        assertEquals(2, query0.size());
        assertEquals("齐藤终", query0.get(0).getName());
        assertEquals("佐佐木异三郎", query0.get(1).getName());
    }
}
