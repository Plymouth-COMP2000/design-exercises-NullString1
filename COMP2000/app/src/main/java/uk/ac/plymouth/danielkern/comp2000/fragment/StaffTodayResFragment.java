package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.adapter.StaffReservationsAdapter;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationItem;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationsDatabaseSingleton;

public class StaffTodayResFragment extends Fragment {
    private StaffReservationsAdapter adapter;
    private ReservationsDatabaseSingleton resDb;

    public StaffTodayResFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_today_reservations, container, false);
        resDb = ReservationsDatabaseSingleton.getInstance(requireContext());

        RecyclerView staffTodayResRecyclerView = view.findViewById(R.id.staffResList);
        staffTodayResRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new StaffReservationsAdapter(resDb.db.getReservationsByDate(LocalDate.now()));
        staffTodayResRecyclerView.setAdapter(adapter);

        Button newResB = view.findViewById(R.id.newResB2);
        newResB.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_staffTodayResFragment_to_staffNewResFragment);
        });

        return view;
    }

    private void updateReservations() {
        ReservationItem[] filteredReservations = resDb.db.getReservationsByDate(LocalDate.now());
        adapter.updateReservations(filteredReservations);
    }
}