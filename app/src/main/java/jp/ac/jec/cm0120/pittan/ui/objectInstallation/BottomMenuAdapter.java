package jp.ac.jec.cm0120.pittan.ui.objectInstallation;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import jp.ac.jec.cm0120.pittan.ui.objectInstallation.product_change_size.ProductChangeSizeFragment;
import jp.ac.jec.cm0120.pittan.ui.objectInstallation.product_menu.ProductMenuFragment;

public class BottomMenuAdapter extends FragmentStateAdapter {

  public static final int FRAGMENT_PAGE_NUM = 2;

  public BottomMenuAdapter(FragmentActivity activity) {
    super(activity);
  }


  @NonNull
  @Override
  public Fragment createFragment(int position) {
    Fragment fragment = null;
    if (position == 0){
      fragment = ProductMenuFragment.newInstance();
    } else if (position == 1){
      fragment = ProductChangeSizeFragment.newInstance();
    }
    return fragment;
  }

  @Override
  public int getItemCount() {
    return FRAGMENT_PAGE_NUM;
  }
}
