package com.licenta.scheduler.exceptions;

public class LocationNotFoundException extends RuntimeException{

    public LocationNotFoundException(Long id) { super("Could not find location with id: " + id); }
}
