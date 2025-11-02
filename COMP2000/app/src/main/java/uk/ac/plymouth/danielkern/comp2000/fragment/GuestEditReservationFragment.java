package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationItem;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationsSingleton;
import uk.ac.plymouth.danielkern.comp2000.ui.GuestsPicker;
import uk.ac.plymouth.danielkern.comp2000.ui.TimePicker;

public class GuestEditReservationFragment extends Fragment {

    final ReservationsSingleton reservationsSingleton = ReservationsSingleton.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guest_edit_reservation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String reservationId = requireArguments().getString("reservationUUID");
        ReservationItem reservationItem = reservationsSingleton.getReservationById(reservationId);

        TimePicker timePicker = view.findViewById(R.id.timePicker);
        timePicker.setTime(reservationItem.getReservationTime());
        GuestsPicker guestsPicker = view.findViewById(R.id.guestsPicker);
        guestsPicker.setGuests(reservationItem.getNumberOfGuests());
        guestsPicker.setChildren(reservationItem.getNumberOfChildren());
        guestsPicker.setHighChairs(reservationItem.getNumberOfHighChairs());
    }
}
