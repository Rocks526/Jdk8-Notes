- [tips](#tips)
- [static](#static)
- [final](#final)
- [try catch finally](#try)
- [transient](#tran)
- [default](#de)

# <a id="tips">tips</a>

> Java 中的关键字很多，每个关键字都代表着不同场景下的不同含义，接下来挑选 几个比较重要的关键字逐一分析

# <a id="static">static</a>

**static修饰对象**

------

static代表静态的、全局的，可修饰属性，方法，方法块，一旦被修饰，说明被修饰的东西在一定范围内是共享的，谁都可以访问，这时候需要注意并发读写的问题。

- 当static修饰属性、方法时，代表该属性、方法属于类本身，与实例无关，当该属性、方法被public修饰时，无需初始化类实例即可通过类名.属性名/方法名访问
- 当static修饰代码块时，叫做静态代码块，常用于在类启动前做一些初始化操作

被static修饰的属性可被多线程访问，可能存在并发问题，可以通过线程安全容器或者锁来解决

被static修饰的属性和方法属于类本身，与实例无关，虽然破坏了面向对象的封装性，但易于访问，适合一些与实例无关的属性和方法

 **static初始化时机**

------

static修饰的对象属于类，因此加载时机也是类初始化时，由于此时实例还未初始化，因此static方法不能调用实例属性，static代码块同理

```java
public class StaticTest1 {

    static {
        System.out.println("父类静态代码块加载！");
    }

    public static ArrayList staticAttribute = new ArrayList(){{
        System.out.println("父类静态变量加载！");
    }};

    public static void staticMethod(){
        System.out.println("父类静态方法加载！");
    }

    public StaticTest1(){
        System.out.println("父类构造方法加载！");
    }
}

public class StaticTest2 extends StaticTest1{

    public static ArrayList staticAttribute = new ArrayList(){{
        System.out.println("子类静态变量加载！");
    }};

    static {
        System.out.println("子类静态代码块加载！");
    }

    public static void staticMethod(){
        System.out.println("子类静态方法加载！");
    }

    public StaticTest2(){
        super();
        System.out.println("子类构造方法加载！");
    }

    public static void main(String[] args) {
        new StaticTest2();
    }

}
```

以上测试代码的输出结果为：

```properties
父类静态代码块加载！
父类静态变量加载！
子类静态变量加载！
子类静态代码块加载！
父类构造方法加载！
子类构造方法加载！
```

以上可以看出：

- static先加载，同被static修饰的代码按先后顺序加载
- 父类优先于子类加载
- 静态代码加载完成后再进行实例初始化，执行构造方法

# <a id="final">final</a>

final表示不可变的，可修饰类，属性，方法：

- final修饰类时，表明该类无法被继承
- final修饰属性时，表明该属性引用不可变
- final修饰方法时，表明该方法无法被重写

> 当final修饰属性时，引用不可变不代表属性不可变，如ArrayList，Map等容器属性，虽然属性引用不可变，即一直指向创建出来的对象，但该对象本身可以变化

# <a id="try">try  catch  finally</a>

try、catch、finally是Java异常处理的关键字，try用来包括可能出现异常的代码，catch用来捕获异常，finally里是无论异常是否捕获到都会执行的代码。

- 执行顺序为try -> catch -> finally -> return

- catch可以捕获多种异常，但只会匹配其中某一种，当匹配到后即停止匹配，开始执行finally代码，因此捕获时，应先捕获子类异常后捕获父类异常

```java
public class FinallyTest {


    public static Integer FinallyTest(){
        try {
            System.out.println("try executed...");
            throw new Exception();
        }catch (Exception e){
            System.out.println("catch executed...");
            System.exit(0);
        }finally {
            System.out.println("finally executed...");
        }
        System.out.println("func return...");
        return 1;
    }

    public static void main(String[] args) {
        FinallyTest.FinallyTest();
    }

}
```
> 正常情况下，finally代码都会被执行，除非在finally执行之前出现Jvm崩溃或者手动调用System.exit等，示例代码如下：此时finally代码不会执行。

# <a id="volatile">volatile</a>

- volatile介绍

volatile是Jvm提供的一个关键字，用于保证多线程编程的线程安全。

volatile只能修饰属性，一般用来修饰共享变量，可以保证该变量在多线程之间的可见性和禁止指令重排序。

- volatile原理

volatile是如何保证可见性以及禁止指令重排的呢？

可见性是由于多核CPU缓存导致的，volatile的内存语义即：遇到volatile修饰的共享变量时，当该变量值被修改时，内存会主动通知所有CPU缓存，该值已经失效，应该从内存中的重新拉取。

> 操作系统为了提高性能，在CPU中增加了缓存，每次读取时优先读取CPU缓存再读取主内存，导致的结果即可能某个线程修改了共享变量，而其他线程可能在另一个CPU运行，使用该CPU内之前的缓存值导致数据出错。
>
> 解决方案其实也很简单，只需要禁止CPU缓存即可，但为了性能考虑，不能全部禁止，因此操作系统将禁止CPU缓存的权限交给了程序员，由程序员在需要的地方使用volatile关键字禁止CPU缓存。

# <a id="tran">transient</a>

- transient介绍

transient是Java提供的一个序列化相关的关键字，可以用来修饰类属性和实例属性，表明该属性序列化时忽略。

- transient原理

transient其实和Serializable接口类似，本身并没有意义，更多类似于一个标志，实现的逻辑在序列化的底层代码中，当序列化时某个属性被transient修饰时，就将该字段忽略。

- transient应用

一般用来在网络传输时忽略掉部分不需要的大字段或者敏感信息。

在ArrayList等数组类容器中，Java为了优化性能减少内存消耗，对存储数据的数组用transient修饰，不让其序列化，自己手动实现writeObject方法重写序列化方案，只序列化有用的值，空值忽略。

# <a id="de">default</a>

- default介绍

default 关键字一般会用在接口的方法上，意思是对于该接口，子类是无需强制实现的，但自己必须有默认实现。

- default应用

在Jdk8中，容器相关类新增了很多方法，为了向前兼容使用了default关键字，父类提供默认实现，子类无需修改原有代码，需要实现的子类手动重写。