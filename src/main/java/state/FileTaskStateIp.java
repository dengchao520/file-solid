package state;

public class FileTaskStateIp extends FileTaskState {

    //当前请求的id
    private String userIp;

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }
}
