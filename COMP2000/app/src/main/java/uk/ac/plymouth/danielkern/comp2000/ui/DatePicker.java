package uk.ac.plymouth.danielkern.comp2000.ui;

import android.content.Context;
import android.icu.util.Calendar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.time.LocalDate;

import uk.ac.plymouth.danielkern.comp2000.R;

public class DatePicker extends LinearLayout {

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

        NumberPicker dayPicker = findViewById(R.id.hourPicker);
        NumberPicker monthPicker = findViewById(R.id.ampmPicker);
        NumberPicker yearPicker = findViewById(R.id.yearPicker);

        Calendar today = Calendar.getInstance();
        dayPicker.setMinValue(today.get(Calendar.DATE));
        dayPicker.setMaxValue(today.getActualMaximum(Calendar.DATE));

        monthPicker.setMinValue(today.get(Calendar.MONTH)+1);
        monthPicker.setMaxValue(12);

        yearPicker.setMinValue(today.get(Calendar.YEAR));
        yearPicker.setMaxValue(today.get(Calendar.YEAR) + 2);
    }

    public LocalDate getDate() {
        NumberPicker dayPicker = findViewById(R.id.hourPicker);
        NumberPicker monthPicker = findViewById(R.id.ampmPicker);
        NumberPicker yearPicker = findViewById(R.id.yearPicker);

        return LocalDate.of(yearPicker.getValue(), monthPicker.getValue(), dayPicker.getValue());
    }



}