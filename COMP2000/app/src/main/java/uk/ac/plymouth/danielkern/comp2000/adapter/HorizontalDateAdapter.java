package uk.ac.plymouth.danielkern.comp2000.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.plymouth.danielkern.comp2000.R;

public class HorizontalDateAdapter extends RecyclerView.Adapter<HorizontalDateAdapter.ViewHolder> {

    public interface OnDateSelectedListener {
        void onDateSelected(int day);
    }

    Integer[] days;
    private int selectedPosition = -1;
    private OnDateSelectedListener listener;

    public HorizontalDateAdapter(Integer[] days){
        this.days = days;
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.listener = listener;
    }

    public void setSelectedPosition(int position) {
        if (position == selectedPosition) return;
        int oldPosition = selectedPosition;
        selectedPosition = position;
        if (oldPosition >= 0) {
            notifyItemChanged(oldPosition);
        }
        if (position >= 0) {
            notifyItemChanged(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_date_picker_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Integer day = days[position];
        holder.dayText.setText(String.valueOf(day));
        holder.setHighlighted(position == selectedPosition);
        holder.itemView.setOnClickListener(v -> {
            setSelectedPosition(position);
            if (listener != null) {
                listener.onDateSelected(day);
            }
        });
    }

    @Override
    public int getItemCount() {
        return days.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView dayText;
        public ViewHolder(View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayTV);
        }

        public void setHighlighted(boolean highlighted) {
            if (highlighted) {
                dayText.setBackgroundColor(Color.BLUE);
            } else {
                dayText.setBackgroundColor(Color.WHITE);
            }
        }
    }
}
