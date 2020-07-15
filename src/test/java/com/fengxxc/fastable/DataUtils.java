package com.fengxxc.fastable;

import com.fengxxc.fastable.bean.People;

import java.io.File;
import java.text.ParseException;
import java.util.*;

import static com.fengxxc.fastable.Utils.date;

public class DataUtils {

    static List<People> getBeanData() throws ParseException {
        final List<People> list = new ArrayList<People>();
        list.add(new People("坂田银时", "万事屋阿银", new Date(), 'M'));
        list.add(new People("神乐", "万事屋阿银", date("1999-03-09"), 'F'));
        list.add(new People("志村新八", "万事屋阿银", date("1999-03-09"), 'M'));
        return list;
    }

    static List<Map<String, Object>> getMapData() throws ParseException {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(new HashMap<String, Object>() {{
            put("name", "坂田银时"); put("old", 38); put("unit", "万事屋阿银"); put("birth", new Date()); put("gender", 'M');
        }});
        list.add(new HashMap<String, Object>() {{
            put("name", "神乐"); put("old", 14); put("unit", "万事屋阿银"); put("birth", date("1999-03-09")); put("gender", 'F');
        }});
        list.add(new HashMap<String, Object>() {{
            put("name", "志村新八"); put("old", 14); put("unit", "万事屋阿银"); put("birth", date("1999-03-09")); put("gender", 'M');
        }});
        return list;
    }

    public static List<People> getDataFromCsv() {
        return (List<People>) fileUtils.readCSVToBeans(
            new File("").getAbsolutePath() + File.separator + "src//test//resources//test-data.csv",
            People.class
        );
    }
}
