package client.task.item;

import client.cache.ClientTaskCache;
import client.task.ReceiveTaskItem;
import client.task.item.interfaces.ClientTaskManageItem;
import service.start.FileSendService;

public class ClientTaskListProgress implements ClientTaskManageItem {

    @Override
    public void exec(String taskName) {
        ClientTaskCache.getTaskMap().forEach((k, v) -> {
            System.out.println(k + "--已经传输---" + v.fileTaskState.getTaskProgress());
        });
    }
}
