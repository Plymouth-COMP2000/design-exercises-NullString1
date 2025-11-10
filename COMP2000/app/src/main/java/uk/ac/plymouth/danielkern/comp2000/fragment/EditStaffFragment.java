package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

public class EditStaffFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_staff, container, false);
        if (getArguments() == null) {
            return view;
        }
        TextInputEditText staffUsername = view.findViewById(R.id.usernameET);
        TextInputEditText staffPassword = view.findViewById(R.id.passwordET);
        TextInputEditText staffEmail = view.findViewById(R.id.emailET);
        TextInputEditText staffPhone = view.findViewById(R.id.phoneET);
        TextInputEditText staffFirstName = view.findViewById(R.id.fnameET);
        TextInputEditText staffLastName = view.findViewById(R.id.snameET);
        CheckBox managerCB = view.findViewById(R.id.managerCheck);

        Account account = getArguments().getParcelable("account", Account.class);
        if (account == null) {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
            return view;
        }
        staffUsername.setText(account.getUsername());
        staffPassword.setText(account.getPassword());
        staffEmail.setText(account.getEmail());
        staffPhone.setText(account.getContact());
        staffFirstName.setText(account.getFirstname());
        staffLastName.setText(account.getLastname());
        managerCB.setChecked(account.getUsertype() == Account.UserType.MANAGER);

        Button saveButton = view.findViewById(R.id.saveChangesB);
        saveButton.setOnClickListener(l -> {
            account.setUsername(staffUsername.getText() != null ? staffUsername.getText().toString() : account.getUsername());
            account.setPassword(staffPassword.getText() != null ? staffPassword.getText().toString() : account.getPassword());
            account.setEmail(staffEmail.getText() != null ? staffEmail.getText().toString() : account.getEmail());
            account.setContact(staffPhone.getText() != null ? staffPhone.getText().toString() : account.getContact());
            account.setFirstname(staffFirstName.getText() != null ? staffFirstName.getText().toString() : account.getFirstname());
            account.setLastname(staffLastName.getText() != null ? staffLastName.getText().toString() : account.getLastname());
            account.setUsertype(managerCB.isChecked() ? Account.UserType.MANAGER : Account.UserType.STAFF);
            try {
                VolleySingleton.updateUser(VolleySingleton.getInstance(requireContext()), account.getUsername(), new JSONObject(new Gson().toJson(account)), jsonObject -> {
                    if (jsonObject.optString("message").equals("User updated successfully")){
                        Toast.makeText(requireContext(), "Changes saved", Toast.LENGTH_SHORT).show();
                        requireActivity().getOnBackPressedDispatcher().onBackPressed();
                    }
                }, error -> {});
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        return view;
    }
}