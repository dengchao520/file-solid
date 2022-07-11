package client.task.item;

import client.cache.ClientTaskCache;
import client.task.item.interfaces.ClientTaskManageItem;

public class ClientTaskStop implements ClientTaskManageItem {

    @Override
    public void exec(String taskName) {
        if (!ClientTaskCache.getTaskMap().containsKey(taskName)) {
            System.out.println(taskName + "--任务不存在---");
        } else {
            System.out.println(taskName + "---暂停");
            ClientTaskCache.getTaskMap().get(taskName).fileTaskState.setTaskState(true);
        }
    }

}
