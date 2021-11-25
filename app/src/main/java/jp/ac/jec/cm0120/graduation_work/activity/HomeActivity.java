package jp.ac.jec.cm0120.graduation_work.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jp.ac.jec.cm0120.graduation_work.R;

public class HomeActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private ArrayList<ProductDataModel> productDataModelArrayList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    recyclerView = findViewById(R.id.recycler_view);

    //テストデータ
    productDataModelArrayList = new ArrayList<>();
    productDataModelArrayList.add(new ProductDataModel("子供部屋","3000mm","900mm","カーテン"));
    productDataModelArrayList.add(new ProductDataModel("子供部屋","3000mm","900mm","カーテン"));
    productDataModelArrayList.add(new ProductDataModel("子供部屋","3000mm","900mm","カーテン"));
    productDataModelArrayList.add(new ProductDataModel("子供部屋","3000mm","900mm","カーテン"));

    CustomRecyclerAdapter customRecyclerAdapter = new CustomRecyclerAdapter(this,productDataModelArrayList);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    recyclerView.setLayoutManager(linearLayoutManager);
    recyclerView.setAdapter(customRecyclerAdapter);
  }
}