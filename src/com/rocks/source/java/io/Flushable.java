package java.io;

import java.io.IOException;

/**
 * 可刷新的对象
 * 调用flush方法将任何缓冲区数据刷入底层
 */
public interface Flushable {

    /**
     * Flushes this stream by writing any buffered output to the underlying
     * stream.
     *
     * @throws IOException If an I/O error occurs
     */
    void flush() throws IOException;
}
