package service.file;

import service.dto.FileAtt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @title: FileMessage
 * @projectName file-solid
 * @description: TODO
 * @date 2022/6/17 18:01
 */
public class FileMessage {
    /**
     * 获取要发送的文件夹下所有的文件状态
     *
     * @param path
     * @return
     */
    public static List<FileAtt> getFiles(String path) {
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
        return fileAtts;
    }

}
