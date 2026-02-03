package com.carecheck.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@DiscriminatorValue("WARD")
@EqualsAndHashCode(callSuper = true)
public class Ward extends User {

    public Ward() {
        this.setCheckIntervalHours(24);
        this.setNotificationsEnabled(true);
    }
}