package com.example.android.chatapp.POJO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by eslam on 11-Jan-18.
 */
public class Chat implements Serializable {
    private String lastMessageSent;
    private long lastMessageTime;
    private List<String> members;

    public Chat() {
    }

    public Chat(List<String> members) {
        this.members = members;
    }

    public void setLastMessageSent(String lastMessageSent) {
        this.lastMessageSent = lastMessageSent;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getLastMessageSent() {
        return lastMessageSent;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public List<String> getMembers() {
        return members;
    }

}
