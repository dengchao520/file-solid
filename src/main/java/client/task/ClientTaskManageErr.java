package client.task;

import client.cache.ClientTaskCache;
import client.task.item.*;
import client.task.item.interfaces.ClientTaskManageItem;
import state.FileTaskState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientTaskManageErr {
    public void run() {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("Enter a line:");
            try {
                String out = stdin.readLine();
                //判断控制台输出的内容，用于是否进行任务状态查询以及任务暂停
                ClientTaskManageItem taskManageItem = null;
                String teskName = "";
                if (out.contains("create")) {
                    taskManageItem = new ClientTaskCreate();
                    System.out.print("请输入服务端获取任务的文件夹:");
                    teskName = stdin.readLine();
                    //获取当前任务号
                    int taskNo = ClientTaskCache.gettaskNo();
                    //创建接受任务处理类
                    ReceiveTaskItem receiveTaskItem = new ReceiveTaskItem(teskName, "task-" + taskNo,new FileTaskState());
                    //启动任务
                    new Thread(receiveTaskItem).start();
                    //存储当前任务
                    ClientTaskCache.putTask(taskNo, receiveTaskItem);
                } else if (out.contains("stop")) {
                    taskManageItem = new ClientTaskStop();
                    System.out.print("请输入任务名称:");
                    teskName = stdin.readLine();
                    if (!ClientTaskCache.getTaskMap().containsKey(teskName)) {
                        System.out.println(teskName + "--任务不存在---");
                    } else {
                        System.out.println(teskName + "---暂停");
                        ClientTaskCache.getTaskMap().get(teskName).fileTaskState.setTaskState(true);
                    }
                } else if (out.contains("start")) {
                    taskManageItem = new ClientTaskStart();
                    System.out.print("请输入任务名称:");
                    teskName = stdin.readLine();
                    if (!ClientTaskCache.getTaskMap().containsKey(teskName)) {
                        System.out.println(teskName + "--任务不存在---");
                    } else {
                        System.out.println(teskName + "---开始");
                        ClientTaskCache.getTaskMap().get(teskName).fileTaskState.setTaskState(false);
                    }
                } else if (out.contains("state")) {
                    taskManageItem = new ClientTaskProgress();
                    System.out.print("请输入任务名称:");
                    teskName = stdin.readLine();
                } else if (out.contains("list")) {
                    taskManageItem = new ClientTaskListProgress();
                } else {
                    System.out.println("未知操作---" + out);
                }
                if (taskManageItem != null) {
                    taskManageItem.exec(teskName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
