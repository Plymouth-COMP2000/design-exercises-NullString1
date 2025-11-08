package uk.ac.plymouth.danielkern.comp2000.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.plymouth.danielkern.comp2000.R;

public class HorizontalDateAdapter extends RecyclerView.Adapter<HorizontalDateAdapter.ViewHolder> {

    public int getTodayPosition() {
        return todayPosition;
    }

    public void setTodayPosition(int todayPosition) {
        this.todayPosition = todayPosition;
    }

    public interface OnDateSelectedListener {
        void onDateSelected(int day);
    }

    Integer[] days;
    private int selectedPosition = -1;
    private int todayPosition = -1;
    private OnDateSelectedListener listener;

    int selectedColour, todayColour, defaultColour;
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
        selectedColour = parent.getResources().getColor(R.color.success, parent.getContext().getTheme());
        todayColour = parent.getResources().getColor(R.color.primary, parent.getContext().getTheme());
        defaultColour = parent.getResources().getColor(R.color.grey, parent.getContext().getTheme());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Integer day = days[position];
        holder.dayText.setText(String.valueOf(day));
        int colour;
        if (position == selectedPosition) {
            colour = selectedColour;
        } else if (position == todayPosition) {
            colour = todayColour;
        } else {
            colour = defaultColour;
        }
        holder.setHighlighted(position == selectedPosition || position == todayPosition, colour);
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
        final LinearLayout dayB;
        public ViewHolder(View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayTV);
            dayB = itemView.findViewById(R.id.dayB);
        }

        public void setHighlighted(boolean highlighted, int colour) {
            Drawable bg = dayB.getBackground();
            bg.setTint(colour);
            dayB.setBackground(bg);
        }
    }
}
