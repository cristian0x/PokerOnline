package data;

import java.io.Serializable;

public class LoginMessage implements Serializable {
    private String msgType;
    private boolean requestRespone;
    private String username;
    private String password;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public boolean isRequestRespone() {
        return requestRespone;
    }

    public void setRequestRespone(boolean requestRespone) {
        this.requestRespone = requestRespone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
