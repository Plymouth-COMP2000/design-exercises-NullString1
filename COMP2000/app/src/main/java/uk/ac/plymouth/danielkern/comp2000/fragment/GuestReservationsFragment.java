package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.adapter.ReservationsAdapter;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationsDatabaseSingleton;
import uk.ac.plymouth.danielkern.comp2000.decoration.DividerItemDecoration;

public class GuestReservationsFragment extends Fragment {

    ReservationsDatabaseSingleton resDb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resDb = ReservationsDatabaseSingleton.getInstance(requireContext());
        return inflater.inflate(R.layout.fragment_guest_reservations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button newResB = view.findViewById(R.id.newResB);
        newResB.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_guestRes_to_newRes));

        RecyclerView reservationsRecycler = view.findViewById(R.id.reservationsRecycler);
        reservationsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        reservationsRecycler.setAdapter(new ReservationsAdapter(resDb.db.getReservations()));

        DividerItemDecoration decoration = new DividerItemDecoration(requireContext(), R.drawable.divider);
        reservationsRecycler.addItemDecoration(decoration);
    }
}
