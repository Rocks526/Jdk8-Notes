package java.util;

/**
 * 供Timer使用的定时任务
 */
public abstract class TimerTask implements Runnable {

    /**
     * 锁
     */
    final Object lock = new Object();

    /**
     * 任务状态
     */
    int state = VIRGIN;


    /**
     * 尚未开始
     */
    static final int VIRGIN = 0;

    /**
     * 被调度 但尚未执行
     */
    static final int SCHEDULED   = 1;

    /**
     * 已执行/正在执行
     */
    static final int EXECUTED    = 2;

    /**
     * 已取消
     */
    static final int CANCELLED   = 3;


    /**
     * 任务的下一次执行时间   毫秒时间戳
     */
    long nextExecutionTime;


    /**
     * 任务执行周期
     *      负数表示固定延迟执行 0表示单次执行 不是重复执行任务
     */
    long period = 0;


    protected TimerTask() {
    }


    /**
     * 任务要执行的具体操作 由子类实现
     */
    public abstract void run();


    /**
     * 取消任务   只是简单的修改状态标识位
     * 调用后任务不会再执行 如果任务之前执行过或者当前正在执行 无能为力
     * @return
     */
    public boolean cancel() {
        synchronized(lock) {
            boolean result = (state == SCHEDULED);
            state = CANCELLED;
            return result;
        }
    }


    /**
     * 返回任务最近上一次的执行时间
     * @return
     */
    public long scheduledExecutionTime() {
        synchronized(lock) {
            return (period < 0 ? nextExecutionTime + period
                               : nextExecutionTime - period);
        }
    }
}
