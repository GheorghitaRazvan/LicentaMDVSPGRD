package com.licenta.scheduler.exceptions;

public class RoadNotFoundException extends RuntimeException{

    public RoadNotFoundException(Long id) { super("Could not find road with id: " + id); }
}
