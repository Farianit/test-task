package com.trackensure.testtask.model;

import java.sql.Timestamp;

public class User {

    private int id;
    private String name;
    private Timestamp createdTime;

    public User(int id, String name, Timestamp createdTime) {
        this.id = id;
        this.name = name;
        this.createdTime = createdTime;
    }

    public User(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
