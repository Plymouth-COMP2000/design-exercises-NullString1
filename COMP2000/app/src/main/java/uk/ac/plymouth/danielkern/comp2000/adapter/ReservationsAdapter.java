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

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ViewHolder> {

    private final ReservationItem[] reservationItems;
    public ReservationsAdapter(ReservationItem[] reservationItems){
        this.reservationItems = reservationItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReservationItem item = reservationItems[position];
        if (item == null) return;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MM : hh.mm a", Locale.getDefault());
        String[] formattedDate = item.getReservationTime().format(fmt).split(":");
        holder.reservationDate.setText(formattedDate[0]);
        holder.reservationTime.setText(formattedDate[1]);
        holder.reservationPeople.setText(String.format(Locale.getDefault(), "%d", item.getNumberOfGuests()));

        holder.layout.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("reservationId", item.getReservationId());
            Navigation.findNavController(v).navigate(R.id.action_reservations_to_edit, args);
        });
    }

    @Override
    public int getItemCount() {
        return reservationItems.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView reservationDate;
        final TextView reservationTime;
        final TextView reservationPeople;

        final ConstraintLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);

            reservationDate = itemView.findViewById(R.id.reservationTime);
            reservationTime = itemView.findViewById(R.id.reservationName);
            reservationPeople = itemView.findViewById(R.id.reservationPeople);
            layout = (ConstraintLayout) reservationDate.getParent();
        }
    }
}
