package uk.ac.plymouth.danielkern.comp2000.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.time.LocalDate;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.adapter.HorizontalDateAdapter;

public class HorizontalDatePicker extends LinearLayout {

    private HorizontalDateAdapter adapter;

    public HorizontalDatePicker(Context context) {
        super(context);
        init(context);
    }

    public HorizontalDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public HorizontalDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.horizontal_date_picker, this, true);
        RecyclerView container = findViewById(R.id.dayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        container.setLayoutManager(layoutManager);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(container);

        int numDays = LocalDate.now().lengthOfMonth();
        Integer[] days = new Integer[numDays];
        for (int i = 0; i < numDays; i++) {
            days[i] = i + 1;
        }
        adapter = new HorizontalDateAdapter(days);
        container.setAdapter(adapter);
        int currentItem = LocalDate.now().getDayOfMonth() - 1;

        container.post(() -> {
            layoutManager.scrollToPositionWithOffset(currentItem,
                container.getWidth() / 2 - container.getWidth() / (2 * Math.max(1, numDays)));
            adapter.setSelectedPosition(currentItem);
        });
    }

    public void setOnDateSelectedListener(HorizontalDateAdapter.OnDateSelectedListener listener) {
        if (adapter != null) {
            adapter.setOnDateSelectedListener(listener);
        }
    }
}