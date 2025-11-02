package uk.ac.plymouth.danielkern.comp2000.data;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReservationItem {
    private final UUID reservationId;
    private final LocalDateTime reservationTime;
    private final int numberOfGuests;
    private int numberOfChildren;
    private int numberOfHighChairs;

    public ReservationItem(LocalDateTime reservationTime, int numberOfGuests, int numberOfChildren, int numberOfHighChairs) {
        this.reservationTime = reservationTime;
        this.numberOfGuests = numberOfGuests;
        this.numberOfChildren = numberOfChildren;
        this.numberOfHighChairs = numberOfHighChairs;
        reservationId = UUID.randomUUID();
    }

    public ReservationItem(LocalDateTime reservationTime, int numberOfGuests, int numberOfChildren, int numberOfHighChairs, UUID reservationId) {
        this.reservationTime = reservationTime;
        this.numberOfGuests = numberOfGuests;
        this.reservationId = reservationId;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public int getNumberOfHighChairs() {
        return numberOfHighChairs;
    }
}
