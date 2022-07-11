package client.start;

import client.task.ClientTaskManage;
import manage.TaskManage;
import system.Start;

import java.io.*;

/**
 * 客户端任务启动
 */
public class FileReceiveClient implements Start {

    public static void main(String[] args) throws IOException {
        //创建客户端启动类
        Start start = new FileReceiveClient();
        //启动
        start.start();
    }

    @Override
    public void start() {
        //启动客户端任务管理
        TaskManage taskManage = new ClientTaskManage();
        taskManage.manage();
    }
}
