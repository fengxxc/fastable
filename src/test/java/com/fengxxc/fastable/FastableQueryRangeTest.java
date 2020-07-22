package com.fengxxc.fastable;

import com.fengxxc.fastable.bean.People;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FastableQueryRangeTest {

    /**
     * 可排序属性值（Integer）
     */
    @Test
    void sortableIntegerTest() {
        final List<People> list = DataUtils.getDataFromCsv();
        Fastable<People> tp = new Fastable<>(list);
//        System.out.println("-------prop2IntValMap-------");
//        System.out.println(tp.getProp2IntValIndexer().toString());

        // find: 100 <=stature <= 170
        List<People> query0 = tp.queryRange("stature", 100, 170, true, true).fetch();
        assertEquals(24, query0.size());
        assertArrayEquals(
                new String[]{
                        "志村新八", "神乐", "定春", "登势"
                        , "凯瑟琳", "小玉", "志村妙", "柳生九兵卫"
                        , "冲田总悟", "山崎退", "高杉晋助", "陆奥"
                        , "神威", "澄夜公主", "月咏", "松平片栗虎"
                        , "松平栗子", "今井信女", "日轮", "结野克莉斯特"
                        , "外道丸", "寺门通", "HATA", "猿飞菖蒲"
                }
                , query0.stream().map(e -> e.getName()).toArray()
        );

        // find: 170 < stature <= 190
        List<People> query1 = tp.queryRange("stature", 170, 190, false, true).fetch();
        assertEquals(11, query1.size());
        assertArrayEquals(
                new String[]{
                        "坂田银时", "长谷川泰三", "近藤勋", "土方十四郎"
                        , "齐藤终", "桂小太郎", "坂本辰马", "德川茂茂"
                        , "伊丽莎白", "服部全藏", "佐佐木异三郎"
                }
                , query1.stream().map(e -> e.getName()).toArray()
        );

        // find: 160 <= stature <= 180 and 45 <= weight < 60 and gender = F
        List<People> query2 = tp.queryRange("stature", 160, 180, true, true)
                .andRange("weight", 45, 60, true, false)
                .and("gender", 'F')
                .fetch();
        assertEquals(7, query2.size());
        assertArrayEquals(
                new String[]{
                        "登势", "凯瑟琳", "志村妙", "月咏"
                        , "松平栗子", "今井信女", "寺门通"
                }
                , query2.stream().map(e -> e.getName()).toArray()
        );

        // find: -40 <= weight <= 40
        List<People> query3 = tp.queryRange("weight", -40, 40, true, true).fetch();
        assertEquals(4, query3.size());
        assertArrayEquals(
                new String[]{
                        "神乐"
                        , "寿限无寿限无扔屎机前天小新的内裤新八的人生巴尔蒙克·费扎利昂艾萨克·施奈德三分之一的纯情之感情的剩下三分之二是在意倒刺的感情背叛好像知道我的名字我知道他不知道的不在家干鱿鱼干青鱼子粪坑这个跟刚才的不同哦这个是池乃鱼辣油雄帝宫王木村皇呸呸呸呸呸呸呸呸呸呸呸小屎丸"
                        , "澄夜公主"
                        , "外道丸"
                }
                , query3.stream().map(e -> e.getName()).toArray()
        );
    }
}
