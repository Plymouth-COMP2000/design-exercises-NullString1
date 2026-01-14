package uk.ac.plymouth.danielkern.comp2000.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import uk.ac.plymouth.danielkern.comp2000.R;

public class GuestsPicker extends LinearLayout {

    private EditText totalGuestsI;
    private EditText childrenI;
    private EditText highChairsI;

    public GuestsPicker(Context context) {
        super(context);
        init(context);
    }

    public GuestsPicker(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public GuestsPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.guests_picker, this, true);

        totalGuestsI = findViewById(R.id.totalGuestsI);
        childrenI = findViewById(R.id.childrenI);
        highChairsI = findViewById(R.id.highChairsI);

        totalGuestsI.setText("1");
        childrenI.setText("0");
        highChairsI.setText("0");
    }

    public int getGuests() {
        return Integer.parseInt(totalGuestsI.getText().toString());
    }

    public int getChildren() {
        return Integer.parseInt(childrenI.getText().toString());
    }

    public int getHighChairs() {
        return Integer.parseInt(highChairsI.getText().toString());
    }

    public void setGuests(int guests) {
        totalGuestsI.setText(String.valueOf(guests));
    }

    public void setChildren(int children) {
        childrenI.setText(String.valueOf(children));
    }

    public void setHighChairs(int highChairs) {
        highChairsI.setText(String.valueOf(highChairs));
    }
}