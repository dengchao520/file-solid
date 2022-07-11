package client.task;

import action.*;
import service.dto.FileAtt;
import state.FileTaskState;

import java.io.*;
import java.net.Socket;

/**
 * 客户端任务类
 * @author
 * @title: SendTaskItem
 * @projectName file-solid
 * @description: TODO
 * @date 2022/6/17 15:34
 */
public class ReceiveTaskItemErr implements Runnable, TaskItemInterfaceErr {
    private static String mFilePath = "E:\\test2\\";
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    public FileTaskState fileTaskState;
    //服务端的文件夹路径
    private String serviceFilePath;
    private String savePeath;

    public ReceiveTaskItemErr(String serviceFilePath, String savePeath) {
        this.serviceFilePath = serviceFilePath;
        this.savePeath = savePeath;
    }

    @Override
    public void sendFile(FileAtt file) {

    }

    @Override
    public void run() {
        //1、和服务端建立链接初始化io
        init();
        //2、告诉服务端获取的文件夹路径
        sendFileInfo();
        //3、接收文件
        receive();
        //4、释放资源
        release();
    }

    /**
     * 发送文件夹路径
     */
    @Override
    public void sendFileInfo() {
        try {
            this.dataOutputStream.writeUTF(this.serviceFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化资源
     */
    @Override
    public void init() {
        try {
            socket = new Socket("localhost", 8081);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            fileTaskState = new FileTaskState();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        if (this.dataOutputStream != null) {
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.dataOutputStream != null) {
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
     * 接收文件
     */
    @Override
    public void receive() {
        File dirs = new File(mFilePath + "\\" + savePeath);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        int fileNum = 0;
        long totalSize = 0;
        FileAtt[] fileinfos = null;
        try {
            //先获取总的文件数量
            fileNum = dataInputStream.readInt();
            System.out.println("本次任务传输的任务数量---" + fileNum);
            //获取本次任务总的传输大小
            totalSize = dataInputStream.readLong();
            System.out.println("本次任务传输的文件总大小---" + totalSize);
            fileTaskState.setTaskTotalSize(totalSize);
            //获取每个文件的名称以及大小
            fileinfos = new FileAtt[fileNum];
            for (int i = 0; i < fileNum; i++) {
                fileinfos[i] = new FileAtt();
                fileinfos[i].setFileName(dataInputStream.readUTF());
                fileinfos[i].setFileSize(dataInputStream.readLong());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        // 写满文件后缓存区中剩余的字节长度。
        int leftLen = 0;
        // 当前缓冲区中的字节数
        int bufferedLen;
        // 每次向文件中写入的字节数
        int writeLen;
        // 当前已经向单个文件中写入的字节总数
        long writeLens;
        // 写入的所有字节数
        long totalWriteLens = 0;
        byte buf[] = new byte[8192];
        FileOutputStream fout = null;
        for (int i = 0; i < fileNum; i++) {
            writeLens = 0;
            try {
                fout = new FileOutputStream(mFilePath + "\\" + savePeath + "\\" + fileinfos[i].getFileName());
                while (true) {
                    if (leftLen > 0) {
                        bufferedLen = leftLen;
                    } else {
                        bufferedLen = dataInputStream.read(buf);
                    }
                    if (bufferedLen == -1) {
                        return;
                    }
                    /**
                     * 自旋实现任务暂停
                     */
                    while (fileTaskState.isTaskState()) {
                    }
                    fileTaskState.setCurrentTaskSize(bufferedLen);
                    // 如果已写入文件的字节数加上缓存区中的字节数已大于文件的大小，只写入缓存区的部分内容。
                    if (writeLens + bufferedLen >= fileinfos[i].getFileSize()) {
                        leftLen = (int) (writeLens + bufferedLen - fileinfos[i].getFileSize());
                        writeLen = bufferedLen - leftLen;
                        // 写入部分
                        fout.write(buf, 0, writeLen);
                        totalWriteLens += writeLen;
                        move(buf, writeLen, leftLen);
                        break;
                    } else {
                        // 全部写入
                        fout.write(buf, 0, bufferedLen);
                        writeLens += bufferedLen;
                        totalWriteLens += bufferedLen;
                        if (totalWriteLens >= totalSize) {
                            return;
                        }
                        leftLen = 0;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                if (fout != null) {
                    try {
                        fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void move(byte buf[], int writeLen, int leftLen) {
        for (int i = 0; i < leftLen; i++) {
            if (buf[writeLen + i] != 0) {
                buf[i] = buf[writeLen + i];
            }
        }
    }
}
