package com.example.gencybersocialmediaapi.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Post {
    @JsonProperty("username")
    private String username;
    @JsonProperty("postContent")
    private String postContent;

    @JsonProperty("photoURL")
    private String photoURL;

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
    public String getPhotoURL(){
        return photoURL;
    }
    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
