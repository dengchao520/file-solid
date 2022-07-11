package action;

import service.dto.FileAtt;

/**
 * 接口隔离错误案例
 */
public interface TaskItemInterfaceErr {

    void init();

    void release();

    void sendFileInfo();

    void sendFile(FileAtt file);

    void receive();

}
