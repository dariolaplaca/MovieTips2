package com.gruppo4java11.MovieTips.enumerators;

/**
 * Logical status of our records in the Database, i.e: Whether they are Active or Deleted
 */
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
