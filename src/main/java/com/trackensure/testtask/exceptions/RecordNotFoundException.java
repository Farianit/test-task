package com.trackensure.testtask.exceptions;

public class RecordNotFoundException extends Exception {

    public RecordNotFoundException() {
        super("Database record not found");
    }
}
