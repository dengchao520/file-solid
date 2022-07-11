package service.dto;

import java.io.File;

/**
 * 文件属性
 *
 * @author 
 * @title: FileAtt
 * @projectName file-solid
 * @description: TODO
 * @date 2022/6/17 18:04
 */
public class FileAtt {

    private File file;
    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 已发送大小
     */
    private long sentSize;

    public FileAtt() {
    }

    public FileAtt(String fileName, long fileSize, File file) {
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSentSize() {
        return sentSize;
    }

    public void setSentSize(long sentSize) {
        this.sentSize = sentSize;
    }
}
