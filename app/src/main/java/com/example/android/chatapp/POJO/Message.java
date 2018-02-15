package com.example.android.chatapp.POJO;

/**
 * Created by eslam on 12-Jan-18.
 */

public class Message {
    private String text;
    private long timestamp;
    private String userID;

    public Message() {
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUserID() {
        return userID;
    }
}
