package com.ant.attendance;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeGestureCallback extends ItemTouchHelper.SimpleCallback {

    private final SwipeActions mSwipeActions;
    private final Context mContext;
    private Drawable rightSwipeIcon;
    private Drawable leftSwipeIcon;
    private ColorDrawable rightSwipeBackground;
    private ColorDrawable leftSwipeBackground;

    public interface SwipeActions {
        void onEdit(int position);
        void onDelete(int position);
    }


    public SwipeGestureCallback(Context context, SwipeActions swipeActions) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mContext = context;
        mSwipeActions = swipeActions;


        rightSwipeIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_edit);
        leftSwipeIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_delete);
        rightSwipeBackground = new ColorDrawable(Color.BLUE);
        leftSwipeBackground = new ColorDrawable(Color.RED);
    }


    public SwipeGestureCallback(Context context, SwipeActions swipeActions, int rightSwipeIconResId, int leftSwipeIconResId, int rightColor, int leftColor) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mContext = context;
        mSwipeActions = swipeActions;

        rightSwipeIcon = ContextCompat.getDrawable(mContext, rightSwipeIconResId);
        leftSwipeIcon = ContextCompat.getDrawable(mContext, leftSwipeIconResId);
        rightSwipeBackground = new ColorDrawable(rightColor);
        leftSwipeBackground = new ColorDrawable(leftColor);
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.RIGHT) {
            mSwipeActions.onEdit(position);
        } else {
            mSwipeActions.onDelete(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        Drawable icon;
        ColorDrawable background;

        if (dX > 0) { // Swiping to the right
            icon = rightSwipeIcon;
            background = rightSwipeBackground;
        } else { // Swiping to the left
            icon = leftSwipeIcon;
            background = leftSwipeBackground;
        }

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) {
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) {
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else {
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }
}
