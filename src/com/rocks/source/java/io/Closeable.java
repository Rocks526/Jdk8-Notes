package java.io;

import java.io.IOException;

/**
 * 此接口代表可关闭的资源，例如IO等，在调用close方法后会将资源关闭
 */
public interface Closeable extends AutoCloseable {

    public void close() throws IOException;

}
