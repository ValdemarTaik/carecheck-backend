package com.carecheck.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@DiscriminatorValue("RELATIVE")
@EqualsAndHashCode(callSuper = true)
public class Relative extends User {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "relative_wards",
            joinColumns = @JoinColumn(name = "relative_id"),
            inverseJoinColumns = @JoinColumn(name = "ward_id")
    )
    private Set<Ward> wards = new HashSet<>();

    public Relative() {
        this.setReceiveEmailNotifications(true);
        this.setReceiveSmsNotifications(false);
        this.setNotificationDelayMinutes(0);
    }

    public void addWard(Ward ward) {
        wards.add(ward);
    }

    public void removeWard(Ward ward) {
        wards.remove(ward);
    }
}