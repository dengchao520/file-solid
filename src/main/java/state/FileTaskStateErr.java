package state;

import java.math.BigDecimal;

public class FileTaskStateErr extends FileTaskState {
    @Override
    public String getTaskProgress() {
        return super.currentTaskSize / super.taskTotalSize + "%";
    }
}
