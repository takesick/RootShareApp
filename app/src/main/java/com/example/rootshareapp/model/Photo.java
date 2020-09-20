package com.example.rootshareapp.model;

public class Photo {
    public String name;
    public String string;

    public Photo() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Photo(String string) {
        this.string = string;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getName() {
        return name;
    }

    public String getString() {
        return string;
    }
}
