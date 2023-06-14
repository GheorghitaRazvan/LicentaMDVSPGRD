package com.licenta.scheduler.model;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "vehicle")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "depot_id", nullable = false, referencedColumnName = "id")
    private Location depot;

    private Long capacity;

    @Transient
    private boolean wasUsed = false;

    public Long getId() {
        return id;
    }

    public boolean getWasUsed() {
        return this.wasUsed;
    }

    public void setWasUsed () {
        this.wasUsed = !this.wasUsed;
    }
}
