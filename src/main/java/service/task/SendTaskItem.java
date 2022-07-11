package service.task;

import action.*;
import service.dto.FileAtt;
import service.file.FileMessage;
import state.FileTaskState;
import state.TaskState;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * 服务端任务类
 *
 * @author
 * @title: SendTaskItem
 * @projectName file-solid
 * @description: TODO
 * @date 2022/6/17 15:34
 */
public class SendTaskItem implements Runnable, SendFileInfoAction, InitAction, RelaseAction, SendFileAction {

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private OutputStream outputStream;
    private InputStream inputStream;
    private DataInputStream dataInputStream;
    public TaskState fileTaskState;
    private List<FileAtt> fileAtts;

    public SendTaskItem(Socket socket, TaskState taskState) {
        this.socket = socket;
        this.fileTaskState = taskState;
    }


    /**
     * 传出任务处理方法
     */
    @Override
    public void run() {
        try {
            System.out.println("文件传输任务开始执行");
            //1、初始化io
            init();
            //2、获取文件夹路径 ，并且获文件夹下全部的文件
            String path = getFilePath();
            //3、获取路径下全部的文件信息
            fileAtts = FileMessage.getFiles(path);
            //4、发送文件信息
            sendFileInfo();
            //5、遍历准备单个文件传输，考虑可能会暂停或者查询进度
            for (FileAtt file : fileAtts) {
                sendFile(file);
            }
            System.out.println("文件传输任务执行结束");
        } catch (Exception e) {
            System.out.println("传输文件发生异常---" + e.getMessage());
            e.printStackTrace();
        } finally {
            //6、发送结束后释放资源
            release();
        }
    }


    /**
     * 初始化io资源
     */
    @Override
    public void init() {
        try {
            this.outputStream = this.socket.getOutputStream();
            this.dataOutputStream = new DataOutputStream(this.outputStream);
            this.inputStream = this.socket.getInputStream();
            this.dataInputStream = new DataInputStream(this.inputStream);

        } catch (IOException e) {
            e.printStackTrace();
            release();
        }
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        if (this.outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.dataOutputStream != null) {
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.dataInputStream != null) {
            try {
                dataInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送文件信息
     */
    @Override
    public void sendFileInfo() {
        try {
            //告诉客户端要发送的文件数量
            this.dataOutputStream.writeInt(fileAtts.size());
            System.out.println("本次发送给客户端的文件数量---" + fileAtts.size());
            //告诉客户端总传输文件大小
            long totalSize = 0;
            for (FileAtt file : fileAtts) {
                totalSize += file.getFileSize();
            }
            //设置总的任务大小
            this.fileTaskState.setTaskTotalSize(totalSize);
            System.out.println("本次发送给客户端的文件总大小---" + totalSize);
            this.dataOutputStream.writeLong(totalSize);
            for (FileAtt file : fileAtts) {
                //先发送总的文件大小
                dataOutputStream.writeUTF(file.getFileName());
                dataOutputStream.flush();
                dataOutputStream.writeLong(file.getFileSize());
                dataOutputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送文件
     *
     * @param file
     */
    @Override
    public void sendFile(FileAtt file) {
        FileInputStream fileInputStream = null;
        try {
            System.out.println(file.getFileName() + "----开始发送");
            fileInputStream = new FileInputStream(file.getFile());

            int k;
            byte[] buf = new byte[1024];
            while ((k = fileInputStream.read(buf)) != -1) {
                //如果任务暂定 自旋知道任务开始
                while (fileTaskState.isTaskState()) {
                }
                //将文件流读取的byte写到网络中
                dataOutputStream.write(buf, 0, k);
                dataOutputStream.flush();
                //记录下当前任务已经发送的大小
                fileTaskState.setCurrentTaskSize(k);
            }
            System.out.println(file.getFileName() + "----文件已经传输完毕");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从客户端获取文件夹路径
     *
     * @return
     */
    public String getFilePath() {
        try {
            String path = dataInputStream.readUTF();
            System.out.println("请求传输的文件夹目录是---" + path);
            return path;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
