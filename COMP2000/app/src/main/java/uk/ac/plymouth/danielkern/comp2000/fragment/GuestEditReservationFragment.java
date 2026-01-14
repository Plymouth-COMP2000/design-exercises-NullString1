package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationItem;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationsDatabaseSingleton;
import uk.ac.plymouth.danielkern.comp2000.ui.GuestsPicker;
import uk.ac.plymouth.danielkern.comp2000.ui.TimePicker;

public class GuestEditReservationFragment extends Fragment {

    ReservationsDatabaseSingleton resDb;
    GuestsPicker guestsPicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resDb = ReservationsDatabaseSingleton.getInstance(requireContext());
        return inflater.inflate(R.layout.fragment_guest_edit_reservation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int reservationId = requireArguments().getInt("reservationId");
        ReservationItem reservationItem = resDb.db.getReservationById(reservationId);
        TimePicker timePicker = view.findViewById(R.id.timePicker);
        Button saveButton = view.findViewById(R.id.submitB);
        saveButton.setOnClickListener(v -> {
            reservationItem.setNumberOfGuests(guestsPicker.getGuests());
            reservationItem.setNumberOfChildren(guestsPicker.getChildren());
            reservationItem.setNumberOfHighChairs(guestsPicker.getHighChairs());
            reservationItem.setReservationTime(reservationItem.getReservationTime().toLocalDate().atTime(timePicker.getTime().toLocalTime()));
            resDb.db.updateReservation(reservationItem);
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
        Button deleteButton = view.findViewById(R.id.delB);
        deleteButton.setOnClickListener(v -> {
            resDb.db.deleteReservation(reservationItem.getReservationId());
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
        timePicker.setTime(reservationItem.getReservationTime());
        guestsPicker = view.findViewById(R.id.guestsPicker);
        guestsPicker.setGuests(reservationItem.getNumberOfGuests());
        guestsPicker.setChildren(reservationItem.getNumberOfChildren());
        guestsPicker.setHighChairs(reservationItem.getNumberOfHighChairs());
    }
}
