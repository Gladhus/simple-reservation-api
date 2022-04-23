package com.gladhus.volcanocampingapi.domain;

public enum ReservationStatus {
    /**
     * When a reservation is created, status is set to ACTIVE
     */
    ACTIVE,
    /**
     * When a reservation is cancelled, status is set to CANCELLED.
     */
    CANCELLED;

    @Override
    public String toString() {
        return name();
    }
}