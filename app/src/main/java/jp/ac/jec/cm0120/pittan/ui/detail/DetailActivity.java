package jp.ac.jec.cm0120.pittan.ui.detail;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

import jp.ac.jec.cm0120.pittan.R;
import jp.ac.jec.cm0120.pittan.database.PittanProductDataModel;
import jp.ac.jec.cm0120.pittan.database.PittanSQLiteOpenHelper;
import jp.ac.jec.cm0120.pittan.ui.add_data.AddDataActivity;
import jp.ac.jec.cm0120.pittan.ui.home.HomeActivity;

public class DetailActivity extends AppCompatActivity {

  /// Constants
  private static final String TAG = "###";
  public static final String EXTRA_MODEL = "PittanProductDataModel";
  public static final String EXTRA_TRANSITION_NAME = "TransitionName";

  // Component
  private MaterialToolbar mToolbar;
  private CardView cardView;
  private ImageView imageView;
  private TextView textViewPlaceTitle;
  private TextView textViewItemCategory;
  private TextView textViewItemHeight;
  private TextView textViewItemWidth;
  private TextView textViewComments;

  /// Fields
  private Intent mIntent;
  private int placeID;
  private String imagePath;
  private ArrayList<PittanProductDataModel> selectItem = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    initialize();
    buildAppTopBar();
    setItemData();
  }

  private void initialize() {

    mToolbar = findViewById(R.id.detail_top_bar);
    cardView = findViewById(R.id.detail_card_view_item_photo_frame);
    imageView = findViewById(R.id.detail_image_view_item_photo);
    textViewPlaceTitle = findViewById(R.id.detail_text_view_item_place_title);
    textViewItemCategory = findViewById(R.id.detail_text_view_item_category);
    textViewItemHeight = findViewById(R.id.detail_text_view_item_height);
    textViewItemWidth = findViewById(R.id.detail_text_view_item_width);
    textViewComments = findViewById(R.id.detail_text_view_comments_area);

    mIntent = getIntent();
    placeID = mIntent.getIntExtra(HomeActivity.EXTRA_PLACE_ID, 1);
  }

  private void buildAppTopBar() {
    mToolbar.setTitle("");
    setSupportActionBar(mToolbar);

    mToolbar.setNavigationOnClickListener(view -> finish());
  }

  private void setItemData() {
    PittanSQLiteOpenHelper helper = new PittanSQLiteOpenHelper(this);
    selectItem = helper.getSelectDetailData(placeID);

    if (selectItem.size() > 0) {

      for (PittanProductDataModel item : selectItem) {
        String itemHeight = item.getProductHeight() + getResources().getString(R.string.millimetre);
        String itemWidth = item.getProductWidth() + getResources().getString(R.string.millimetre);

        textViewPlaceTitle.setText(item.getPlaceName());
        textViewItemCategory.setText(item.getProductCategory());
        textViewItemHeight.setText(itemHeight);
        textViewItemWidth.setText(itemWidth);
        textViewComments.setText(item.getProductComment());
        // TODO: 2022/01/31 ImagePath -> file -> bitmap -> 反映
        imagePath = item.getProductImagePath();
      }

    } else {
      Log.i(TAG, "setItemData: Trouble");
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.detail_top_app_bar, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.menu_item_edit) {
      mIntent = new Intent(this, AddDataActivity.class);
      mIntent.putExtra(EXTRA_MODEL,selectItem);
      mIntent.putExtra(EXTRA_TRANSITION_NAME,"Detail");
      startActivity(mIntent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}