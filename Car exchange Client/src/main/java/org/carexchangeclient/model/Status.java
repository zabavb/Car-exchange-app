package org.carexchangeclient.model;

public class Status {
    private boolean isSuccess;
    private String message;
    private String exception;

    public boolean isSuccess() {
        return isSuccess;
    }

    public Status(boolean isSuccess, String message, String exception) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.exception = exception;
    }

    public Status(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public Status() {
    }

    @Override
    public String toString() {
        return isSuccess ?
                "Success: " + message :
                !exception.isEmpty() ?
                        "Error: " + message + " (" + exception + ")" :
                        "Error: " + message;
    }
}