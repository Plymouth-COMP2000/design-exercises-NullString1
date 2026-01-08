package uk.ac.plymouth.danielkern.comp2000.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.stream.Stream;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.data.Account;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    private Account[] staffAccounts;
    private final onClickListener listener;

    public interface onClickListener {
        void onClick(Account account);
    }

    public StaffAdapter(Account[] staffAccounts, onClickListener listener){
        this.staffAccounts = staffAccounts;
        this.listener = listener;
    }

    public void setStaffAccounts(Account[] staffAccounts) {
        this.staffAccounts = staffAccounts;
        notifyDataSetChanged();
    }

    public void deleteAccount(String username) {
        int index = -1;
        for (int i = 0; i < staffAccounts.length; i++) {
            if (staffAccounts[i].getUsername().equals(username)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            staffAccounts = Stream.of(staffAccounts).filter(a -> !a.getUsername().equals(username)).toArray(Account[]::new);
            notifyItemRemoved(index);
        }
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
        holder.deleteStaffB.setOnClickListener(v -> {
            listener.onClick(account);
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
