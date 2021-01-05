- [tips](#tips)
- [枚举基础](#js)
- [枚举集合](#jh)
- [枚举原理](#yl)

# <a id="tips">tips</a>

> 在Java中定义一些常量一般使用static fianl来实现，在Jdk1.5之后，Java推出了枚举类型，通过枚举类型可以实现常量的定义以及一些只有固定值的变量定义。
>
> 枚举类型适合场景：
>
> - 常量
> - 有固定值的变量，如星期，颜色等
> - 系统返回状态码以及异常种类

# <a id="js">枚举基础</a>

- 利用枚举类型定义常量

```java
public enum Color {

    RED,BLANK,YELLOW,GREEN,WHITE

}
   public static void main(String[] args) {

        System.out.println(Color.GREEN);
        System.out.println(Color.BLANK);
        System.out.println(Color.BLANK == Color.RED);
    }
```

- 枚举类型结合switch
  - 在Jdk1.6之后，switch支持enum类型

```java
    public static void main(String[] args) {

        Color color = Color.RED;
        switch (color){
            case RED:
                System.out.println("红色");
                break;
            case BLANK:
                System.out.println("黑色");
                break;
            case GREEN:
                System.out.println("绿色");
                break;
            default:
                System.out.println("其他");
        }
    }
```

- 枚举类型里自定义属性，方法，重写父类方法
  - 构造方法不能为public

```java
public enum  GlobalException {

    USER_NO_LOGIN(1001,"用户未登录异常"),
    USER_PASSWORD_ERROR(1002,"用户密码错误");

    private Integer code;
    private String msg;

    private GlobalException(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
    public Integer getCode(){
        return this.code;
    }
    
    public String getMsg(){
        return this.msg;
    }
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Exception is :" + this.getCode() + "--->" + this.getMsg();
    }

}
public static void main(String[] args) {

        System.out.println(GlobalException.USER_NO_LOGIN.getCode());
        System.out.println(GlobalException.USER_NO_LOGIN.getMsg());
 System.out.println(GlobalException.USER_PASSWORD_ERROR);
    }
```

> 枚举类里每个成员其实是继承于Java中的Enum类，因此枚举类不可再继承其他类，但可以实现接口，通过接口组织枚举结构。

# <a id="jh">枚举集合的使用</a>

除了基础枚举类型之外，Java还提供了EunmMap和EnumSet两个枚举集合工具类供我们使用，位于java.util包下，两者的功能如下：

- EnumSet保证集合中的元素不重复
- EnumMap中的 key是enum类型，而value则可以是任意类型

****

**EnumSet**

```java
    public static void main(String[] args) {

        //创建一个EunmSet 将枚举类型的实例作为set的值
        EnumSet<GlobalException> set = EnumSet.allOf(GlobalException.class);
        System.out.println(set);
        Iterator<GlobalException> iterator = set.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        System.out.println("-------------------------------------");

        //创建一个空的EnumSet
        EnumSet<GlobalException> set2 = EnumSet.noneOf(GlobalException.class);
        System.out.println(set2);
        set2.add(GlobalException.USER_NO_LOGIN);
        System.out.println(set2);
        System.out.println("-------------------------------------");

        //创建一个带指定值的EnumSet
        EnumSet<GlobalException> set3 = EnumSet.of(GlobalException.USER_NO_LOGIN);
        System.out.println(set3);
    }
结果：
[Exception is :1001--->用户未登录异常, Exception is :1002--->用户密码错误]
Exception is :1001--->用户未登录异常
Exception is :1002--->用户密码错误
-------------------------------------
[]
[Exception is :1001--->用户未登录异常]
-------------------------------------
[Exception is :1001--->用户未登录异常]
```

**EnumMap**

```java
public static void main(String[] args) {

        //创建一个空的EnumMap
        EnumMap<GlobalException, Object> map = new EnumMap<>(GlobalException.class);
        System.out.println(map);
        map.put(GlobalException.USER_NO_LOGIN,"用户尚未登录");
        System.out.println(map);
        System.out.println("--------------------------------------------");

        //利用EnumMap为参数创建EnumMap
        EnumMap map2 = new EnumMap(map);
        System.out.println(map2);
        System.out.println("------------------------------------------");

        //利用普通map创建EnumMap  key必须为枚举类型
        HashMap<GlobalException, Object> hashmap = new HashMap<>();
        hashmap.put(GlobalException.USER_PASSWORD_ERROR,"用户密码错误！");
        EnumMap<GlobalException, Object> map3 = new EnumMap<>(hashmap);
        System.out.println(map3);
    }
结果：
{}
{Exception is :1001--->用户未登录异常=用户尚未登录}
--------------------------------------------
{Exception is :1001--->用户未登录异常=用户尚未登录}
------------------------------------------
{Exception is :1002--->用户密码错误=用户密码错误！}
```

# <a id="yl">枚举原理</a>

枚举类和泛型一样，都是编译器的一种语法糖，当一个枚举类型被编译后，会经过以下处理：

- 将枚举类型定义为抽象类，并继承自Enum基类
- 将成员变量定义为枚举类型，并修饰为final static，同样继承自Enum基类，拥有Enum基类的方法
- 重写writeObject，readObject和clone方法，修饰为final类型，这三个方法和枚举通过静态代码块一块进行初始化，保证了枚举类型的不可变性，不能通过克隆，序列化和反序列化来复制，保证枚举常量只有一个实例，即单例。因此推荐使用枚举类型实现单例模式

****

**Enum类**

```java
public abstract class Enum<E extends Enum<E>>
        implements Comparable<E>, Serializable {

    //枚举常量名称
    private final String name;

    //返回枚举常量名称
    public final String name() {
        return name;
    }

    //枚举常量下标
    private final int ordinal;

    //返回枚举常量下标
    public final int ordinal() {
        return ordinal;
    }

    //protect开发者无法调用  由编译器调用
    protected Enum(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    //可重写  默认返回常量名称
    public String toString() {
        return name;
    }

    //比较方法 默认比较地址
    //Enum单例 每个常量只有一个实例
    public final boolean equals(Object other) {
        return this==other;
    }

    //hash
    public final int hashCode() {
        return super.hashCode();
    }

    //重写clone 防止克隆
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    //枚举大小比较  负数小于 正数大于
    public final int compareTo(E o) {
        Enum<?> other = (Enum<?>)o;
        Enum<E> self = this;
        if (self.getClass() != other.getClass() && // optimization
            self.getDeclaringClass() != other.getDeclaringClass())
            throw new ClassCastException();
        return self.ordinal - other.ordinal;
    }

    //返回枚举常量对应的类对象
    @SuppressWarnings("unchecked")
    public final Class<E> getDeclaringClass() {
        Class<?> clazz = getClass();
        Class<?> zuper = clazz.getSuperclass();
        return (zuper == Enum.class) ? (Class<E>)clazz : (Class<E>)zuper;
    }

    //返回带指定名称的指定枚举类型的枚举常量
    public static <T extends Enum<T>> T valueOf(Class<T> enumType,
                                                String name) {
        T result = enumType.enumConstantDirectory().get(name);
        if (result != null)
            return result;
        if (name == null)
            throw new NullPointerException("Name is null");
        throw new IllegalArgumentException(
            "No enum constant " + enumType.getCanonicalName() + "." + name);
    }

    //禁止finalize
    protected final void finalize() { }

    //重写序列化 禁止反序列化 保证单例
    private void readObject(ObjectInputStream in) throws IOException,
        ClassNotFoundException {
        throw new InvalidObjectException("can't deserialize enum");
    }
    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("can't deserialize enum");
    }
}
```

枚举类除了拥有Enum的方法之外，编译器还会生成一个values方法，用于返回所有枚举常量。