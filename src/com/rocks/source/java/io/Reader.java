package java.io;


/**
 * 输入字符流抽象类 子类必须重写read方法 其他方法可选
 */
public abstract class Reader implements Readable, Closeable {


    // 锁  ==> 只所以不加在方法上 而是单独拆出来 有利于控制锁范围 提升效率
    protected Object lock;

    // 以此对象本身作为锁
    protected Reader() {
        this.lock = this;
    }

    // 传入一个对象作为锁
    protected Reader(Object lock) {
        if (lock == null) {
            throw new NullPointerException();
        }
        this.lock = lock;
    }


    // 读取字符到字符缓冲区 返回读取的字符数 如果返回-1则到末尾
    public int read(java.nio.CharBuffer target) throws IOException {
        int len = target.remaining();
        char[] cbuf = new char[len];
        int n = read(cbuf, 0, len);
        if (n > 0)
            target.put(cbuf, 0, n);
        return n;
    }


    // 阻塞读取单个字符 实现很低效 子类可以重写
    public int read() throws IOException {
        char cb[] = new char[1];
        if (read(cb, 0, 1) == -1)
            return -1;
        else
            return cb[0];
    }

    // 阻塞读取字符到数组 返回可读字符数或-1
    public int read(char cbuf[]) throws IOException {
        return read(cbuf, 0, cbuf.length);
    }

    // 阻塞读取字符到数组的某一部分 off是数组开始的下标 len是读取的长度
    abstract public int read(char cbuf[], int off, int len) throws IOException;


    // 允许跳过的最大字符数
    private static final int maxSkipBufferSize = 8192;


    // 用于跳过字符的缓冲区
    private char skipBuffer[] = null;


    // 跳过某些字符 阻塞式
    public long skip(long n) throws IOException {
        if (n < 0L)
            throw new IllegalArgumentException("skip value is negative");
        int nn = (int) Math.min(n, maxSkipBufferSize);
        synchronized (lock) {
            if ((skipBuffer == null) || (skipBuffer.length < nn))
                skipBuffer = new char[nn];
            long r = n;
            while (r > 0) {
                int nc = read(skipBuffer, 0, (int)Math.min(r, nn));
                if (nc == -1)
                    break;
                r -= nc;
            }
            return n - r;
        }
    }

    // 字符流是否准备好被读取 即read是否阻塞
    public boolean ready() throws IOException {
        return false;
    }

    // 是否支持标记操作
    public boolean markSupported() {
        return false;
    }


    // 标记当前位置 之后通过reset方法可以重新读取
    // 参数readlimit是标记位置之后可读取的最大字节数 超过则可能重置失败
    public void mark(int readAheadLimit) throws IOException {
        throw new IOException("mark() not supported");
    }

    public void reset() throws IOException {
        throw new IOException("reset() not supported");
    }

    // 关闭字符流接口
     abstract public void close() throws IOException;

}
