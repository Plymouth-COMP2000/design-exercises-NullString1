package uk.ac.plymouth.danielkern.comp2000.fragment;

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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.api.VolleySingleton;
import uk.ac.plymouth.danielkern.comp2000.data.Account;

public class RegisterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button registerB = view.findViewById(R.id.registerButton);
        EditText usernameET = view.findViewById(R.id.usernameET);
        EditText passwordET = view.findViewById(R.id.passwordET);
        EditText firstNameET = view.findViewById(R.id.fnameET);
        EditText lastNameET = view.findViewById(R.id.snameET);
        EditText emailET = view.findViewById(R.id.emailET);
        EditText phoneET = view.findViewById(R.id.phoneET);
        registerB.setOnClickListener(v -> {
            Account account = new Account(
                    usernameET.getText().toString(),
                    passwordET.getText().toString(),
                    firstNameET.getText().toString(),
                    lastNameET.getText().toString(),
                    emailET.getText().toString(),
                    phoneET.getText().toString()
            );
            JSONObject user;
            try {
                user = new JSONObject(new Gson().toJson(account));
            } catch (JSONException e) {
                Log.e("RegisterFragment", "onViewCreated: ", e);
                Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_SHORT).show();
                return;
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://10.240.72.69/comp2000/coursework/create_user/10944460", user,
                    response -> {
                        if (response.optString("message").equals("User created successfully")) {
                            Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                            requireContext().getSharedPreferences("user_prefs", 0).edit().putString("logged_in_user", account.getUsername()).putString("user_type", Account.UserType.GUEST.toString()).apply();
                            Navigation.findNavController(view).navigate(R.id.action_register_to_login);
                        } else {
                            Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                            Toast.makeText(requireContext(), "Username already taken", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.e("LoginFragment", "onViewCreated: ", error);
                        Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_SHORT).show();
                    });
            RequestQueue queue = VolleySingleton.getInstance(requireContext()).getRequestQueue();
            queue.add(request);
        });
    }
}
