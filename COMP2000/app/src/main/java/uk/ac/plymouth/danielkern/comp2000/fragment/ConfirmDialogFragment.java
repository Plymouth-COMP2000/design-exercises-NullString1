package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.stream.Stream;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.api.VolleySingleton;
import uk.ac.plymouth.danielkern.comp2000.data.Account;

public class ConfirmDialogFragment extends DialogFragment {
    private Account account;
    private final ConfirmDeleteListener listener;

    public ConfirmDialogFragment(Account account, ConfirmDeleteListener listener) {
        this.listener = listener;
        this.account = account;
    }

    public interface ConfirmDeleteListener {
        void onConfirmDelete(String username);
    }

    public static ConfirmDialogFragment newInstance(Account account, ConfirmDeleteListener listener) {
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment(account, listener);
        Bundle args = new Bundle();
        args.putParcelable("account", account);
        confirmDialogFragment.setArguments(args);
        return confirmDialogFragment;
    }

    @Override
    public void onAttach(@NonNull android.content.Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            account = getArguments().getParcelable("account", Account.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_delete_staff_member, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (account == null) {
            dismiss();
            return view;
        }
        ((TextView)view.findViewById(R.id.userNameTV)).setText(account.getName());
        Context context = getContext().getApplicationContext();
        view.findViewById(R.id.confirmB).setOnClickListener(v -> {
            VolleySingleton.deleteUser(VolleySingleton.getInstance(getContext()), account.getUsername(), jsonObject -> {
                String message = jsonObject.optString("message", "");
                if (message.equals("User deleted successfully")) {
                    Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
                }
                listener.onConfirmDelete(account.getUsername());
            }, volleyError -> {
                Toast.makeText(context, "Error deleting user", Toast.LENGTH_SHORT).show();
            });
            dismiss();
        });
        view.findViewById(R.id.cancelB).setOnClickListener(v -> dismiss());
        return view;
    }
}
