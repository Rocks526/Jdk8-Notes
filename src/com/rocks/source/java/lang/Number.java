package java.lang;


// 数值类型的基类 提供各种数值类型之间的转换操作接口
public abstract class Number implements java.io.Serializable {

    // 返回int数值 如果太长 可能截断
    public abstract int intValue();

    // 返回long类型的数值
    public abstract long longValue();

    // 返回float类型的数值
    public abstract float floatValue();

    // 返回double类型的数值
    public abstract double doubleValue();

    // 返回byte类型的数值
    public byte byteValue() {
        return (byte)intValue();
    }

    // 返回short类型的数值
    public short shortValue() {
        return (short)intValue();
    }

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -8742448824652078965L;
}
