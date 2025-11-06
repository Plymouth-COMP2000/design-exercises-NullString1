package uk.ac.plymouth.danielkern.comp2000.data;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReservationsSingleton {
    private static ReservationsSingleton instance;
    private final ArrayList<ReservationItem> reservationsData;

    private ReservationsSingleton() {
        reservationsData = new ArrayList<>();
    }

    public static synchronized ReservationsSingleton getInstance() {
        if (instance == null) {
            instance = new ReservationsSingleton();
        }
        return instance;
    }

    public ReservationItem[] getReservationsData() {
        return reservationsData.toArray(new ReservationItem[0]);
    }

    public ReservationItem[] getReservationsByDate(LocalDate date) {
        ArrayList<ReservationItem> filteredReservations = new ArrayList<>();
        for (ReservationItem item : reservationsData) {
            if (item.getReservationTime().toLocalDate().equals(date)) {
                filteredReservations.add(item);
            }
        }
        return filteredReservations.toArray(new ReservationItem[0]);
    }

    public ReservationItem getReservationById(String reservationId) {
        for (ReservationItem item : reservationsData) {
            if (item.getReservationId().toString().equals(reservationId)) {
                return item;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return reservationsData.isEmpty();
    }
    public void addReservation(ReservationItem reservation) {
        reservationsData.add(reservation);
    }

}
