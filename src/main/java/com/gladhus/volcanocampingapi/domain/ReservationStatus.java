package com.gladhus.volcanocampingapi.domain;

public enum ReservationStatus {
    ACTIVE, DELETED;

    @Override
    public String toString() {
        return name();
    }
}
