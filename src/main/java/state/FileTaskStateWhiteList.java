package state;

import java.util.List;

public class FileTaskStateWhiteList extends FileTaskState {
    //白名单
    private List<String> whiteList;
    //当前请求的id
    private String userIp;

    public List<String> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(List<String> whiteList) {
        this.whiteList = whiteList;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }
}
