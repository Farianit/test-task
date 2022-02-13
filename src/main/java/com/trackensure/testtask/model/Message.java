package com.trackensure.testtask.model;

import java.sql.Timestamp;

public class Message {

    private int id;
    private User author;
    private String text;

    private Timestamp sentTime;

    public Message(int id, User author, String message, Timestamp sentTime) {
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
