package java.util;


/**
 * 标记接口，实现此接口代表容器支持根据下标随机访问
 * 用于在通用算法中，根据下标随机访问的特性进行优化
 * 例如针对查找的算法，如果支持RandomAccess接口，即可通过二分查找实现
 */
public interface RandomAccess {
}
