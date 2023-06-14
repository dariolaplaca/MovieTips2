package com.gruppo4java11.MovieTips.enumerators;

//TODO Aggiungere valore A o D da inserire nel database
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

    @Override
    public String toString() {
        return status;
    }
}
