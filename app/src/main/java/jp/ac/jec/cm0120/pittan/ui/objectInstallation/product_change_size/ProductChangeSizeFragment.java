package jp.ac.jec.cm0120.pittan.ui.objectInstallation.product_change_size;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.ac.jec.cm0120.pittan.R;

public class ProductChangeSizeFragment extends Fragment {


  public ProductChangeSizeFragment() {
    // Required empty public constructor
  }

  public static ProductChangeSizeFragment newInstance() {
    ProductChangeSizeFragment fragment = new ProductChangeSizeFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_product_change_size, container, false);
  }
}