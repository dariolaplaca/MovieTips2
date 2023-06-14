package com.gruppo4java11.MovieTips.entities;

import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;

/**
 * This class it's the parent class of all the entities in the database
 */

//TODO Aggiungere Auditables (ModifiedBy, CreatedBy, ModifiedOn, CreatedOn) Strings e LocalDates
    //TODO cambiare nome classe in AuditableEntity
@MappedSuperclass
public class Record {
    @Column(name = "record_status")
    @Enumerated(EnumType.STRING)
    private RecordStatus recordStatus;
    //TODO aggiungere .getValue() appena creato
    public Record(RecordStatus recordStatus){
        this.recordStatus = RecordStatus.ACTIVE;
    }
    public Record(){}

    public RecordStatus getRecordStatus() {
        return this.recordStatus;
    }
    public void setRecordStatus(RecordStatus recordStatus) {
        this.recordStatus = recordStatus;
    }
}
