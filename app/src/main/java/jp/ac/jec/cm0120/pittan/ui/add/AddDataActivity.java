package jp.ac.jec.cm0120.pittan.ui.add;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButtonToggleGroup;

import jp.ac.jec.cm0120.pittan.R;
import jp.ac.jec.cm0120.pittan.ui.home.HomeActivity;

public class AddDataActivity extends AppCompatActivity {

  private Intent intent;
  private MaterialToolbar mToolbar;
  private Button buttonCurtain;
  private Button buttonRug;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_data);

    buildAppTopBar();

    MaterialButtonToggleGroup segmentedControl = findViewById(R.id.segmented_controller);
    buttonCurtain = findViewById(R.id.button_curtain);
    buttonRug = findViewById(R.id.button_curtain);

    segmentedControl.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
      if (checkedId == R.id.button_curtain) {
        segmentedControl.check(R.id.button_curtain);
        segmentedControl.uncheck(R.id.button_rug);
      } else if (checkedId == R.id.button_rug) {
        segmentedControl.check(R.id.button_rug);
        segmentedControl.uncheck(R.id.button_curtain);
      }
    });
  }

  private void buildAppTopBar() {
    mToolbar = findViewById(R.id.add_top_bar);
    mToolbar.setTitle("");
    setSupportActionBar(mToolbar);
  }

  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.add_top_app_bar, menu);
    return super.onCreateOptionsMenu(menu);
  }

  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.menu_item_save_data) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

}