package com.trackensure.testtask.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class Message {

    private int id;
    private User author;
    private String text;

    private Timestamp sentTime;

    @JsonCreator
    public Message(@JsonProperty("id") int id,@JsonProperty("author") User author,@JsonProperty("message") String message,@JsonProperty("sentTime") Timestamp sentTime) {
        this.id = id;
        this.author = author;
        this.text = message;
        this.sentTime = sentTime;
    }

    public Message(User author, String text) {
        this.author = author;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getSentTime() {
        return sentTime;
    }
}
