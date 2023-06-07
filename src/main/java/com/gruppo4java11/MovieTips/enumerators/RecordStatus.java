package com.gruppo4java11.MovieTips.enumerators;

public enum RecordStatus {
    ACTIVE("ACTIVE"),
    DELETED("DELETED");

    private final String status;
    RecordStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

}
