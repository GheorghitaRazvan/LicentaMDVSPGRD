package com.licenta.scheduler.exceptions;

public class TripNotFoundException extends RuntimeException{

    public TripNotFoundException(Long id) { super("Could not find trip with id: " + id); }
}
