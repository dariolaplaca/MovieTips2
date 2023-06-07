package com.gruppo4java11.MovieTips.exceptionHandler;

public class MovieErrorResponse {
    private int status;
    private String messageError;
    private long timeStamp;

    public MovieErrorResponse(){}

    public MovieErrorResponse(int status, String messageError, long timeStamp) {
        this.status = status;
        this.messageError = messageError;
        this.timeStamp = timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
