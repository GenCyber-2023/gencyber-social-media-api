package com.example.gencybersocialmediaapi.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Post {
    @JsonProperty
    private User user;
    @JsonProperty
    private int id;
    @JsonProperty
    private String postContent;
    @JsonProperty
    private Date timeStamp;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPostContent() {
        return postContent;
    }
    public void setPostContent(String text) {
        this.postContent = text;
    }
    public String getUsername() {
        if (user != null) {
            return user.getUsername();
        }
        else {
            return null;
        }
    }
    public void setUser (User user) {
        this.user = user;
    }
    public Date getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
