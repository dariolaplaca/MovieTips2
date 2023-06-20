package com.gruppo4java11.MovieTips.entities;

import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

/**
 * This class is the parent class of all the entities in the database
 */

//TODO Aggiungere Auditables (ModifiedBy, CreatedBy, ModifiedOn, CreatedOn) Strings e LocalDates
@MappedSuperclass
public class AuditableEntity {
    @Column(name = "record_status")
    @Enumerated(EnumType.STRING)
    private RecordStatus recordStatus;
    @Column(name = "modified_by")
    private String modifiedBy;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "modified_on")
    private LocalDate modifiedOn;
    @Column(name = "created_on")
    private LocalDate createdOn;

    /**
     * Constructor for the Record Superclass
     * recordStatus is hardCoded as Active because every time we retrieve this object, we can only retrieve active objects and not Deleted objects from the database OR
     *                     at the moment of its creation
     */
    public AuditableEntity(){
        this.recordStatus = RecordStatus.ACTIVE;
    }

    public RecordStatus getRecordStatus() {
        return this.recordStatus;
    }
    public void setRecordStatus(RecordStatus recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getModifiedOn() {
        return this.modifiedOn;
    }

    public void setModifiedOn(LocalDate modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }
}
