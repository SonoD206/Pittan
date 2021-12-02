package jp.ac.jec.cm0120.pittan.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import jp.ac.jec.cm0120.pittan.R;

public class CustomSwipeHelper extends ItemTouchHelper.SimpleCallback {

  CustomRecyclerAdapter mAdapter;
  private Paint paint = new Paint();
  private Drawable icon;
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
    String alertMessage = "本当に「リビング」を削除しますか？";
    new MaterialAlertDialogBuilder(mAdapter.getContext())
            .setMessage(alertMessage)
            .setPositiveButton("削除", (dialogInterface, i) -> {
              mAdapter.deleteItem(viewHolder.getAdapterPosition());
            })
            .setNegativeButton("キャンセル", ((dialogInterface, i) -> {
              Toast.makeText(mAdapter.getContext(), "キャンセルしまうま", Toast.LENGTH_SHORT).show();
            })).show();
  }

  @Override
  public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
    super.onChildDraw(c, recyclerView, viewHolder, dX/3, dY, actionState, isCurrentlyActive);

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
              itemView.getTop() + 24, itemView.getRight() - 16, itemView.getBottom() - 24);


    } else { // view is unSwiped
      swipeBackGround.setBounds(0, 0, 0, 0);
    }

    swipeBackGround.draw(c);
    icon.draw(c);
  }
}
