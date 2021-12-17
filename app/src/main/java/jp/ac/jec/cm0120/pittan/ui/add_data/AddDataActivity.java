package jp.ac.jec.cm0120.pittan.ui.add_data;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;

import jp.ac.jec.cm0120.pittan.R;
import jp.ac.jec.cm0120.pittan.database.PittanProductDataModel;
import jp.ac.jec.cm0120.pittan.database.PittanSQLiteOpenHelper;
import jp.ac.jec.cm0120.pittan.ui.objectInstallation.ObjectInstallationActivity;

public class AddDataActivity extends AppCompatActivity {

  private static final String TAG = "###";
  private Intent intent;

  /// Components
  private MaterialButtonToggleGroup segmentedControl;
  private FrameLayout frameLayout;
  private MaterialToolbar mToolbar;
  private TextInputEditText editLocation;
  private TextInputEditText editHeightSize;
  private TextInputEditText editWidthSize;
  private TextInputEditText editComments;

  ///DBItem
  private int placeDeleteFlag = 0;
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

  private void initialize() {
    mToolbar = findViewById(R.id.add_top_bar);
    frameLayout = findViewById(R.id.frame_photo);
    Button buttonCurtain = findViewById(R.id.button_curtain);
    Button buttonRug = findViewById(R.id.button_rug);
    segmentedControl = findViewById(R.id.segmented_controller);
    editLocation = findViewById(R.id.edit_view_location);
    editHeightSize = findViewById(R.id.edit_view_size_height);
    editWidthSize = findViewById(R.id.edit_view_size_width);
    editComments = findViewById(R.id.edit_view_comments);

    productCategory = getString(R.string.add_segment_first_item);
  }

  private void buildAppTopBar() {
    mToolbar.setTitle("");
    setSupportActionBar(mToolbar);
  }

  private void setListener() {

    /// StartActivityForResult
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
              if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                  productColorCode = data.getStringExtra("colorCode");
                  productDesign = data.getStringExtra("design");
                  productType = data.getStringExtra("type");
                  productImagePath = data.getStringExtra("imagePath");
                }
              }
            }
    );

    /// AppTopBar
    mToolbar.setNavigationOnClickListener(view -> finish());

    /// PhotoFrame
    frameLayout.setOnClickListener(view -> {
      intent = new Intent(this, ObjectInstallationActivity.class);
      startForResult.launch(intent);
    });

    /// SegmentedControl
    segmentedControl.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
      if (checkedId == R.id.button_curtain) {
        segmentedControl.check(R.id.button_curtain);
        segmentedControl.uncheck(R.id.button_rug);
        productCategory = getString(R.string.add_segment_first_item);
      } else if (checkedId == R.id.button_rug) {
        segmentedControl.check(R.id.button_rug);
        segmentedControl.uncheck(R.id.button_curtain);
        productCategory = getString(R.string.add_segment_second_item);
      }
    });

  }

  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.add_top_app_bar, menu);
    return super.onCreateOptionsMenu(menu);
  }

  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.menu_item_save_data) {
      // TODO: 2021/12/10  PittanSQLiteOpenHelperのInsert実行　鬼門は画像保存

      if (editHeightSize.getText().toString().length() != 0) {
        productHeight = Float.parseFloat(editHeightSize.getText().toString());
      }
      if (editWidthSize.getText().toString().length() != 0) {
        productWidth = Float.parseFloat(editWidthSize.getText().toString());
      }
      if (editLocation.getText().toString().length() != 0) {
        insertPittanDB();
      } else {
        Toast.makeText(this, "設置場所を入力してください", Toast.LENGTH_SHORT).show();
      }
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }


  /// PittanDBにデータをインサート
  private void insertPittanDB() {
    PittanProductDataModel model = new PittanProductDataModel();

    ///テスト
    productImagePath = "外部ストレージのパス";

    /// placeテーブル
    model.setPlaceName(editLocation.getText().toString());
    model.setPlaceDeleteFlag(placeDeleteFlag);

    ///productテーブル
    model.setProductHeight(productHeight);
    model.setProductWidth(productWidth);
    model.setProductCategory(productCategory);
    model.setProductColorCode(productColorCode);
    model.setProductDesign(productDesign);
    model.setProductType(productType);
    model.setProductComment(editComments.getText().toString());

    ///productImageテーブル
    model.setProductImagePath(productImagePath);

    PittanSQLiteOpenHelper helper = new PittanSQLiteOpenHelper(this);

    try {
      helper.insertProductData(model);
    } catch (RuntimeException runtimeException) {
      runtimeException.printStackTrace();
    }
  }

}