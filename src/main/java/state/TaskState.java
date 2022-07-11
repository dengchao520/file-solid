package state;

public interface TaskState {
    String getTaskProgress();
    boolean isTaskState();
    void setTaskState(boolean taskState);
    void setCurrentTaskSize(long currentTaskSize);
    void setTaskTotalSize(long taskTotalSize);
    long getCurrentTaskSize();
    long getTaskTotalSize();
}
