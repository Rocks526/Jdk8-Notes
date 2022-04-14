package java.lang;

import java.lang.annotation.*;


/**
 * 声明函数接口 其修饰的接口只有一个抽象方法
 *      可以用lambda表达式实现
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FunctionalInterface {}
