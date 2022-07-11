package service.task.item;

import service.cache.ServiceTaskCache;
import service.start.FileSendService;
import service.task.item.interfaces.TaskManageItem;

public class TaskListProgress implements TaskManageItem {

    @Override
    public void exec(String taskName) {
        ServiceTaskCache.getTaskMap().forEach((k, v) -> {
            System.out.println(k + "--已经传输---" + v.fileTaskState.getTaskProgress());
        });
    }
}
