package client.task;

import client.task.item.*;
import client.task.item.interfaces.ClientTaskManageItem;
import system.SystemOutInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemOutStart implements SystemOutInterface {

    @Override
    public void startOut() {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("Enter a line:");
            try {
                String out = stdin.readLine();
                //判断控制台输出的内容，用于判断执行那种策略实现
                ClientTaskManageItem taskManageItem = null;
                String teskName = "";
                if (out.contains("create")) {
                    System.out.print("请输入服务端获取任务的文件夹:");
                    teskName = stdin.readLine();
                    taskManageItem = new ClientTaskCreate();
                } else if (out.contains("stop")) {
                    System.out.print("请输入任务名称:");
                    teskName = stdin.readLine();
                    taskManageItem = new ClientTaskStop();
                } else if (out.contains("start")) {
                    System.out.print("请输入任务名称:");
                    teskName = stdin.readLine();
                    taskManageItem = new ClientTaskStart();
                } else if (out.contains("state")) {
                    System.out.print("请输入任务名称:");
                    teskName = stdin.readLine();
                    taskManageItem = new ClientTaskProgress();
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
