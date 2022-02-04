package jp.ac.jec.cm0120.pittan.ui.add_data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import jp.ac.jec.cm0120.pittan.R;
import jp.ac.jec.cm0120.pittan.app.AppConstant;
import jp.ac.jec.cm0120.pittan.app.AppLog;
import jp.ac.jec.cm0120.pittan.database.PittanProductDataModel;
import jp.ac.jec.cm0120.pittan.database.PittanSQLiteOpenHelper;
import jp.ac.jec.cm0120.pittan.ui.home.HomeActivity;
import jp.ac.jec.cm0120.pittan.ui.objectInstallation.ObjectInstallationActivity;
import jp.ac.jec.cm0120.pittan.util.PictureIO;

public class AddDataActivity extends AppCompatActivity {
  /// Components
  private LinearLayout mLinearLayout;
  private MaterialButtonToggleGroup segmentedControl;
  private FrameLayout frameLayout;
  private MaterialToolbar mToolbar;
  private TextInputEditText editLocation;
  private TextInputEditText editHeightSize;
  private TextInputEditText editWidthSize;
  private TextInputEditText editComments;
  private ImageView imageViewPhoto;

  /// Fields
  private Intent mIntent;
  private InputMethodManager mInputMethodManager;
  private String transitionName;
  private Bitmap photoBitmap;

  ///DBItem
  public static final int PLACE_DELETE_FLAG = 0;
  private float productHeight = 0f;
  private float productWidth = 0f;
  private String productCategory;
  private String productColorCode;
  private String productDesign;
  private String productType;
  private String productImagePath;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_data);

    initialize();
    buildAppTopBar();
    setListener();
  }

  /// 初期設定
  private void initialize() {
    mLinearLayout = findViewById(R.id.add_main_layout);
    mToolbar = findViewById(R.id.add_top_bar);
    frameLayout = findViewById(R.id.frame_photo);
    segmentedControl = findViewById(R.id.segmented_controller);
    editLocation = findViewById(R.id.edit_view_location);
    editHeightSize = findViewById(R.id.edit_view_size_height);
    editWidthSize = findViewById(R.id.edit_view_size_width);
    editComments = findViewById(R.id.edit_view_comments);
    imageViewPhoto = findViewById(R.id.add_image_view_user_photo);
    productCategory = getString(R.string.add_segment_first_item);
    mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    setTransitionName();
  }
  /// AppToolBarの作成
  private void buildAppTopBar() {
    mToolbar.setTitle("");
    setSupportActionBar(mToolbar);
  }
  /// 各ListenerのSet
  private void setListener() {
    /// StartActivityForResult
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
              if (result.getResultCode() != Activity.RESULT_OK) {
                AppLog.info(AppConstant.Log.FAILURE);
              }
              Intent data = result.getData();
              if (data != null) {
                productImagePath = data.getStringExtra(AppConstant.Objection.EXTRA_IMAGE_FILE_PATH);
                String tempPath = data.getStringExtra(AppConstant.Objection.EXTRA_IMAGE_TEMP_FILE_PATH);
                setPhotoImage(tempPath);
              }
            }
    );
    /// AppTopBar
    mToolbar.setNavigationOnClickListener(view -> {
      if (transitionName.equals(AppConstant.Detail.ACTIVITY_NAME)){
        finish();
      }  else if (transitionName.equals(AppConstant.Objection.ACTIVITY_NAME)){
        mIntent = new Intent(this, HomeActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(mIntent);
      }
    });

    /// PhotoFrame
    frameLayout.setOnClickListener(view -> {
      mIntent = new Intent(this, ObjectInstallationActivity.class);
      startForResult.launch(mIntent);
    });

    /// SegmentedControl
    segmentedControl.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
      if (checkedId == R.id.button_curtain) {
        checkSegmentControl(true);
      } else if (checkedId == R.id.button_rug) {
        checkSegmentControl(false);
      }
    });

    editLocation.setOnKeyListener(this::doCloseKeyboard);
    editWidthSize.setOnKeyListener(this::doCloseKeyboard);
    editHeightSize.setOnKeyListener(this::doCloseKeyboard);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    if (ev.getAction() == MotionEvent.ACTION_DOWN) {
      View view = getCurrentFocus();
      if (view instanceof EditText) {
        Rect outRect = new Rect();
        view.getGlobalVisibleRect(outRect);
        if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
          view.clearFocus();
          mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
      }
    }
    return super.dispatchTouchEvent(ev);
  }

  private void setTransitionName() {
    mIntent = getIntent();
    transitionName = mIntent.getStringExtra(AppConstant.EXTRA_TRANSITION_NAME);
    if (transitionName.equals(AppConstant.Detail.ACTIVITY_NAME)){
      ArrayList<PittanProductDataModel> pittanProductDataModelArrayList = (ArrayList<PittanProductDataModel>) mIntent.getSerializableExtra(AppConstant.Detail.EXTRA_MODEL);
      setDetailData(pittanProductDataModelArrayList);
    } else if (transitionName.equals(AppConstant.Objection.ACTIVITY_NAME)){
      productImagePath = mIntent.getStringExtra(AppConstant.Objection.EXTRA_IMAGE_FILE_PATH);
      String tempPath = mIntent.getStringExtra(AppConstant.Objection.EXTRA_IMAGE_TEMP_FILE_PATH);
      photoBitmap = PictureIO.outputPicture(tempPath);
      imageViewPhoto.setVisibility(View.VISIBLE);
      imageViewPhoto.setImageBitmap(photoBitmap);
    }
  }

  private void setDetailData(ArrayList<PittanProductDataModel> models) {
    if (models != null){
      for (PittanProductDataModel model: models) {
        editLocation.setText(model.getPlaceName());
        editHeightSize.setText(String.valueOf(model.getProductHeight()));
        editWidthSize.setText(String.valueOf(model.getProductWidth()));
        editComments.setText(model.getProductComment());
        if (model.getProductCategory().equals(getString(R.string.add_segment_first_item)) || model.getProductCategory().equals("")){
          checkSegmentControl(true);
        } else if (model.getProductCategory().equals(getString(R.string.add_segment_second_item))){
          checkSegmentControl(false);
        }
        String imagePath = model.getProductImagePath();
        setPhotoImage(imagePath);
      }
    }
  }

  private boolean doCloseKeyboard(View view, int keyCode, KeyEvent keyEvent) {
    if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
      //キーボードを閉じる
      mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
      mLinearLayout.requestLayout();
      return true;
    }
    return false;
  }

  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.add_top_app_bar, menu);
    return super.onCreateOptionsMenu(menu);
  }

  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.menu_item_save_data) {
      if (Objects.requireNonNull(editHeightSize.getText()).toString().length() != 0) {
        productHeight = Float.parseFloat(editHeightSize.getText().toString());
      }
      if (Objects.requireNonNull(editWidthSize.getText()).toString().length() != 0) {
        productWidth = Float.parseFloat(editWidthSize.getText().toString());
      }
      if (Objects.requireNonNull(editLocation.getText()).toString().length() != 0) {
        insertPittanDB();
        try {
          PictureIO.saveBitmapToDisk(photoBitmap,productImagePath);
        } catch (IOException e) {
          e.printStackTrace();
        }
        mIntent = new Intent(this, HomeActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(mIntent);
      } else {
        Snackbar.make(mLinearLayout, "設置場所を入力してください", Snackbar.LENGTH_SHORT).setDuration(1000).show();
      }
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /// PittanDBにデータをインサート
  private void insertPittanDB() {
    PittanProductDataModel model = new PittanProductDataModel();
    /// placeテーブル
    model.setPlaceName(Objects.requireNonNull(editLocation.getText()).toString());
    model.setPlaceDeleteFlag(PLACE_DELETE_FLAG);

    ///productテーブル
    model.setProductHeight(productHeight);
    model.setProductWidth(productWidth);
    model.setProductCategory(productCategory);
    model.setProductColorCode(productColorCode);
    model.setProductDesign(productDesign);
    model.setProductType(productType);
    model.setProductComment(Objects.requireNonNull(editComments.getText()).toString());

    ///productImageテーブル
    model.setProductImagePath(productImagePath);

    PittanSQLiteOpenHelper helper = new PittanSQLiteOpenHelper(this);
    try {
      helper.insertProductData(model);
    } catch (RuntimeException runtimeException) {
      runtimeException.printStackTrace();
    }
  }

  private void checkSegmentControl(boolean isCheckSegmentControl) {
    if (isCheckSegmentControl){
      segmentedControl.check(R.id.button_curtain);
      segmentedControl.uncheck(R.id.button_rug);
      productCategory = getString(R.string.add_segment_first_item);
    } else {
      segmentedControl.check(R.id.button_rug);
      segmentedControl.uncheck(R.id.button_curtain);
      productCategory = getString(R.string.add_segment_second_item);
    }
  }

  private void setPhotoImage(String imagePath) {
    imageViewPhoto.setVisibility(View.VISIBLE);
    imageViewPhoto.setImageBitmap(PictureIO.outputPicture(imagePath));
  }
}