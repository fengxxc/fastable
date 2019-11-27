package main;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

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

    public static String toUpperFristChar(String string) {
        char[] charArray = string.toCharArray();
        charArray[0] -= 32;
        return String.valueOf(charArray);
    }

}