package com.gladhus.volcanocampingapi.domain;

public enum ReservationStatus {
    ACTIVE, CANCELLED;

    @Override
    public String toString() {
        return name();
    }
}
