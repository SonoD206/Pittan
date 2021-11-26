package jp.ac.jec.cm0120.pittan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jp.ac.jec.cm0120.pittan.R;

public class HomeActivity extends AppCompatActivity {

  private static final String TAG = "###";
  private RecyclerView recyclerView;
  private ArrayList<ProductDataModel> productDataModelArrayList;
  private Intent intent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    recyclerView = findViewById(R.id.recycler_view);

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

  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.settings:
        Log.i(TAG, "onOptionsItemSelected: ");
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }
}