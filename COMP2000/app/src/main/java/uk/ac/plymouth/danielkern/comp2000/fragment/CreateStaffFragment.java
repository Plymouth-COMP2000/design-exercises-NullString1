package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.api.VolleySingleton;
import uk.ac.plymouth.danielkern.comp2000.data.Account;

public class CreateStaffFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_staff, container, false);
        TextInputEditText staffUsername = view.findViewById(R.id.usernameET2);
        TextInputEditText staffPassword = view.findViewById(R.id.passwordET2);
        TextInputEditText staffEmail = view.findViewById(R.id.emailET2);
        TextInputEditText staffPhone = view.findViewById(R.id.phoneET2);
        TextInputEditText staffFirstName = view.findViewById(R.id.fnameET2);
        TextInputEditText staffLastName = view.findViewById(R.id.snameET2);
        CheckBox managerCB = view.findViewById(R.id.managerCheck2);
        Button saveButton = view.findViewById(R.id.saveChangesB2);

        saveButton.setOnClickListener(l -> {
            Account account = new Account();
            account.setUsername(staffUsername.getText() != null ? staffUsername.getText().toString() : account.getUsername());
            account.setPassword(staffPassword.getText() != null ? staffPassword.getText().toString() : account.getPassword());
            account.setEmail(staffEmail.getText() != null ? staffEmail.getText().toString() : account.getEmail());
            account.setContact(staffPhone.getText() != null ? staffPhone.getText().toString() : account.getContact());
            account.setFirstname(staffFirstName.getText() != null ? staffFirstName.getText().toString() : account.getFirstname());
            account.setLastname(staffLastName.getText() != null ? staffLastName.getText().toString() : account.getLastname());
            account.setUsertype(managerCB.isChecked() ? Account.UserType.MANAGER : Account.UserType.STAFF);
            try {
                VolleySingleton.createUser(VolleySingleton.getInstance(requireContext()), new JSONObject(new Gson().toJson(account)),response -> {
                    if (response.optString("message").equals("User created successfully")) {
                        Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                        requireActivity().getOnBackPressedDispatcher().onBackPressed();
                    } else {
                        Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                        Toast.makeText(requireContext(), "Username already taken", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.e("LoginFragment", "onViewCreated: ", error);
                    Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_SHORT).show();
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        return view;
    }
}