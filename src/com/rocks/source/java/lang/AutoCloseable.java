package java.lang;

/**
 * 用于保存资源（文件/Socket句柄）的对象，资源关闭时自动调用close方法
 */
public interface AutoCloseable {

    // 资管关闭接口 如果关闭失败 会抛出异常 建议外抛
    void close() throws Exception;
}
