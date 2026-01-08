package uk.ac.plymouth.danielkern.comp2000.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.api.VolleySingleton;

public class EditMyProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_my_profile, container, false);

        TextInputEditText confirmPasswordET = view.findViewById(R.id.confirmPassET);
        TextInputEditText newPasswordET = view.findViewById(R.id.newPassET);
        Button changePasswordB = view.findViewById(R.id.changePassB);
        changePasswordB.setOnClickListener(v -> {
            String confirmPassword = confirmPasswordET.getText() != null ? confirmPasswordET.getText().toString() : "";
            String newPassword = newPasswordET.getText() != null ? newPasswordET.getText().toString() : "";
            if (confirmPassword.isEmpty() || newPassword.isEmpty()){
                Toast.makeText(requireContext(), "Please enter both fields", Toast.LENGTH_SHORT).show();
                return;
            }
            String username = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE).getString("logged_in_user", "");
            VolleySingleton.setUserPassword(VolleySingleton.getInstance(requireContext()),  username, confirmPassword, newPassword, response -> {
                if (response.optString("message").equals("User updated successfully")){
                    Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            }, error -> Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show());
        });

        Button saveChangesB = view.findViewById(R.id.saveB);
        TextInputEditText firstNameET = view.findViewById(R.id.fNameET);
        TextInputEditText lastNameET = view.findViewById(R.id.sNameET);
        TextInputEditText emailET = view.findViewById(R.id.emailETProf);
        TextInputEditText phoneET = view.findViewById(R.id.phoneETProf);

        VolleySingleton.getUser(VolleySingleton.getInstance(requireContext()), requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE).getString("logged_in_user", null), response -> {
            JSONObject user = response.optJSONObject("user");
            if (user == null){
                Toast.makeText(requireContext(), "Network request failed", Toast.LENGTH_SHORT).show();
                return;
            }
            String firstName = user.optString("firstname");
            String lastName = user.optString("lastname");
            String email = user.optString("email");
            String phone = user.optString("contact");

            firstNameET.setText(firstName);
            lastNameET.setText(lastName);
            emailET.setText(email);
            phoneET.setText(phone);
        }, error -> Toast.makeText(requireContext(), "Network request failed", Toast.LENGTH_SHORT).show());

        saveChangesB.setOnClickListener(v -> {
            String firstName = firstNameET.getText() != null ? firstNameET.getText().toString() : "";
            String lastName = lastNameET.getText() != null ? lastNameET.getText().toString() : "";
            String email = emailET.getText() != null ? emailET.getText().toString() : "";
            String phone = phoneET.getText() != null ? phoneET.getText().toString() : "";

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String username = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE).getString("logged_in_user", "");
            JSONObject user = new JSONObject();
            try {
                user.put("firstname", firstName);
                user.put("lastname", lastName);
                user.put("email", email);
                user.put("contact", phone);
            } catch (Exception e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            VolleySingleton.updateUser(VolleySingleton.getInstance(requireContext()), username, user, response -> {
                if (response.optString("message").equals("User updated successfully")){
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            }, error -> Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show());
        });



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
