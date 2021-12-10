package jp.ac.jec.cm0120.pittan.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
import jp.ac.jec.cm0120.pittan.database.PittanSQLiteOpenHelper;
import jp.ac.jec.cm0120.pittan.ui.add_data.AddDataActivity;
import jp.ac.jec.cm0120.pittan.ui.detail.DetailActivity;
import jp.ac.jec.cm0120.pittan.ui.setting.SettingActivity;

public class HomeActivity extends AppCompatActivity {

  private static final String TAG = "###";
  private Intent intent;
  private ArrayList<ProductDataModel> productDataModelArrayList;
  private Toolbar mToolbar;
  private FloatingActionButton fab;
  private LinearLayout centralLinear;
  private RecyclerView mRecyclerView;
  private PittanSQLiteOpenHelper helper;
  private CustomRecyclerAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    initialize();
    onClickFab();

    productDataModelArrayList = new ArrayList<>();
  }

  private void initialize() {
    centralLinear = findViewById(R.id.linear_layout_central_image_button);
    fab = findViewById(R.id.fab);
    mToolbar = findViewById(R.id.toolbar);
    setSupportActionBar(mToolbar);
  }

  @Override
  protected void onResume() {
    super.onResume();
    helper = new PittanSQLiteOpenHelper(this);
    productDataModelArrayList = helper.getSelectCardData();
    buildRecyclerView();

//　DBにデータがあるかないか
    if (productDataModelArrayList.size() > 0){
      mRecyclerView.setVisibility(View.VISIBLE);
      centralLinear.setVisibility(View.GONE);
    } else {
      mRecyclerView.setVisibility(View.GONE);
      centralLinear.setVisibility(View.VISIBLE);
    }
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
    mRecyclerView = findViewById(R.id.recycler_view);
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
      snackbar.setAnchorView(R.id.fab);
      snackbar.addCallback(new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar transientBottomBar, int event) {
          super.onDismissed(transientBottomBar, event);
          if (productDataModelArrayList.size() > 0){
            mRecyclerView.setVisibility(View.VISIBLE);
            centralLinear.setVisibility(View.GONE);
          } else {
            mRecyclerView.setVisibility(View.GONE);
            centralLinear.setVisibility(View.VISIBLE);
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

  //　FloatingActionButtonを押した時の処理
  private void onClickFab() {
    fab.setOnClickListener(view -> {
      intent = new Intent(this, AddDataActivity.class);
      startActivity(intent);
    });
  }
}