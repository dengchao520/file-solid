package service.task.item;

import service.cache.ServiceTaskCache;
import service.start.FileSendService;
import service.task.item.interfaces.TaskManageItem;

public class TaskStart implements TaskManageItem {
    @Override
    public void exec(String taskName) {
        if (!ServiceTaskCache.getTaskMap().containsKey(taskName)) {
            System.out.println(taskName + "--任务不存在---");
        } else {
            System.out.println(taskName + "---开始");
            ServiceTaskCache.getTaskMap().get(taskName).fileTaskState.setTaskState(false);
        }
    }
}
