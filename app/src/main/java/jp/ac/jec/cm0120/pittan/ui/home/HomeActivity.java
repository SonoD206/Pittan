package jp.ac.jec.cm0120.pittan.ui.home;

import static jp.ac.jec.cm0120.pittan.app.AppLog.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
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
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;

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
  private Session mSession;
  private boolean installRequested;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    initialize();
    buildAppTopBar();
    onClickFab();
  }


  private void checkPermissionAR() {
    if (mSession == null) {
      Exception exception = null;
      String message = null;
      try {
        switch (ArCoreApk.getInstance().requestInstall(this, !installRequested)) {
          case INSTALL_REQUESTED:
            installRequested = true;
            return;
          case INSTALLED:
            break;
        }

        // Create the session.
        mSession = new Session(this);
      } catch (UnavailableArcoreNotInstalledException
              | UnavailableUserDeclinedInstallationException e) {
        message = "ARCoreをインストールしてください";
        exception = e;
      } catch (UnavailableApkTooOldException e) {
        message = "ARCoreをアップデートしてください";
        exception = e;
      } catch (UnavailableSdkTooOldException e) {
        message = "Pittanをアップデートしてください";
        exception = e;
      } catch (UnavailableDeviceNotCompatibleException e) {
        message = "ARCoreに対応してません";
        exception = e;
      } catch (Exception e) {
        message = "ARSessionに失敗しました。";
        exception = e;
      }

      if (message != null) {
        Log.e(TAG, "Exception creating session", exception);
        Log.i(TAG, "checkPermissionAR: message" + message);
        return;
      }
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
    checkPermissionAR();
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
      mIntent.putExtra(AppConstant.Home.EXTRA_TRANSITION_TAG,0);
      startActivity(mIntent);
    });
  }
}