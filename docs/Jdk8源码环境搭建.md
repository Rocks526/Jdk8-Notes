# Jdk8源码环境搭建

## tips

> 在项目开发中，查看Jdk源码时是只允许读，无法修改或者加注释的。为了方便学习Jdk源码，需要搭建Jdk环境，可以对Jdk源码进行修改，添加注释等操作。

## Step1：创建空项目

打开IDEA，创建一个普通的Java工程。


## Step2：导入Jdk源码

Jdk的源码位于Jdk文件夹下的src压缩包里，将该压缩包解压到新创建的Java工程里。

![image-20210105152503390](http://rocks526.top/lzx/image-20210105152503390.png)

> 拷贝的时候，建议删除里面的com目录，这个目录不涉及Jdk源码，否则编译会报错。
>
> 也可以在Jdk官网下载com包需要的工具安装即可解决编译报错问题，参考：https://blog.csdn.net/IT_Migrant_worker/article/details/104743218

## Step3： 创建测试模块

在项目中创建一个test子目录，用于做一些源码的测试，最终目录结构如下所示：

![image-20210105152822757](http://rocks526.top/lzx/image-20210105152822757.png)

## Step4： 调整IDEA相关配置

> 调大编译时分给IDEA的堆内存大小

![image-20210105153019645](http://rocks526.top/lzx/image-20210105153019645.png)

> 取消debug时不进Jdk源码的限制

![image-20210105152947143](http://rocks526.top/lzx/image-20210105152947143.png)

> 将程序debug时的源码目录关联到项目下

![image-20210105153212165](http://rocks526.top/lzx/image-20210105153212165.png)

> 写个demo测试，编译代码，测试debug进入source目录的源码

![image-20210105153530660](http://rocks526.top/lzx/image-20210105153530660.png)

# Jdk源码目录结构

![image-20210105153452290](http://rocks526.top/lzx/image-20210105153452290.png)

Jdk核心源码都在src/java目录下：

- applet提供了需要创建一个小程序和用来和其他小程序交流的相关类
- awt提供了UI部分相关的类
- beans提供了基于JavaBean架构组件开发相关的类
- io提供了通过数据流，序列化和文件系统进行交互相关的类
- lang提供了Java编程语言基础设计的类
- math提供了数学运算相关的类
- net提供了实现网络应用程序的类
- nio提供了非阻塞IO相关的支持类
- rmi提供了远程访问的相关类
- security提供了安全框架的相关类
- sql提供了访问数据库的相关支持类
- time提供了时间，日期的相关类
- util提供了容器，并发，日期时间工具，国际化工具等相关类

> 建议阅读顺序：lang --> util --> io --> nio --> math --> time --> net





