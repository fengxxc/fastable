package main;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utils
 */
public class Utils {

    public static PropertyDescriptor[] getBeanPropDesc(Class<?> clazz) throws IntrospectionException {
        BeanInfo beanInfo = null;
        beanInfo = Introspector.getBeanInfo(clazz);
        PropertyDescriptor[] propDesc = beanInfo.getPropertyDescriptors();
        return propDesc;
    }

    public static Map<String, Method> getPropReadMethods(PropertyDescriptor[] propDesc) {
        Map<String, Method> prm = new HashMap<String, Method>();
        for (PropertyDescriptor pd : propDesc) {
            Method rm = pd.getReadMethod();
            if (rm.getName().equals("getClass"))
                continue;
            prm.put(Utils.fristChartoLower(rm.getName().substring(3)), rm);
        }
        return prm;
    }

    public static String fristChartoLower(String string) {
        if (65 <= string.charAt(0) && string.charAt(0) <= 90) {
            char[] charArr = string.toCharArray();
            charArr[0] += 32;
            return String.valueOf(charArr);
        }
        return string;
    }

    public static String fristChartoUpper(String string) {
        if (97 <= string.charAt(0) && string.charAt(0) <= 122) {
            char[] charArr = string.toCharArray();
            charArr[0] -= 32;
            return String.valueOf(charArr);
        }
        return string;
    }

    public static boolean nullOrEmptyStr(String string) {
        return string == null || "".equals(string);
    }

    public static boolean nullOrZeroSize(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static void main(String[] args) {
        System.out.println(fristChartoUpper("qwer"));
        System.out.println(fristChartoLower("QWER"));
        System.out.println(fristChartoLower(".QWER"));
        System.out.println(fristChartoUpper(".QWER"));
    }

}