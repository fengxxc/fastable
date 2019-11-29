package test;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.Utils;
import test.bean.People;

/**
 * fileUtils
 */
public class fileUtils {

    private static Date date(String dateStr) throws ParseException {
        return new SimpleDateFormat("yyyy/MM/dd").parse(dateStr);
    }

    public static List<? extends Object> readCSVToBeans(String path, Class<?> clazz) {
        List<Object> list = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = null;
            line = reader.readLine();// 第一行，标题
            line = line.substring(1); // 去除BOM空白符
            String[] columns = line.split(",");
            while ((line = reader.readLine()) != null) {
                String item[] = line.split(",");
                Object bean = clazz.newInstance();
                for (int i = 0; i < columns.length; i++) {
                    PropertyDescriptor propDesc = new PropertyDescriptor(Utils.fristChartoUpper(columns[i]), clazz);
                    Object ite = item[i];
                    if (propDesc.getPropertyType().isAssignableFrom(Date.class)) {
                        ite = date((String) ite);
                    }
                    if (propDesc.getPropertyType().toString().equals("char")) {
                        ite = ite.toString().toCharArray()[0];
                    }
                    System.out.print(propDesc.getPropertyType() + " :: ");
                    System.out.print(columns[i] + " :: ");
                    System.out.println(ite);
                    propDesc.getWriteMethod().invoke(bean, ite);
                }
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        List<People> beans = (List<People>) readCSVToBeans("E://github//fastable//bin//test//test-data.csv", People.class);
        for (People people : beans) {
            System.out.println(people.toString());
        }
    }

}