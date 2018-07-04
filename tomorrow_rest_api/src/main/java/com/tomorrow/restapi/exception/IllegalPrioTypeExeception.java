package com.tomorrow.restapi.exception;

public class IllegalPrioTypeExeception extends RuntimeException {
    private static final long serialVersionUID = -5657476680933612254L;
    private String prio;

    public IllegalPrioTypeExeception(String prio){
        super(String.format("%s is not a viable prio", prio));
        this.prio = prio;
    }

    public String getPrio(){
        return prio;
    }
}