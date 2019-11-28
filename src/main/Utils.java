package main;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
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

    public static Map<String, Method> getBeanReadMethods(PropertyDescriptor[] propDesc) {
        Map<String, Method> prm = new HashMap<String, Method>();
        for (PropertyDescriptor pd : propDesc) {
            Method rm = pd.getReadMethod();
            if (rm.getName().equals("getClass"))
                continue;
            prm.put(rm.getName().substring(3), rm);
        }
        return prm;
    }

    public static String toUpperFristChar(String string) {
        char[] charArray = string.toCharArray();
        charArray[0] -= 32;
        return String.valueOf(charArray);
    }

}