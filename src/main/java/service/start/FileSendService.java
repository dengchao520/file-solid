package service.start;

import service.cache.ServiceTaskCache;
import service.config.ServiceConfig;
import service.task.SendTaskItem;
import service.task.ServiceTaskManage;
import state.FileTaskState;
import system.Start;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author
 * @title: FileSendService
 * @projectName file-solid
 * @description: TODO
 * @date 2022/6/17 14:55
 */
public class FileSendService implements Start {
    /**
     * 用户保存任务信息
     */

    public static void main(String[] args) throws IOException {
        //创建服务端
        Start fileSendService = new FileSendService();
        //启动
        fileSendService.start();
    }

    @Override
    public void start() throws IOException {
        ServerSocket socketServer = new ServerSocket(ServiceConfig.SERVICE_PORT);
        //启动控制台任务管理，用来获取任务状态 暂停 开始任务
        new Thread(new ServiceTaskManage()).start();
        while (true) {
            Socket socket = socketServer.accept();
            System.out.println("监听到文件传输任务-----------");
            //创建
            SendTaskItem sendTaskItem = new SendTaskItem(socket, new FileTaskState());
            new Thread(sendTaskItem).start();
            //将任务保存
            ServiceTaskCache.getTaskMap().put("task-" + ServiceTaskCache.gettaskNo(), sendTaskItem);
        }
    }
}
