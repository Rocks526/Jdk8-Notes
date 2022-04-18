package com.lizhaoxuan.lang;

/**
 * 包装类型测试
 * @author lizhaoxuan
 */
public class WrapperClassTest {

    public static void main(String[] args) {
        // 包装类和基本类型转换
        transfer();
        // 包装类的缓存机制
        wrapperCache();
        // 自动装箱拆箱
        autoWrapper();
    }

    private static void wrapperCache() {
        System.out.println("=================[wrapperCache]=======================");
        int v1 = 1;
        int v2 = 288;
        Integer v3 = Integer.valueOf(v1);
        Integer v4 = Integer.valueOf(v2);
        Integer v5 = Integer.valueOf(v1);
        Integer v6 = Integer.valueOf(v2);
        System.out.println(v1 + "," + v2 + "," + v3 + "," + v4 + "," + v5 + "," + v6);  // 1,288,1,288,1,288
        System.out.println(v3 == v5);   // true
        System.out.println(v4 == v6);   // false
    }

    private static void transfer() {
        System.out.println("=================[transfer]=======================");
        // int
        int x = 1;
        Integer wrapperX = Integer.valueOf(x);
        int x2 = wrapperX.intValue();
        System.out.println(x + "," + wrapperX + "," + x2);

        // long
        long v = 2l;
        Long wrapperV = Long.valueOf(v);
        long v2 = wrapperV.longValue();
        System.out.println(v + "," + wrapperV + "," + v2);

        // double
        double m = 0.2f;
        Double wrapperM = Double.valueOf(m);
        double m2 = wrapperM.doubleValue();
        System.out.println(m + "," + wrapperM + "," + m2);

        // float
        float n = 0.8f;
        Float wrapperN = Float.valueOf(n);
        float n2 = wrapperN.floatValue();
        System.out.println(n + "," + wrapperN + "," + n2);

        // bool
        boolean p = false;
        Boolean wrapperP = Boolean.valueOf(p);
        boolean p2 = wrapperP.booleanValue();
        System.out.println(p + "," + wrapperP + "," + p2);

        // str
        char o = 'p';
        Character wrapperO = Character.valueOf(o);
        char o2 = wrapperO.charValue();
        System.out.println(o + "," + wrapperO + "," + o2);

        // short
        short c = 2;
        Short wrapperC = Short.valueOf(c);
        short c2 = wrapperC.shortValue();
        System.out.println(c + "," + wrapperC + "," + c2);

        // byte
        byte z = 'm';
        Byte wrapperZ = Byte.valueOf(z);
        byte z2 = wrapperZ.byteValue();
        System.out.println(z + "," + wrapperZ + "," + z2);
    }

    private static void autoWrapper() {
        System.out.println("=================[autoWrapper]=======================");
        // int
        int x = 1;
        Integer wrapperX = x;
        int x2 = wrapperX;
        System.out.println(x + "," + wrapperX + "," + x2);

        // long
        long v = 2l;
        Long wrapperV = v;
        long v2 = wrapperV;
        System.out.println(v + "," + wrapperV + "," + v2);

        // double
        double m = 0.2f;
        Double wrapperM = m;
        double m2 = wrapperM;
        System.out.println(m + "," + wrapperM + "," + m2);

        // float
        float n = 0.8f;
        Float wrapperN = n;
        float n2 = wrapperN;
        System.out.println(n + "," + wrapperN + "," + n2);

        // bool
        boolean p = false;
        Boolean wrapperP = p;
        boolean p2 = wrapperP;
        System.out.println(p + "," + wrapperP + "," + p2);

        // str
        char o = 'p';
        Character wrapperO = o;
        char o2 = wrapperO;
        System.out.println(o + "," + wrapperO + "," + o2);

        // short
        short c = 2;
        Short wrapperC = c;
        short c2 = wrapperC;
        System.out.println(c + "," + wrapperC + "," + c2);

        // byte
        byte z = 'm';
        Byte wrapperZ = z;
        byte z2 = wrapperZ;
        System.out.println(z + "," + wrapperZ + "," + z2);
    }


}
