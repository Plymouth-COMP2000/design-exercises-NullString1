package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationItem;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationsDatabaseSingleton;
import uk.ac.plymouth.danielkern.comp2000.ui.GuestsPicker;
import uk.ac.plymouth.danielkern.comp2000.ui.TimePicker;

public class StaffNewResFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_new_res, container, false);

        Button createResB = view.findViewById(R.id.createResB);
        createResB.setOnClickListener(v -> {
            TextInputEditText guestFNameET = view.findViewById(R.id.guestFirstName);
            TextInputEditText guestSNameET = view.findViewById(R.id.guestLastName);
            TextInputEditText guestEmailET = view.findViewById(R.id.guestEmail);
            TextInputEditText guestPhoneET = view.findViewById(R.id.guestPhone);
            GuestsPicker guestsPicker = view.findViewById(R.id.guestsPicker2);
            TimePicker timePicker = view.findViewById(R.id.timePicker2);
            String guestFName = guestFNameET.getText() != null ? guestFNameET.getText().toString() : "";
            String guestSName = guestSNameET.getText() != null ? guestSNameET.getText().toString() : "";
            String guestEmail = guestEmailET.getText() != null ? guestEmailET.getText().toString() : "";
            String guestPhone = guestPhoneET.getText() != null ? guestPhoneET.getText().toString() : "";
            ReservationItem newRes = new ReservationItem(
                    guestFName,
                    guestSName,
                    guestEmail,
                    guestPhone,
                    timePicker.getTime(),
                    guestsPicker.getGuests(),
                    guestsPicker.getChildren(),
                    guestsPicker.getHighChairs()
            );
            ReservationsDatabaseSingleton.getInstance(requireContext()).db.addReservation(newRes);
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
        return view;
    }
}