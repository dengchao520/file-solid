package client.cache;

import client.task.ReceiveTaskItem;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientTaskCache {

    private final static ConcurrentHashMap<String, ReceiveTaskItem> CLIENT_TASKS = new ConcurrentHashMap<>();
    private static AtomicInteger TASK_NO = new AtomicInteger(1);

    /**
     * 保存任务
     *
     * @param clientTaskItemInterface
     */
    public static void putTask(int taskNo, ReceiveTaskItem clientTaskItemInterface) {
        CLIENT_TASKS.put("task-" + taskNo, clientTaskItemInterface);
    }

    /**
     * 获取任务集合
     *
     * @return
     */
    public static ConcurrentHashMap<String, ReceiveTaskItem> getTaskMap() {
        return CLIENT_TASKS;
    }

    /**
     * 获取当前单号
     *
     * @return
     */
    public static int gettaskNo() {
        return TASK_NO.getAndAdd(1);
    }
}
