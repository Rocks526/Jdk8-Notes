package java.io;

/**
 * 字节输入流抽象类，供子类继承，需要子类提供返回下一个输入字节的方法
 */
public abstract class InputStream implements Closeable {


    // 用来确定跳字符时最大缓冲区大小
    private static final int MAX_SKIP_BUFFER_SIZE = 2048;


    // 从字节流中读取下一个字节返回 如果到达末尾 返回-1
    public abstract int read() throws IOException;

    // 从字节流中读取一定数量的字节并将他们存储到缓冲区数组
    // 阻塞式调用 会一直阻塞到读取文件末尾或者缓冲区填满
    // 返回读取的字节数量
    // 如果读取到末尾 没有任何字节 则返回-1
    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }
    public int read(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte)c;

        int i = 1;
        try {
            for (; i < len ; i++) {
                c = read();
                if (c == -1) {
                    break;
                }
                b[off + i] = (byte)c;
            }
        } catch (IOException ee) {
        }
        return i;
    }

    // 跳过字节流的N个字节 返回实际跳过的字节数  最多只能跳过MAX_SKIP_BUFFER_SIZE
    public long skip(long n) throws IOException {

        long remaining = n;
        int nr;

        if (n <= 0) {
            return 0;
        }

        int size = (int)Math.min(MAX_SKIP_BUFFER_SIZE, remaining);
        byte[] skipBuffer = new byte[size];
        while (remaining > 0) {
            nr = read(skipBuffer, 0, (int)Math.min(size, remaining));
            if (nr < 0) {
                break;
            }
            remaining -= nr;
        }

        return n - remaining;
    }


    // 非阻塞的返回此输入流可读的字节数 如果输入流已关闭则抛出异常
    public int available() throws IOException {
        return 0;
    }

    // 关闭输入流
    public void close() throws IOException {}

    // 标记当前位置 之后通过reset方法可以重新读取
    // 参数readlimit是标记位置之后可读取的最大字节数 超过则可能重置失败
    public synchronized void mark(int readlimit) {}

    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    // 测试输入流是否支持标记/重置方法
    public boolean markSupported() {
        return false;
    }

}
