package com.bdeesorn_r.demo_crud.constant;

public enum Status {
    ACTIVE("Active"),
    SUSPENDED("Suspended");

    public final String value;

    private Status(String value) {
        this.value = value;
    }
}
