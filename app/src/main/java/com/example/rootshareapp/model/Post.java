package com.example.rootshareapp.model;

public class Post {

    public String uid;
    public String author;
    public String body;
    public String created_at;
    public int starCount = 0;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String created_at, String body) {
        this.uid = uid;
        this.author = author;
        this.created_at = created_at;
        this.body = body;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUid() {
        return uid;
    }

    public String getAuthor() {
        return author;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getBody() {
        return body;
    }
}
