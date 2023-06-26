package com.example.gencybersocialmediaapi.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Post {
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Post Content")
    private String postContent;

    public String getPostContent() {
        return postContent;
    }
    public void setPostContent(String text) {
        this.postContent = text;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername (String username) {
        this.username = username;
    }
}
