package service.task;

import service.dto.FileAtt;
import state.FileTaskState;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @title: SendTaskItem
 * @projectName file-solid
 * @description: TODO
 * @date 2022/6/17 15:34
 */
public class SendTaskItemErr implements Runnable {

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private OutputStream outputStream;
    private InputStream inputStream;
    private DataInputStream dataInputStream;
    public FileTaskState fileTaskState;


    public SendTaskItemErr(Socket socket) {
        this.socket = socket;
    }

    /**
     * 传出任务处理方法
     */
    @Override
    public void run() {
        try {
            System.out.println("文件传输任务开始执行");
            //1、初始化io
            this.outputStream = this.socket.getOutputStream();
            this.dataOutputStream = new DataOutputStream(this.outputStream);
            this.inputStream = this.socket.getInputStream();
            this.dataInputStream = new DataInputStream(this.inputStream);
            this.fileTaskState = new FileTaskState();

            //2、获取文件夹路径 ，并且获文件夹下全部的文件
            String path = dataInputStream.readUTF();
            System.out.println("请求传输的文件夹目录是---" + path);
            //3、获取路径下全部的文件信息
            List<FileAtt> fileAtts = new ArrayList<>();
            File file = new File(path);
            if (file.exists()) {
                //目录存在
                File[] files = file.listFiles();
                for (File item : files) {
                    fileAtts.add(new FileAtt(item.getName(), item.length(), item));
                }
            } else {
                System.out.println("获取的目录不存在");
                throw new RuntimeException("获取的目录不存在");
            }
            //4、发送文件信息
            try {
                //告诉客户端要发送的文件数量
                this.dataOutputStream.writeInt(fileAtts.size());
                System.out.println("本次发送给客户端的文件数量---" + fileAtts.size());
                //告诉客户端总传输文件大小
                long totalSize = 0;
                for (FileAtt item : fileAtts) {
                    totalSize += item.getFileSize();
                }
                //设置总的任务大小
                this.fileTaskState.setTaskTotalSize(totalSize);
                System.out.println("本次发送给客户端的文件总大小---" + totalSize);
                this.dataOutputStream.writeLong(totalSize);
                for (FileAtt item : fileAtts) {
                    //先发送总的文件大小
                    dataOutputStream.writeUTF(item.getFileName());
                    dataOutputStream.flush();
                    dataOutputStream.writeLong(item.getFileSize());
                    dataOutputStream.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //5、遍历准备单个文件传输，考虑可能会暂停或者查询进度
            for (FileAtt item : fileAtts) {
                FileInputStream fileInputStream = null;
                try {
                    System.out.println(item.getFileName() + "----开始发送");
                    fileInputStream = new FileInputStream(item.getFile());

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
                    System.out.println(item.getFileName() + "----文件已经传输完毕");
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
            System.out.println("文件传输任务执行结束");
        } catch (Exception e) {
            System.out.println("传输文件发生异常---" + e.getMessage());
            e.printStackTrace();
        } finally {
            //6、发送结束后释放资源
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
    }


}
