package uk.ac.plymouth.danielkern.comp2000.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import uk.ac.plymouth.danielkern.comp2000.R;

public class HorizontalDatePickerItem extends LinearLayout {

    TextView dayText;

    public HorizontalDatePickerItem(Context context) {
        super(context);
        init(context);
    }

    public HorizontalDatePickerItem(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public HorizontalDatePickerItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.horizontal_date_picker_item, this, true);

        dayText = findViewById(R.id.dayTV);
    }

    public void setDay(int day) {
        dayText.setText(String.valueOf(day));
    }



}