package com.example.rootshareapp.model;

public class Photo {
    public String name;
    public String uri;
    public String created_at;

    public Photo() {
    }

    public Photo(String uri, String created_at) {
        this.uri = uri;
        this.created_at = created_at;
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
