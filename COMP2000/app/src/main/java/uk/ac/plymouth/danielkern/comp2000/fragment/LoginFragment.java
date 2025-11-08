package uk.ac.plymouth.danielkern.comp2000.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.activity.MainActivity;
import uk.ac.plymouth.danielkern.comp2000.api.VolleySingleton;

public class LoginFragment extends Fragment {

    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences == null) {
            sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
        }
        if (sharedPreferences.contains("logged_in_user")) {
            Navigation.findNavController(requireView()).navigate(R.id.action_login_to_menu);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);

        if (sharedPreferences.contains("logged_in_user")) {
            Navigation.findNavController(view).navigate(R.id.action_login_to_menu);
            return;
        }

        Button loginButton = view.findViewById(R.id.loginB);
        EditText usernameET = view.findViewById(R.id.usernameET);
        EditText passwordET = view.findViewById(R.id.passwordET);
        loginButton.setOnClickListener(v -> {
            String username = usernameET.getText().toString();
            String password = passwordET.getText().toString();
            if (username.isBlank() || password.isBlank()) {
                return;
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, String.format(Locale.getDefault(), "http://10.240.72.69/comp2000/coursework/read_user/10944460/%s", username), null,
                    response -> {
                        try {
                            JSONObject user = response.getJSONObject("user");
                            if (user.optString("password").equals(password)) {
                                sharedPreferences.edit().putString("logged_in_user", username).putString("user_type", user.getString("usertype")).apply();
                                ((MainActivity) requireActivity()).updateNavBarPerUserType();
                                Navigation.findNavController(view).navigate(R.id.action_login_to_menu);
                            } else {
                                Toast.makeText(requireContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    error -> {
                        if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                            Toast.makeText(requireContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.e("LoginFragment", "onViewCreated: ", error);
                        Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_SHORT).show();
                    });
            RequestQueue queue = VolleySingleton.getInstance(requireContext()).getRequestQueue();
            queue.add(request);
        });

        Button registerButton = view.findViewById(R.id.registerB);
        registerButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_login_to_register));
    }
}
