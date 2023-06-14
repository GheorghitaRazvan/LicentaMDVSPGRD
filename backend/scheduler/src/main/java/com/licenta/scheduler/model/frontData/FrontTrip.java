package com.licenta.scheduler.model.frontData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class FrontTrip {
    private String startingId;

    private String finishingId;

    private String userId;

    private String startingTime;

    private String finishingTime;

    private String persons;

    public Long getStartingId() {
        return parseLong(startingId);
    }
    public Long getFinishingId() {
        return parseLong(finishingId);
    }

    public Long getUserId() {
        return parseLong(userId);
    }

    public Date getStartingTime() {
        String formatPattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
        try {
            return sdf.parse(startingTime);
        } catch (ParseException e) {
            return null;
        }
    }

    public Date getFinishingTime() {
        String formatPattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
        try {
            return sdf.parse(finishingTime);
        } catch (ParseException e) {
            return null;
        }
    }

    public int getPersons() {
        return parseInt(persons);
    }
}
