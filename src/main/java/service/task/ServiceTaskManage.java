package service.task;

import manage.TaskManage;
import service.task.item.*;
import service.task.item.interfaces.TaskManageItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 任务管理类
 */
public class ServiceTaskManage implements Runnable, TaskManage {
    @Override
    public void manage() {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("Enter a line:");
            try {
                String out = stdin.readLine();
                //判断控制台输出的内容，用于判断执行那种策略实现
                TaskManageItem taskManageItem = null;
                String teskName = "";
                if (out.contains("stop")) {
                    System.out.println("暂停任务---");
                    taskManageItem = new TaskStop();
                    System.out.print("请输入任务名称:");
                    teskName = stdin.readLine();
                } else if (out.contains("start")) {
                    System.out.println("启动任务---");
                    taskManageItem = new TaskStart();
                    System.out.print("请输入任务名称:");
                    teskName = stdin.readLine();
                } else if (out.contains("state")) {
                    System.out.println("获取单个任务进度---");
                    taskManageItem = new TaskProgress();
                    System.out.print("请输入任务名称:");
                    teskName = stdin.readLine();
                } else if (out.contains("list")) {
                    System.out.println("获取全部的任务明细以及进度---");
                    taskManageItem = new TaskListProgress();
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

    @Override
    public void run() {
        manage();
    }
}
