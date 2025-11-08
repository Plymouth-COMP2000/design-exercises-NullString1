package uk.ac.plymouth.danielkern.comp2000.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationItem;

public class StaffReservationsAdapter extends RecyclerView.Adapter<StaffReservationsAdapter.ViewHolder> {

    private ReservationItem[] reservationItems;

    public StaffReservationsAdapter(ReservationItem[] reservationItems){
        this.reservationItems = reservationItems;
    }

    public void updateReservations(ReservationItem[] newReservations) {
        this.reservationItems = newReservations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_reservation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReservationItem item = reservationItems[position];
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("hh.mm a", Locale.getDefault());
        String formattedDate = item.getReservationTime().format(fmt);
        holder.reservationTime.setText(formattedDate);
        holder.reservationName.setText(String.format(Locale.getDefault(), "%s %S", item.getCustomerFirstName(), item.getCustomerLastName()));

        holder.reservationPeople.setText(String.format(Locale.getDefault(), "%d", item.getNumberOfGuests()));

        holder.layout.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("reservationId", item.getReservationId());
            if (Navigation.findNavController(v).getCurrentDestination().getId() == R.id.staffAllResFragment) {
                Navigation.findNavController(v).navigate(R.id.action_staffAllResFragment_to_guestEditReservationFragment, args);
            } else if (Navigation.findNavController(v).getCurrentDestination().getId() == R.id.staffTodayResFragment) {
                Navigation.findNavController(v).navigate(R.id.action_staffTodayResFragment_to_guestEditReservationFragment, args);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationItems.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView reservationName;
        final TextView reservationTime;
        final TextView reservationPeople;

        final ConstraintLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);

            reservationName = itemView.findViewById(R.id.reservationName);
            reservationTime = itemView.findViewById(R.id.reservationTime);
            reservationPeople = itemView.findViewById(R.id.reservationPeople);
            layout = (ConstraintLayout) reservationName.getParent();
        }
    }
}
