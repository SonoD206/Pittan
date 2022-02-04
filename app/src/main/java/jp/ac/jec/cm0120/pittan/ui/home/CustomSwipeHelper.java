package jp.ac.jec.cm0120.pittan.ui.home;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import jp.ac.jec.cm0120.pittan.R;

public class CustomSwipeHelper extends ItemTouchHelper.SimpleCallback {

  private final CustomRecyclerAdapter mAdapter;
  private final Drawable icon;
  private final ColorDrawable swipeBackGround;

  CustomSwipeHelper(CustomRecyclerAdapter mAdapter) {
    super(0, ItemTouchHelper.LEFT);
    this.mAdapter = mAdapter;
    this.icon = ContextCompat.getDrawable(mAdapter.getContext(), R.drawable.icon_delete);
    this.swipeBackGround = new ColorDrawable(Color.RED);
  }

  @Override
  public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
    return false;
  }

  @Override
  public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    int position = viewHolder.getAdapterPosition();
    mAdapter.deleteItem(position);
  }

  @Override
  public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    View itemView = viewHolder.itemView;
    int backgroundCornerOffset = 28;

    int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 4;
    int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
    int iconBottom = iconTop + icon.getIntrinsicHeight();

    if (dX < 0) { // Swiping to the left

      int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
      int iconRight = itemView.getRight() - iconMargin;
      icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
      swipeBackGround.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
              itemView.getTop() + 24, itemView.getRight(), itemView.getBottom() - 24);

    } else { // view is unSwiped
      swipeBackGround.setBounds(0, 0, 0, 0);
    }

    swipeBackGround.draw(c);
    icon.draw(c);
  }
}