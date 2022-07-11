package client.task;

import client.task.item.*;
import client.task.item.interfaces.ClientTaskManageItem;
import manage.TaskManage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientTaskManage implements TaskManage {

    @Override
    public void manage() {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("Enter a line:");
            try {
                String out = stdin.readLine();
                //判断控制台输出的内容，用于判断执行那种策略实现
                ClientTaskManageItem taskManageItem = null;
                String teskName = "";
                if (out.contains("create")) {
                    taskManageItem = new ClientTaskCreate();
                    System.out.print("请输入服务端获取任务的文件夹:");
                    teskName = stdin.readLine();
                } else if (out.contains("stop")) {
                    taskManageItem = new ClientTaskStop();
                    System.out.print("请输入任务名称:");
                    teskName = stdin.readLine();
                } else if (out.contains("start")) {
                    taskManageItem = new ClientTaskStart();
                    System.out.print("请输入任务名称:");
                    teskName = stdin.readLine();
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
