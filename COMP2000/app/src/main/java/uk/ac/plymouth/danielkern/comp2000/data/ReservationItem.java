package uk.ac.plymouth.danielkern.comp2000.data;

import java.time.LocalDateTime;

public class ReservationItem {
    private int reservationId;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private String customerPhone;
    private LocalDateTime reservationTime;
    private int numberOfGuests;
    private int numberOfChildren;
    private int numberOfHighChairs;

    public ReservationItem(LocalDateTime reservationTime, int numberOfGuests, int numberOfChildren, int numberOfHighChairs) {
        this.reservationTime = reservationTime;
        this.numberOfGuests = numberOfGuests;
        this.numberOfChildren = numberOfChildren;
        this.numberOfHighChairs = numberOfHighChairs;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public void setNumberOfHighChairs(int numberOfHighChairs) {
        this.numberOfHighChairs = numberOfHighChairs;
    }

    public ReservationItem(int reservationId, String customerFirstName, String customerLastName, LocalDateTime reservationTime, int numberOfGuests, int numberOfChildren, int numberOfHighChairs) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.reservationTime = reservationTime;
        this.numberOfGuests = numberOfGuests;
        this.numberOfChildren = numberOfChildren;
        this.numberOfHighChairs = numberOfHighChairs;
        this.reservationId = reservationId;
    }

    public ReservationItem(LocalDateTime reservationTime, int numberOfGuests, String customerFirstName) {
        this.reservationTime = reservationTime;
        this.numberOfGuests = numberOfGuests;
        this.customerFirstName = customerFirstName;
    }

    public ReservationItem(String customerFirstName, String customerLastName, String customerEmail, String customerPhone, LocalDateTime reservationTime, int numberOfGuests, int numberOfChildren, int numberOfHighChairs) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.reservationTime = reservationTime;
        this.numberOfGuests = numberOfGuests;
        this.numberOfChildren = numberOfChildren;
        this.numberOfHighChairs = numberOfHighChairs;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public int getReservationId() {
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

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }
}
