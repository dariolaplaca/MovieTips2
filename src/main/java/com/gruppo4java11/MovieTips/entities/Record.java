package com.gruppo4java11.MovieTips.entities;

import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;

/**
 * This class is the parent class of all the entities in the database
 */

//TODO Aggiungere Auditables (ModifiedBy, CreatedBy, ModifiedOn, CreatedOn) Strings e LocalDates
    //TODO cambiare nome classe in AuditableEntity
@MappedSuperclass
public class Record {
    @Column(name = "record_status")
    @Enumerated(EnumType.STRING)
    private RecordStatus recordStatus;
    //TODO aggiungere .getValue() appena creato

    /**
     * Constructor for the Record Superclass
     * @param recordStatus hardCoded as Active because every time we retrieve this object, we can only retrieve active objects and not Deleted objects from the database OR
     *                     at the moment of its creation
     */
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
