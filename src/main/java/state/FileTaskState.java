package state;

import java.math.BigDecimal;

public class FileTaskState implements TaskState {

    /**
     * 任务状态
     */
    protected boolean taskState;

    /**
     * 当前任务已传大小
     */
    protected long currentTaskSize;

    /**
     * 总任务大小
     */
    protected long taskTotalSize;

    @Override
    public String getTaskProgress() {
        return BigDecimal.valueOf(this.currentTaskSize)
                .divide(BigDecimal.valueOf(this.taskTotalSize), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100)) + "%";
    }

    @Override
    public boolean isTaskState() {
        return taskState;
    }

    @Override
    public void setTaskState(boolean taskState) {
        this.taskState = taskState;
    }

    @Override
    public long getCurrentTaskSize() {
        return currentTaskSize;
    }

    @Override
    public void setCurrentTaskSize(long currentTaskSize) {
        this.currentTaskSize += currentTaskSize;
    }

    @Override
    public long getTaskTotalSize() {
        return taskTotalSize;
    }

    @Override
    public void setTaskTotalSize(long taskTotalSize) {
        this.taskTotalSize = taskTotalSize;
    }
}
