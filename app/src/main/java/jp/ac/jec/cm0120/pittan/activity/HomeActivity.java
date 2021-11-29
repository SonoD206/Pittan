package jp.ac.jec.cm0120.pittan.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import jp.ac.jec.cm0120.pittan.R;

public class HomeActivity extends AppCompatActivity  {

  private static final String TAG = "###";
  private ArrayList<ProductDataModel> productDataModelArrayList;
  private RecyclerView mRecyclerView;
  private CustomRecyclerAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private Intent intent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    Toolbar toolbar = findViewById(R.id.toolbar);
    ImageButton imageButtonCentralWoman = findViewById(R.id.image_button_central_woman);
    FloatingActionButton fab = findViewById(R.id.fab);
    setSupportActionBar(toolbar);

    //テストデータ
    productDataModelArrayList = new ArrayList<>();
    productDataModelArrayList.add(new ProductDataModel("子供部屋", "3000mm", "900mm", "カーテン"));
    productDataModelArrayList.add(new ProductDataModel("子供部屋", "3000mm", "900mm", "カーテン"));
    productDataModelArrayList.add(new ProductDataModel("子供部屋", "3000mm", "900mm", "カーテン"));
    productDataModelArrayList.add(new ProductDataModel("子供部屋", "3000mm", "900mm", "カーテン"));

    buildRecyclerView();

    imageButtonCentralWoman.setOnClickListener(view -> {
      intent = new Intent(this,AddDataActivity.class);
      startActivity(intent);
    });

    fab.setOnClickListener(view -> {
      intent = new Intent(this,AddDataActivity.class);
      startActivity(intent);
    });

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
      intent = new Intent(this,SettingActivity.class);
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /// RecyclerViewの作成
  private void buildRecyclerView(){
    mRecyclerView = findViewById(R.id.recycler_view);
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(this);
    mAdapter = new CustomRecyclerAdapter(productDataModelArrayList);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mAdapter.setOnItemClickListener(new CustomRecyclerAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(int position) {
        intent = new Intent(HomeActivity.this,DetailActivity.class);
        startActivity(intent);
      }
    });
  }
}