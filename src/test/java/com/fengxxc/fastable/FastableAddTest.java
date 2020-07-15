package com.fengxxc.fastable;

import com.fengxxc.fastable.bean.People;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FastableAddTest {

    /**
     * 初始化后再添加数据
     */
    @Test
    void addTest() {
        final List<People> list = DataUtils.getDataFromCsv();
        Fastable<People> tp = new Fastable<>(list);

        People bean = new People("空之英秋", "JUMP", null, 'M');
        tp.addData(bean);

        // birth: null
        List<People> query0 = tp.fetchQuery("birth", null);
        assertEquals(3, query0.size());
    }
}
