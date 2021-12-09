package jp.ac.jec.cm0120.pittan.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import jp.ac.jec.cm0120.pittan.R;
import jp.ac.jec.cm0120.pittan.ui.add.AddDataActivity;
import jp.ac.jec.cm0120.pittan.ui.detail.DetailActivity;
import jp.ac.jec.cm0120.pittan.ui.setting.SettingActivity;
import jp.ac.jec.cm0120.pittan.database.PittanSQLiteOpenHelper;

public class HomeActivity extends AppCompatActivity {

  private Intent intent;
  private ArrayList<ProductDataModel> productDataModelArrayList;
  private Toolbar mToolbar;
  private ImageButton imageButtonCentralWoman;
  private FloatingActionButton fab;
  private PittanSQLiteOpenHelper helper;
  private CustomRecyclerAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    imageButtonCentralWoman = findViewById(R.id.image_button_central_woman);
    fab = findViewById(R.id.fab);

    buildAppTopBar();

    productDataModelArrayList = new ArrayList<>();
    onClickCentralWoman();
    onClickFab();
  }

  private void buildAppTopBar() {
    mToolbar = findViewById(R.id.toolbar);
    setSupportActionBar(mToolbar);
  }

  @Override
  protected void onResume() {
    super.onResume();
    helper = new PittanSQLiteOpenHelper(this);
    productDataModelArrayList = helper.getSelectCardData();
    buildRecyclerView();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.home_top_app_bar, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.menu_item_setting) {
      intent = new Intent(this, SettingActivity.class);
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /// RecyclerViewの作成
  private void buildRecyclerView() {
    RecyclerView mRecyclerView = findViewById(R.id.recycler_view);

    mRecyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    mAdapter = new CustomRecyclerAdapter(productDataModelArrayList, this);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);

    mAdapter.setOnItemClickListener(position -> {
      intent = new Intent(HomeActivity.this, DetailActivity.class);
      startActivity(intent);
    });

    // SnackBar
    mAdapter.setSnackbarListener((position, placeName, placeID) -> {
      View view = findViewById(R.id.coordinator_layout);
      Snackbar snackbar = Snackbar.make(view, placeName,
              Snackbar.LENGTH_LONG);
      snackbar.setAction("元に戻す", v -> mAdapter.undoDelete());
      snackbar.addCallback(new Snackbar.Callback(){
        @Override
        public void onDismissed(Snackbar transientBottomBar, int event) {
          super.onDismissed(transientBottomBar, event);
          if (helper.isUpdatePlaceTable(placeID)) {
            Toast.makeText(HomeActivity.this, "seikou", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(HomeActivity.this, "sippai", Toast.LENGTH_SHORT).show();
          }
        }
        @Override
        public void onShown(Snackbar sb) {
          super.onShown(sb);
        }
      });
      snackbar.show();
    });

    ItemTouchHelper touchHelper = new ItemTouchHelper(new CustomSwipeHelper(mAdapter));
    touchHelper.attachToRecyclerView(mRecyclerView);
  }

  // 中心の女性を押した時の処理
  private void onClickCentralWoman() {
    imageButtonCentralWoman.setOnClickListener(view -> {
      intent = new Intent(this, AddDataActivity.class);
      startActivity(intent);
    });
  }

  //　FloatingActionButtonを押した時の処理
  private void onClickFab() {
    fab.setOnClickListener(view -> {
      intent = new Intent(this, AddDataActivity.class);
      startActivity(intent);
    });
  }
}