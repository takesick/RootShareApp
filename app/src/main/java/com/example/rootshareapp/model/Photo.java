package com.example.rootshareapp.model;

public class Photo {
    public String name;
    public String uri;

    public Photo() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Photo(String uri) {
        this.uri = uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }
}
