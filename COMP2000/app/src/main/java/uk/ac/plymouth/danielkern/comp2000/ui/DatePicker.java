package uk.ac.plymouth.danielkern.comp2000.ui;

import android.content.Context;
import android.icu.util.Calendar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import uk.ac.plymouth.danielkern.comp2000.R;

public class DatePicker extends LinearLayout {
    private NumberPicker dayPicker;
    private NumberPicker monthPicker;
    private NumberPicker yearPicker;

    public DatePicker(Context context) {
        super(context);
        init(context);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.date_picker, this, true);

        dayPicker = findViewById(R.id.hourPicker);
        monthPicker = findViewById(R.id.ampmPicker);
        yearPicker = findViewById(R.id.yearPicker);

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        yearPicker.setMinValue(year);
        yearPicker.setMaxValue(year + 1);
    }



}