package com.licenta.scheduler.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roads")
public class Road {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "startL_id", nullable = false, referencedColumnName = "id")
    private Location startingLocation;

    @ManyToOne
    @JoinColumn(name = "finishL_id", nullable = false, referencedColumnName = "id")
    private Location finishingLocation;

    private double cost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getFinishingLocation() {
        return finishingLocation;
    }

    public void setFinishingLocation(Location finishingLocation) {
        this.finishingLocation = finishingLocation;
    }

    public Location getStartingLocation() {
        return startingLocation;
    }

    public void setStartingLocation(Location startingLocation) {
        this.startingLocation = startingLocation;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
