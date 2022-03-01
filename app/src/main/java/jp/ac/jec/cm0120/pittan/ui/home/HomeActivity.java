package jp.ac.jec.cm0120.pittan.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.ArCoreApk;

import java.util.ArrayList;

import jp.ac.jec.cm0120.pittan.R;
import jp.ac.jec.cm0120.pittan.app.AppConstant;
import jp.ac.jec.cm0120.pittan.database.PittanProductDataModel;
import jp.ac.jec.cm0120.pittan.database.PittanSQLiteOpenHelper;
import jp.ac.jec.cm0120.pittan.ui.detail.DetailActivity;
import jp.ac.jec.cm0120.pittan.ui.objectInstallation.ObjectInstallationActivity;
import jp.ac.jec.cm0120.pittan.ui.setting.SettingActivity;

public class HomeActivity extends AppCompatActivity {

  private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

  /// Components
  private Toolbar mToolbar;
  private FloatingActionButton fab;
  private LinearLayout centralLinear;
  private RecyclerView homeRecyclerView;

  /// Fields
  private Intent mIntent;
  private ArrayList<PittanProductDataModel> pittanProductDataModelArrayList = new ArrayList<>();
  private PittanSQLiteOpenHelper helper;
  private HomeRecyclerViewAdapter mAdapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    maybeEnableAr();
    initialize();
    buildAppTopBar();
    onClickFab();
  }

  private void maybeEnableAr() {
    ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
    if (availability.isTransient()) {
      // Continue to query availability at 5Hz while compatibility is checked in the background.
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          maybeEnableAr();
        }
      }, 200);
    }

    if (availability.isSupported()) {
      Toast.makeText(this, "ARCoreに対応しています", Toast.LENGTH_SHORT).show();
    } else {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle("ARCoreに対応していません")
              .setMessage("本デバイスはARCoreに対応していませんので、一部機能が使用できません。")
              .setPositiveButton(getString(R.string.ok), null)
              .setNegativeButton(getString(R.string.cancel), null).show();
    }
  }


  private void checkPermissionStorage() {
    if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }
  }

  private void initialize() {
    mToolbar = findViewById(R.id.toolbar);
    homeRecyclerView = findViewById(R.id.recycler_view_home);
    centralLinear = findViewById(R.id.linear_layout_central_woman);
    fab = findViewById(R.id.fab);
  }

  private void buildAppTopBar() {
    setSupportActionBar(mToolbar);
  }

  @Override
  protected void onResume() {
    super.onResume();
    helper = new PittanSQLiteOpenHelper(this);
    pittanProductDataModelArrayList = helper.getSelectCardData();
    buildRecyclerView();
    checkPermissionStorage();

//　DBにデータがあるかないか
    if (pittanProductDataModelArrayList.size() > 0) {
      homeRecyclerView.setVisibility(View.VISIBLE);
      centralLinear.setVisibility(View.GONE);
    } else {
      homeRecyclerView.setVisibility(View.GONE);
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
      mIntent = new Intent(this, SettingActivity.class);
      startActivity(mIntent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /// RecyclerViewの作成
  private void buildRecyclerView() {
    homeRecyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    mAdapter = new HomeRecyclerViewAdapter(pittanProductDataModelArrayList, this);
    homeRecyclerView.setLayoutManager(mLayoutManager);
    homeRecyclerView.setAdapter(mAdapter);

    mAdapter.setOnItemClickListener(position -> {
      int currentPlaceID = pittanProductDataModelArrayList.get(position).getPlaceID();
      mIntent = new Intent(HomeActivity.this, DetailActivity.class);
      mIntent.putExtra(AppConstant.Home.EXTRA_PLACE_ID, currentPlaceID);
      startActivity(mIntent);
    });

    // SnackBar
    mAdapter.setSnackbarListener((position, placeName, placeID) -> {
      View view = findViewById(R.id.coordinator_layout);
      Snackbar snackbar = Snackbar.make(view, placeName,
              Snackbar.LENGTH_LONG);
      snackbar.setAction(getString(R.string.home_snackbar_action_text), v -> mAdapter.undoDelete());
      snackbar.setAnchorView(R.id.fab);
      snackbar.addCallback(new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar transientBottomBar, int event) {
          super.onDismissed(transientBottomBar, event);
          if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_SWIPE
                  || event == DISMISS_EVENT_CONSECUTIVE || event == DISMISS_EVENT_MANUAL) {
            helper.isUpdatePlaceDeleteFlag(placeID);
          }
          if (pittanProductDataModelArrayList.size() > 0) {
            homeRecyclerView.setVisibility(View.VISIBLE);
            centralLinear.setVisibility(View.GONE);
          } else {
            homeRecyclerView.setVisibility(View.GONE);
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
    touchHelper.attachToRecyclerView(homeRecyclerView);
  }

  //　FloatingActionButtonを押した時の処理
  private void onClickFab() {
    fab.setOnClickListener(view -> {
      mIntent = new Intent(this, ObjectInstallationActivity.class);
      mIntent.putExtra(AppConstant.Home.EXTRA_TRANSITION_TAG, 0);
      startActivity(mIntent);
    });
  }
}