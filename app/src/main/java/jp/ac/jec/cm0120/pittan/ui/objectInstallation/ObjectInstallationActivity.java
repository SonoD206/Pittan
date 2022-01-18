package jp.ac.jec.cm0120.pittan.ui.objectInstallation;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import jp.ac.jec.cm0120.pittan.R;

public class ObjectInstallationActivity extends AppCompatActivity {

  // Components
  private TabLayout mTabLayout;
  private ViewPager2 mViewPager2;
  private ImageButton imageButtonClose;
  private ImageButton imageButtonDelete;

  // Fields
  private BottomMenuAdapter bottomMenuAdapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_object_installation);

    initialize();
    buildViewPager2();

  }

  private void initialize() {

    mTabLayout = findViewById(R.id.tab_menu_category);
    mViewPager2 = findViewById(R.id.view_pager2_menu_item);
    imageButtonClose = findViewById(R.id.image_button_close);
    imageButtonDelete = findViewById(R.id.image_button_delete);


  }

  private void buildViewPager2() {
    bottomMenuAdapter = new BottomMenuAdapter(this);
    mViewPager2.setUserInputEnabled(false);
    mViewPager2.setAdapter(bottomMenuAdapter);

    new TabLayoutMediator(mTabLayout, mViewPager2,
            (tab, position) -> {

              switch (position) {
                case 0:
                  tab.setText("設置");
                  break;
                case 1:
                  tab.setText("サイズ");
                  break;
                default:
                  tab.setText("");
                  break;
              }
            }
    ).attach();
  }
}