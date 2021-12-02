package jp.ac.jec.cm0120.pittan.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

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

public class HomeActivity extends AppCompatActivity {

  private ArrayList<ProductDataModel> productDataModelArrayList;
  private RecyclerView mRecyclerView;
  private CustomRecyclerAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private Intent intent;
  private ImageButton imageButtonCentralWoman;
  private FloatingActionButton fab;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    Toolbar toolbar = findViewById(R.id.toolbar);
    imageButtonCentralWoman = findViewById(R.id.image_button_central_woman);
    fab = findViewById(R.id.fab);

    setSupportActionBar(toolbar);

    //テストデータ
    productDataModelArrayList = new ArrayList<>();
    productDataModelArrayList.add(new ProductDataModel("子供部屋", "3000mm", "900mm", "カーテン"));
    productDataModelArrayList.add(new ProductDataModel("子供部屋", "3000mm", "900mm", "カーテン"));
    productDataModelArrayList.add(new ProductDataModel("子供部屋", "3000mm", "900mm", "カーテン"));
    productDataModelArrayList.add(new ProductDataModel("子供部屋", "3000mm", "900mm", "カーテン"));

    buildRecyclerView();
    onClickCentralWoman();
    onClickFab();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.top_app_bar, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.settings) {
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
    mLayoutManager = new LinearLayoutManager(this);
    mAdapter = new CustomRecyclerAdapter(productDataModelArrayList, this);

    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);

    mAdapter.setOnItemClickListener(position -> {
      intent = new Intent(HomeActivity.this, DetailActivity.class);
      startActivity(intent);
    });

    mAdapter.setSnackbarListener((position, dataTitle) -> {
      View view = findViewById(R.id.coordinator_layout);
      Snackbar snackbar = Snackbar.make(view, dataTitle,
              Snackbar.LENGTH_LONG);
      snackbar.setAction("元に戻す", v -> mAdapter.undoDelete());
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