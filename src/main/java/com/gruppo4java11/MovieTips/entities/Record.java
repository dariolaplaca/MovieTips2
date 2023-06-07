package com.gruppo4java11.MovieTips.entities;

import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * This class it's the parent class of all the entities in the database
 */
@MappedSuperclass
public class Record {
    @Column(name = "record_status")
    private RecordStatus recordStatus;

    public Record(RecordStatus recordStatus){
        this.recordStatus = recordStatus;
    }
    public Record(){}

    public RecordStatus getRecordStatus() {
        return this.recordStatus;
    }
    public void setRecordStatus(RecordStatus recordStatus) {
        this.recordStatus = recordStatus;
    }
}
