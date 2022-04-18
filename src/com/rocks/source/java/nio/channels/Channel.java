package java.nio.channels;

import java.io.IOException;
import java.io.Closeable;

/**
 * Jdk1.4引入的IO操作通道，与Stream相比，Channel的API是非阻塞的，是双向的
 * 主要包含下面四个实现类：
 *      1.SocketChannel ： 一个客户端用来发起 TCP 的 Channel
 *      2.ServerSocketChannel ：一个服务端用来监听新进来的连接的 TCP 的 Channel 。对于每一个新进来的连接，都会创建一个对应的 SocketChannel 。
 *      3.DatagramChannel ：通过 UDP 读写数据。
 *      4.FileChannel ：从文件中，读写数据。
 */
public interface Channel extends Closeable {

    // 判断此通道是否处于打开状态
    public boolean isOpen();

    // 关闭通道
    public void close() throws IOException;

}
