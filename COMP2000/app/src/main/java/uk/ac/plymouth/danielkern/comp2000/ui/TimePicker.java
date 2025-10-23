package uk.ac.plymouth.danielkern.comp2000.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.time.LocalDateTime;

import uk.ac.plymouth.danielkern.comp2000.R;

public class TimePicker extends LinearLayout {
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;

    public TimePicker(Context context) {
        super(context);
        init(context);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.time_picker, this, true);

        hourPicker = findViewById(R.id.hourPicker);
        minutePicker = findViewById(R.id.minutePicker);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(3);
        minutePicker.setDisplayedValues(new String[]{"00", "15", "30", "45"});
    }

    public LocalDateTime getTime() {
        int hour = hourPicker.getValue();
        int minute = minutePicker.getValue() * 15; // Convert picker value to actual minutes
        LocalDateTime now = LocalDateTime.now();
        return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), hour, minute);
    }

    public void setTime(LocalDateTime time) {
        hourPicker.setValue(time.getHour());
        minutePicker.setValue(time.getMinute() / 15); // Convert actual minutes to picker value
    }
}