package uk.ac.plymouth.danielkern.comp2000.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable divider;
    private final int insetLeft;

    public DividerItemDecoration(@NonNull Context context, int drawableResId) {
        this(context, drawableResId, 0);
    }

    public DividerItemDecoration(@NonNull Context context, int drawableResId, int insetLeftPx) {
        this.divider = ContextCompat.getDrawable(context, drawableResId);
        this.insetLeft = insetLeftPx;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (divider == null) return;
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) return;
        int itemCount = adapter.getItemCount();

        int left = parent.getPaddingLeft() + insetLeft;
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(child);
            if (pos == -1) continue;
            if (pos >= itemCount - 1) continue;

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + (divider.getIntrinsicHeight() > 0 ? divider.getIntrinsicHeight() : 1);
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
