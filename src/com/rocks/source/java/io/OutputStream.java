package java.io;

/**
 * 字节输出流 接收一些输出的字节 将他们输出到一些sink
 */
public abstract class OutputStream implements Closeable, Flushable {

    public abstract void write(int b) throws IOException;


    // 将字节数组参数输出到这个输出流
    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                   ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        for (int i = 0 ; i < len ; i++) {
            write(b[off + i]);
        }
    }


    // 刷新缓冲区  将字节刷入sink (操作系统层)
    public void flush() throws IOException {
    }


    // 关闭输出流
    public void close() throws IOException {
    }

}
