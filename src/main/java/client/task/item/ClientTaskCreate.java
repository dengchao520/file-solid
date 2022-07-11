package client.task.item;

import client.cache.ClientTaskCache;
import client.task.ReceiveTaskItem;
import client.task.item.interfaces.ClientTaskManageItem;
import state.FileTaskState;
import state.FileTaskStateIp;

public class ClientTaskCreate implements ClientTaskManageItem {


    @Override
    public void exec(String taskName) {
        //获取当前任务号
        int taskNo = ClientTaskCache.gettaskNo();
        //创建接受任务处理类
        ReceiveTaskItem receiveTaskItem = new ReceiveTaskItem(taskName, "task-" + taskNo, new FileTaskState());

        //启动任务
        new Thread(receiveTaskItem).start();
        //存储当前任务
        ClientTaskCache.putTask(taskNo, receiveTaskItem);
    }

}
