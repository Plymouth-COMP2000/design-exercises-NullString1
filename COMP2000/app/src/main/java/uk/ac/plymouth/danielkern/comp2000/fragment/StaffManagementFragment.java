package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.Arrays;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.adapter.StaffAdapter;
import uk.ac.plymouth.danielkern.comp2000.api.VolleySingleton;
import uk.ac.plymouth.danielkern.comp2000.data.Account;

public class StaffManagementFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_management, container, false);
        RecyclerView staffRecyclerView = view.findViewById(R.id.staffRecycler);

        staffRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        StaffAdapter staffAdapter = new StaffAdapter(new Account[]{});
        staffRecyclerView.setAdapter(staffAdapter);

        VolleySingleton.getAllUsers(VolleySingleton.getInstance(requireContext()), jsonObject -> {
            JSONArray users = jsonObject.optJSONArray("users");
            if (users == null)
                return;
            Account[] accounts = Arrays.stream(new Gson().fromJson(users.toString(), Account[].class)).filter(account -> !account.getUsertype().toString().equals("GUEST")).toArray(Account[]::new);
            staffAdapter.setStaffAccounts(accounts);
        }, volleyError -> {
            Toast.makeText(requireContext(), "Error fetching staff", Toast.LENGTH_SHORT).show();
        });
        return view;
    }
}