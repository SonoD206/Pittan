package jp.ac.jec.cm0120.pittan.ui.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import jp.ac.jec.cm0120.pittan.R;
import jp.ac.jec.cm0120.pittan.app.AppLog;

public class SettingActivity extends AppCompatActivity {

  /// Components
  private Toolbar mToolbar;
  private RecyclerView settingRecyclerView;

  private final ArrayList<String> settingItems = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);

    initialize();
    buildAppTopBar();
    buildRecyclerView();
  }

  private void initialize() {
    mToolbar = findViewById(R.id.setting_top_bar);
    settingRecyclerView = findViewById(R.id.recycler_view_setting);
    settingItems.add(getString(R.string.setting_license));
  }

  private void buildAppTopBar() {
    mToolbar.setTitle(getString(R.string.setting));
    setSupportActionBar(mToolbar);
    mToolbar.setNavigationOnClickListener(view -> finish());
  }

  private void buildRecyclerView() {
    RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL);
    settingRecyclerView.addItemDecoration(itemDecoration);
    settingRecyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    SettingRecyclerViewAdapter mAdapter = new SettingRecyclerViewAdapter(settingItems);
    settingRecyclerView.setLayoutManager(mLayoutManager);
    settingRecyclerView.setAdapter(mAdapter);

    mAdapter.setOnItemClickListener(position -> {
      AppLog.info("kita");
    });
  }

}