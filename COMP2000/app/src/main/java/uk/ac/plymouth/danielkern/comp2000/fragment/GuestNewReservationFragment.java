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

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationItem;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationsDatabaseSingleton;
import uk.ac.plymouth.danielkern.comp2000.ui.GuestsPicker;
import uk.ac.plymouth.danielkern.comp2000.ui.TimePicker;

public class GuestNewReservationFragment extends Fragment {

    ReservationsDatabaseSingleton resDb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resDb = ReservationsDatabaseSingleton.getInstance(getContext());
        return inflater.inflate(R.layout.fragment_guest_new_reservation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TimePicker timePicker = view.findViewById(R.id.timePicker);
        GuestsPicker guestsPicker = view.findViewById(R.id.guestPicker);
        Button createReservationButton = view.findViewById(R.id.submitB2);

        createReservationButton.setOnClickListener(v -> {
            resDb.db.addReservation(new ReservationItem(timePicker.getTime(), guestsPicker.getGuests(), guestsPicker.getChildren(), guestsPicker.getHighChairs()));
            Navigation.findNavController(view).navigate(R.id.action_guestNewReservationFragment_to_guestReservationsFragment);
        });
    }
}
