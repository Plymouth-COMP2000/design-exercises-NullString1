package uk.ac.plymouth.danielkern.comp2000.fragment;

import static android.content.Context.MODE_PRIVATE;

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
        editProfileB.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_preferencesFragment_to_editMyProfileFragment));

        Button logoutB = view.findViewById(R.id.logoutB);
        logoutB.setOnClickListener(v -> {
            requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE).edit().remove("logged_in_user").remove("user_type").apply();
            Navigation.findNavController(view).navigate(R.id.action_preferencesFragment_to_loginFragment);
        });
    }
}
