package uk.ac.plymouth.danielkern.comp2000.adapter;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.stream.Stream;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.api.VolleySingleton;
import uk.ac.plymouth.danielkern.comp2000.data.Account;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    private Account[] staffAccounts;
    public StaffAdapter(Account[] staffAccounts){
        this.staffAccounts = staffAccounts;
    }

    public void setStaffAccounts(Account[] staffAccounts) {
        this.staffAccounts = staffAccounts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account account = staffAccounts[position];
        if (account == null) return;
        holder.staffName.setText(account.getName());
        holder.staffType.setText(account.getUsertype().toString());
        holder.deleteStaffB.setOnClickListener(l -> {
            Dialog confirmDeleteDialog = new Dialog(holder.itemView.getContext());
            confirmDeleteDialog.setContentView(R.layout.dialog_confirm_delete_staff_member);
            confirmDeleteDialog.findViewById(R.id.confirmB).setOnClickListener(v -> {
                VolleySingleton.deleteUser(VolleySingleton.getInstance(holder.itemView.getContext()), account.getUsername(), jsonObject -> {
                    String message = jsonObject.optString("message", "");
                    if (message.equals("User deleted successfully")) {
                        Toast.makeText(holder.itemView.getContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                    staffAccounts =  Stream.of(staffAccounts).filter(a -> !a.getUsername().equals(account.getUsername())).toArray(Account[]::new);
                    notifyDataSetChanged();
                }, volleyError -> {
                    Toast.makeText(holder.itemView.getContext(), "Error deleting user", Toast.LENGTH_SHORT).show();
                });
                confirmDeleteDialog.dismiss();
            });
            confirmDeleteDialog.findViewById(R.id.cancelB).setOnClickListener(v -> confirmDeleteDialog.dismiss());
            confirmDeleteDialog.show();
        });

        holder.layout.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putParcelable("account", account);
            Navigation.findNavController(v).navigate(R.id.action_staffManagementFragment_to_editStaffFragment, args);
        });
    }

    @Override
    public int getItemCount() {
        return staffAccounts.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView staffName;
        final TextView staffType;
        final ImageButton deleteStaffB;

        final ConstraintLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            staffName = itemView.findViewById(R.id.staffName);
            staffType = itemView.findViewById(R.id.staffType);
            deleteStaffB = itemView.findViewById(R.id.deleteStaffB);
            layout = (ConstraintLayout) staffType.getParent();
        }
    }
}
