package java.lang;


/**
 * 任务对象 启动子线程需要传入此对象
 */
@FunctionalInterface
public interface Runnable {

    /**
     * 任务执行逻辑
     */
    public abstract void run();
}
