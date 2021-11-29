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

import java.util.ArrayList;

import jp.ac.jec.cm0120.pittan.R;

public class HomeActivity extends AppCompatActivity  {

  private static final String TAG = "###";
  private ArrayList<ProductDataModel> productDataModelArrayList;
  private RecyclerView recyclerView;
  private Intent intent;

  private Toolbar toolbar;
  private ImageButton imageButtonCentralWoman;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    recyclerView = findViewById(R.id.recycler_view);
    toolbar = findViewById(R.id.toolbar);
    imageButtonCentralWoman = findViewById(R.id.image_button_central_woman);
    setSupportActionBar(toolbar);

    //テストデータ
    productDataModelArrayList = new ArrayList<>();
    productDataModelArrayList.add(new ProductDataModel("子供部屋", "3000mm", "900mm", "カーテン"));
    productDataModelArrayList.add(new ProductDataModel("子供部屋", "3000mm", "900mm", "カーテン"));
    productDataModelArrayList.add(new ProductDataModel("子供部屋", "3000mm", "900mm", "カーテン"));
    productDataModelArrayList.add(new ProductDataModel("子供部屋", "3000mm", "900mm", "カーテン"));

    CustomRecyclerAdapter customRecyclerAdapter = new CustomRecyclerAdapter(this, productDataModelArrayList);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    recyclerView.setLayoutManager(linearLayoutManager);
    recyclerView.setAdapter(customRecyclerAdapter);

    imageButtonCentralWoman.setOnClickListener(view -> {
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
}