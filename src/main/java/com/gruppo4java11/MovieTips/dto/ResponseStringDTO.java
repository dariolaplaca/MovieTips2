package com.gruppo4java11.MovieTips.dto;

public class ResponseStringDTO {
    private String message;

    public ResponseStringDTO(String message){
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
