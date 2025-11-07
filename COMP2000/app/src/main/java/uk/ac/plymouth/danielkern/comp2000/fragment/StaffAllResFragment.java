package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDate;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.adapter.StaffReservationsAdapter;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationItem;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationsDatabaseSingleton;
import uk.ac.plymouth.danielkern.comp2000.ui.HorizontalDatePicker;

public class StaffAllResFragment extends Fragment {

    ReservationsDatabaseSingleton resDb;
    private StaffReservationsAdapter adapter;
    private LocalDate selectedDate;

    public StaffAllResFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_all_reservations, container, false);
        resDb = ReservationsDatabaseSingleton.getInstance(requireContext());
        selectedDate = LocalDate.now();

        RecyclerView staffTodayResRecyclerView = view.findViewById(R.id.staffResList);
        staffTodayResRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new StaffReservationsAdapter(resDb.db.getReservationsByDate(selectedDate));
        staffTodayResRecyclerView.setAdapter(adapter);

        HorizontalDatePicker datePicker = view.findViewById(R.id.horizontalDatePicker);
        datePicker.setOnDateSelectedListener(day -> {
            selectedDate = LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), day);
            updateReservations();
        });

        return view;
    }

    private void updateReservations() {
        ReservationItem[] filteredReservations = resDb.db.getReservationsByDate(selectedDate);
        adapter.updateReservations(filteredReservations);
    }
}