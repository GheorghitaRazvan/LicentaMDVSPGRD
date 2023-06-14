package com.licenta.scheduler.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.Date;

@Entity
@Table(name = "trips")
@Getter
@Setter
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "startL_id", nullable = false, referencedColumnName = "id")
    private Location startingLocation;

    @ManyToOne
    @JoinColumn(name = "finishL_id", nullable = false, referencedColumnName = "id")
    private Location finishingLocation;

    @Temporal(TemporalType.TIME)
    private Date startingTime;

    @Temporal(TemporalType.TIME)
    private Date finishingTime;

    private int persons;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    private String status;

    public Duration getStartingTimeAsDuration() {
        String[] startingTimeStringSplit = this.startingTime.toString().split(":");
        String durationFormat = "PT";
        durationFormat += startingTimeStringSplit[0];
        durationFormat += "H";
        durationFormat += startingTimeStringSplit[1];
        durationFormat += "M";
        return Duration.parse(durationFormat);
    }

    public Duration getFinishingTimeAsDuration() {
        String[] finishingTimeStringSplit = this.finishingTime.toString().split(":");
        String durationFormat = "PT";
        durationFormat += finishingTimeStringSplit[0];
        durationFormat += "H";
        durationFormat += finishingTimeStringSplit[1];
        durationFormat += "M";
        return Duration.parse(durationFormat);
    }
}
