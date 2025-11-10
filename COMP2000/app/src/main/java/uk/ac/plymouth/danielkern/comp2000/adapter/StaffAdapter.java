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

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.data.Account;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationItem;

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

        holder.layout.setOnClickListener(v -> {
//            Bundle args = new Bundle();
//            args.putInt("reservationId", item.getReservationId());
//            Navigation.findNavController(v).navigate(R.id.action_reservations_to_edit, args);
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
