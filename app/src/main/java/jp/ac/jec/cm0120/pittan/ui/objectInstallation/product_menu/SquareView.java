package jp.ac.jec.cm0120.pittan.ui.objectInstallation.product_menu;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.card.MaterialCardView;

public class SquareView extends MaterialCardView {
  public SquareView(Context context) {
    super(context);
  }

  public SquareView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int newHeightMeasureSpec = widthMeasureSpec;
    super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
  }
}
