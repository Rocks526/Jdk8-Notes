- [tips](#tips)

- [基于字节的IO操作接口](#zj)
- [基于字符的IO操作接口](#zf)
- [其他重要接口](#wj)
- [Java序列化](#xlh)

# <a id="tips">tips</a>

> Java的I/O操作类主要在包java.io下，大概有80+个类，这些类大概可以分成如下四组：
>
> - 基于字节操作的I/O接口：InputStream和OutputStream
> - 基于字符操作的I/O接口：Writer和Reader
> - 基于磁盘操作的I/O接口：File
> - 基于网络操作的I/O接口：Socket
>
> 前两组主要是传输数据的数据格式，后两组主要是传输数据的方式。
>
> Java的IO体系即围绕InputStream，OutputStream，Writer，Reader，File这五个核心类构建，Socket在java.net网络包中，此处不再讨论。

# <a id="zj">基于字节的IO操作接口</a>

**字节输入流重要接口如下所示**

<img src="C:\Users\67409\Desktop\git-repository\jdk8-SourceCode\images\image-20200323175549596.png" alt="image-20200323175549596" style="zoom:200%;" />

-  AutoCloseable：用于保存资源（文件/Socket句柄）的对象，资源关闭时调用close方法
-  Closeable：类似于Serializable的语义性接口，表明该对象是可以关闭的数据的源或目标，调用close方法以释放对象

-  InputStream：字节输入流抽象类，供子类继承，需要子类提供返回下一个输入字节的方法
-  FileInputStream：文件系统阅读器，从文件系统获取字节输入流
  - 接收一个File文件对象或者path字符串作为输入
-  ObjectInputStream：用于对象序列化和反序列化
  - 接收InputStream接口作为输入，即可以搭配file，pipe等使用
-  ByteArrayInputStream：Byte数组中读取字节流
  - 接收byte数组作为输入
-  PipedInputStream：管道输入字节流
  - 只能接收管道输出流作为输入
-  BufferedInputStream：具备缓冲区功能的字节输入流
  - 接收InputStream接口作为子类，即file，piped等都可以作为输入，为他们增加缓冲区的功能
-  DataInputStream：允许读取原始Java数据

**字节输出流重要接口如下所示**

<img src="C:\Users\67409\Desktop\git-repository\jdk8-SourceCode\images\image-20200323183433844.png" alt="image-20200323183433844" style="zoom:200%;" />

-  Flushable：将缓冲区刷入底层

其他接口都与输入流类似，不再一一赘述。

# <a id="zf">基于字符的IO操作接口</a>

> 不管是磁盘还是网络传输，最小的存储单元都是字节，而不是字符，所以I/O操作的都是字节而不是字符，之所以提供字符操作是因为我们的程序中通常操作的数据都是字符形式的，为了操作方便提供一个直接写字符的I/O接口。

**字符输入流重要接口**

![image-20200323184706500](C:\Users\67409\Desktop\git-repository\jdk8-SourceCode\images\image-20200323184706500.png)

- Readable：可读的字符流/源

**字符输出流重要接口**

![image-20200323185045163](C:\Users\67409\Desktop\git-repository\jdk8-SourceCode\images\image-20200323185045163.png)

顾名思义，常用接口和字节流基本保持一致，只是把字节流转换成了字符流。

**总结图**

| 分类       |        字节输入流        | 字节输出流                | 字符输入流          | 字符输出流          |
| :--------- | :----------------------: | :------------------------ | :------------------ | :------------------ |
| 抽象基类   |      *InputStream*       | *OutputStream*            | *Reader*            | *Writer*            |
| 访问文件   |   **FileInputStream**    | **FileOutputStream**      | **FileReader**      | **FileWriter**      |
| 访问数组   | **ByteArrayInputStream** | **ByteArrayOutputStream** | **CharArrayReader** | **CharArrayWriter** |
| 访问管道   |   **PipedInputStream**   | **PipedOutputStream**     | **PipedReader**     | **PipedWriter**     |
| 访问字符串 |                          |                           | **StringReader**    | **StringWriter**    |
| 缓冲流     |   BufferedInputStream    | BufferedOutputStream      | BufferedReader      | BufferedWriter      |
| 转换流     |                          |                           | InputStreamReader   | OutputStreamWriter  |
| 对象流     |    ObjectInputStream     | ObjectOutputStream        |                     |                     |
| 抽象基类   |   *FilterInputStream*    | *FilterOutputStream*      | *FilterReader*      | *FilterWriter*      |
| 打印流     |                          | PrintStream               |                     | PrintWriter         |
| 推回输入流 |   PushbackInputStream    |                           | PushbackReader      |                     |
| 特殊流     |     DataInputStream      | DataOutputStream          |                     |                     |

> 字节流和字符流的转换器OutputStreamWriter和InputStreamReader采用适配器设计模式，继承字符流接口，通过构造函数组合一个字节流接口，完成字节和字符流之间的转换，可以指定编码格式。

# <a id="wj">其他重要接口</a>

- File（文件特征与管理）：用于文件或者目录的描述信息，例如生成新目录，修改文件名，删除文件，判断文件所在路径等。
- RandomAccessFile（随机文件操作）：它的功能丰富，可以从文件的任意位置进行存取（输入输出）操作。
- FileDescriptor：文件描述符

- ...........

# <a id="xlh">Java序列化</a>

**Java序列化介绍**

- 序列化：将对象写入到IO流中
- 反序列化：从IO流中恢复对象
- 意义：序列化机制允许将实现序列化的Java对象转换位字节序列，这些字节序列可以保存在磁盘上，或通过网络传输，以达到以后恢复成原来的对象。序列化机制使得对象可以脱离程序的运行而独立存在。
- 使用场景：
  - 所有可在网络上传输的对象都必须是可序列化的，比如RMI（remote method invoke,即远程方法调用），传入的参数或返回的对象都是可序列化的，否则会出错
  - 所有需要保存到磁盘的java对象都必须是可序列化的。

****

**Java序列化实现方式**

- 通过Serializable接口实现序列化
- 通过Externalizable接口实现序列化

****

**通过Serializable接口实现序列化**

Serializable接口是一个标记接口，不用实现任何方法。一旦实现了此接口，该类的对象就是可序列化的。

序列化步骤：

- 创建一个ObjectOutputStream输出流
- 调用ObjectOutputStream输出流的writeObject方法输出可序列化对象

反序列化步骤：

- 创建一个ObjectInputStream输入流
- 调用ObjectInputStream对象的readObject方法得到序列化的对象

```java
@Data
public class People implements Serializable {

    private String name;

    private Integer age;

    private String email;

    private String telephone;

    public People(String name,Integer age,String email,String telephone){
        System.out.println("构造方法调用！");
        this.age = age;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
    }
}

public class SerializableTest {
    //序列化
//    public static void main(String[] args) {
//        try {
//            //创建ObjectOutputStream
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("res.txt"));
//            //创建待序列化的实体
//            People rocks = new People("rocks", 21, "rocks526@126.com", "17629154010");
//            //序列化
//            objectOutputStream.writeObject(rocks);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    //反序列化
    public static void main(String[] args) {
        try {
            //创建ObjectInputStream
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("res.txt"));
            //反序列化
            try {
                People o = (People) objectInputStream.readObject();
                System.out.println(o.toString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
输出：People(name=rocks, age=21, email=rocks526@126.com, telephone=17629154010)
```

- 反序列化时并没有打印构造方法调用信息，即反序列化的对象是Jvm自己生成的对象，不是通过构造方法生成的。

```java
@Data
public class Fimaly implements Serializable {

    private Person person;

    private String fimalyName;

    public Fimaly(String fimalyName,Person person){
        this.fimalyName = fimalyName;
        this.person = person;
    }
}
@Data
public class Person  {

    private String name;

    private Integer age;

    private String email;

    private String telephone;

    public Person(String name, Integer age, String email, String telephone){
        System.out.println("构造方法调用！");
        this.age = age;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
    }
}
    //引用成员序列化
    public static void main(String[] args) {
        try {
            //创建ObjectOutputStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("res.txt"));
            //创建待序列化的实体
            Person rocks = new Person("rocks", 21, "rocks526@126.com", "17629154010");
            Fimaly fimaly = new Fimaly("happy finamily", rocks);
            //序列化
            objectOutputStream.writeObject(fimaly);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

java.io.NotSerializableException: cn.rocks.Person
```

- 当类中某个成员属性是引用类型时，如果该成员属性未实现Serializable接口，会抛出NotSerializableException

```java
    //同一个对象多次序列化
    public static void main(String[] args) {
        try {
            //创建ObjectOutputStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("res.txt"));
            //创建待序列化的实体
            Person rocks = new Person("rocks", 21, "rocks526@126.com", "17629154010");
            Fimaly fimaly = new Fimaly("happy finamily", rocks);
            //序列化
            objectOutputStream.writeObject(rocks);
            objectOutputStream.writeObject(fimaly);
            objectOutputStream.writeObject(rocks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            //创建ObjectInputStream
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("res.txt"));
            //反序列化
            try {
                Person o = (Person) objectInputStream.readObject();
                Fimaly f = (Fimaly) objectInputStream.readObject();
                Person o2 = (Person) objectInputStream.readObject();
                System.out.println(o == o2);
                System.out.println(o == f.getPerson());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

结果：
true
true
```

- 同一个对象多次序列化时，并不会将对象信息多次序列化，得到多个对象

Java序列化算法：

- 所有保存到磁盘的对象都有一个序列化编号
- 当程序试图序列化一个对象时，会先检查此对象是否已经序列化过，只有此对象从未（在此虚拟机）被序列化过，才会将此对象序列化为字节序列输出
- 如果此对象已经序列化过，则直接输出编号即可

```java
    public static void main(String[] args) {
        try {
            //创建ObjectOutputStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("res.txt"));
            //创建ObjectInputStream
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("res.txt"));
            //创建待序列化的实体
            Person rocks = new Person("rocks", 21, "rocks526@126.com", "17629154010");
            //序列化
            objectOutputStream.writeObject(rocks);
            rocks.setName("Rocks");
            objectOutputStream.writeObject(rocks);
            Person o = (Person) objectInputStream.readObject();
            Person o2 = (Person) objectInputStream.readObject();
            System.out.println(o == o2);
            System.out.println(o.toString());
            System.out.println(o2.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
```

- Java序列化存在的问题，当某个对象序列化之后，修改对象的属性，重新序列化，由于之前有该对象的序列化标志，因此不会重新保存新的属性。反序列化时，两次读取的都是第一次保存的属性信息

```java
@Data
public class Person implements Serializable{

    private String name;

    private Integer age;

    transient private String email;

    private String telephone;

    public Person(String name, Integer age, String email, String telephone){
        System.out.println("构造方法调用！");
        this.age = age;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
    }
}
    public static void main(String[] args) {
        try {
            //创建ObjectOutputStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("res.txt"));
            //创建ObjectInputStream
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("res.txt"));
            //创建待序列化的实体
            Person rocks = new Person("rocks", 21, "rocks526@126.com", "17629154010");
            //序列化
            objectOutputStream.writeObject(rocks);
            Person o = (Person) objectInputStream.readObject();
            System.out.println(o.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
结果：
Person(name=rocks, age=21, email=null, telephone=17629154010)
```

- 当对Java对象的某些属性不想进行序列化时，可以使用transient关键字修饰，序列化时会忽略该字段，反序列化时会给该属性填充默认值

```java
@Data
public class Person implements Serializable{

    private String name;

    private Integer age;

    transient private String email;

    private String telephone;

    public Person(String name, Integer age, String email, String telephone){
        System.out.println("构造方法调用！");
        this.age = age;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
    }


    //重写序列化方案
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(name);
        out.writeInt(age);
    }

    private void readObject(ObjectInputStream ins) throws IOException,ClassNotFoundException{
         this.name = (String)ins.readObject();
         this.age = ins.readInt();
    }

}

    public static void main(String[] args) {
        try {
            //创建ObjectOutputStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("res.txt"));
            //创建ObjectInputStream
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("res.txt"));
            //创建待序列化的实体
            Person rocks = new Person("rocks", 21, "rocks526@126.com", "17629154010");
            //序列化
            objectOutputStream.writeObject(rocks);
            Person o = (Person) objectInputStream.readObject();        
            System.out.println(o.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
结果：
    Person(name=rocks, age=21, email=null, telephone=null)
```

- 通过重写writeObject和readObject方法可以自定义序列化方案，实现过滤敏感属性，序列化加密等

> ArrayList等容器都是通过重写writeObject和readObject实现自定义序列化方案。
>
> 除了这两个方法之外，通过writeReplace和readResolve也可以实现自定义序列化，这是因为底层序列化时会先调用writeReplace方法然后再调用writeObject方法，writeReplace方法的作用在于序列化自定义更为彻底，可以返回任意对象。
>
> readResolve常用来反序列单例类，保证单例类的唯一性。

****

**通过Externalizable接口实现序列化**

- Externalizable接口是Serializable接口的一个子类，通过此接口也可以实现序列化。

- 实现此接口必须实现接口中的两个方法实现自定义序列化方案
- 必须提供pulic的无参构造器，因为在反序列化的时候需要反射创建对象。

****

**序列化版本号**

在实现Serializable接口后，IDEA提示生成一个序列化ID，此序列化ID可有可无，当没有的时候，Jvm会根据类信息自动生成一个序列化ID。

- 如果序列化后，序列化ID改变，反序列话时会抛出异常
- 如果指定序列化ID，当class属性改变或者Jvm采用不同版本时，彼此之间可以兼容
- 不指定序列化ID，可能不同版本之间的序列化ID生成不一致，导致反序列化失败

> 序列化ID的主要作用在于是否向前保持兼容，当类更新时，如果需要兼容，则不修改ID，如果不需要向前兼容，则修改序列化ID即可

****

**Java序列化过程解析**

