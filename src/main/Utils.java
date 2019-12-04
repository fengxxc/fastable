package main;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.BitSet;
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

    public static void BitSet_not(BitSet a, BitSet b) {
        BitSet t = (BitSet) a.clone();
        t.and(b);
        int i = t.nextSetBit(0);
        if (i != -1) {
            a.set(i, false);
            for (i = t.nextSetBit(i + 1); i >= 0; i = t.nextSetBit(i + 1)) {
                int endOfRun = t.nextClearBit(i);
                do {
                    a.set(i, false);
                } while (++i < endOfRun);
            }
        }
    }

    public static int[] bitSetToBecimalArray(BitSet bs) {
        int[] res = new int[bs.cardinality()];
        int ri = 0;
        int i = bs.nextSetBit(0);
        if (i != -1) {
            res[ri++] = i;
            for (i = bs.nextSetBit(i + 1); i >= 0; i = bs.nextSetBit(i + 1)) {
                int endOfRun = bs.nextClearBit(i);
                do {
                    res[ri++] = i;
                } while (++i < endOfRun);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        BitSet bs1 = new BitSet();
        bs1.set(2);
        bs1.set(5);
        bs1.set(7);

        BitSet bs2 = new BitSet();
        bs2.set(1);
        bs2.set(5);
        bs2.set(8);

        BitSet_not(bs1, bs2);
        System.out.println(bs1);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        BitSet bs3 = new BitSet();
        bs3.set(31);
        bs3.set(52);
        bs3.set(28);
        int[] bta = bitSetToBecimalArray(bs3);
        System.out.println(Arrays.toString(bta));
    }

}