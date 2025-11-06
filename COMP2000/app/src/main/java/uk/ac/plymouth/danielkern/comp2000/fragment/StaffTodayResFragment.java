package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDate;
import java.time.LocalDateTime;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.adapter.StaffReservationsAdapter;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationItem;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationsSingleton;
import uk.ac.plymouth.danielkern.comp2000.ui.HorizontalDatePicker;

public class StaffTodayResFragment extends Fragment {

    ReservationsSingleton reservationsSingleton = ReservationsSingleton.getInstance();
    private StaffReservationsAdapter adapter;
    private LocalDate selectedDate;

    public StaffTodayResFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void testReservations(){
        if (reservationsSingleton.isEmpty()) {
            reservationsSingleton.addReservation(new ReservationItem(LocalDateTime.now().withMinute(30), 5,"Alice"));
            reservationsSingleton.addReservation(new ReservationItem(LocalDateTime.now().withMinute(45), 3, "Bob"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_todays_reservations, container, false);

        RecyclerView staffTodayResRecyclerView = view.findViewById(R.id.staffResList);
        staffTodayResRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        testReservations();

        selectedDate = LocalDate.now();

        adapter = new StaffReservationsAdapter(reservationsSingleton.getReservationsByDate(selectedDate));
        staffTodayResRecyclerView.setAdapter(adapter);

        HorizontalDatePicker datePicker = view.findViewById(R.id.horizontalDatePicker);
        datePicker.setOnDateSelectedListener(day -> {
            selectedDate = LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), day);
            updateReservations();
        });

        return view;
    }

    private void updateReservations() {
        ReservationItem[] filteredReservations = reservationsSingleton.getReservationsByDate(selectedDate);
        adapter.updateReservations(filteredReservations);
    }
}