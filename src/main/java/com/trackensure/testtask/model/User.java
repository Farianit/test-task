package com.trackensure.testtask.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class User {

    private int id;
    private String name;
    private Timestamp createdTime;

    @JsonCreator
    public User(@JsonProperty("id") int id,@JsonProperty("name") String name, @JsonProperty("createdTime") Timestamp createdTime) {
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

    public Timestamp getCreatedTime() {
        return createdTime;
    }
}
