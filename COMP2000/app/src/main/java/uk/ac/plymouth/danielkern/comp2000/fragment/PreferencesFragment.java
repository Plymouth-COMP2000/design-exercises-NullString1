package uk.ac.plymouth.danielkern.comp2000.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.json.JSONObject;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.api.VolleySingleton;

public class PreferencesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preferences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button editProfileB = view.findViewById(R.id.editProfB);
        EditText nameView = view.findViewById(R.id.nameView);
        EditText emailView = view.findViewById(R.id.emailView);
        EditText phoneView = view.findViewById(R.id.phoneView);
        editProfileB.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_preferencesFragment_to_editMyProfileFragment));

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

            nameView.setText(String.format("%s %s", firstName, lastName));
            emailView.setText(email);
            phoneView.setText(phone);
        }, error -> {
            Toast.makeText(requireContext(), "Network request failed", Toast.LENGTH_SHORT).show();
        });

        Button logoutB = view.findViewById(R.id.logoutB);
        logoutB.setOnClickListener(v -> {
            requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE).edit().remove("logged_in_user").remove("user_type").apply();
            Navigation.findNavController(view).navigate(R.id.action_preferencesFragment_to_loginFragment);
        });
    }
}
