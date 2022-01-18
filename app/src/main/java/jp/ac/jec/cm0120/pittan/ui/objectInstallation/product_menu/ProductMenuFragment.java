package jp.ac.jec.cm0120.pittan.ui.objectInstallation.product_menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jp.ac.jec.cm0120.pittan.R;

public class ProductMenuFragment extends Fragment {

  private static final String TAG = "###";
  /// Components
  private RecyclerView productMenuRecyclerView;

  /// Fields
  private ArrayList<ProductMenuModel> productMenuArrayList;
  private ProductMenuRecyclerViewAdapter productMenuAdapter;
  // Test Data
  private int[] productMenuCurtainModels = new int[]{
          R.drawable.icon_curtain_double,
          R.drawable.central_woman, R.drawable.central_woman, R.drawable.central_woman, R.drawable.central_woman, R.drawable.central_woman, R.drawable.central_woman, R.drawable.central_woman, R.drawable.central_woman,
          R.drawable.icon_curtain_single
  };

  public ProductMenuFragment() {
    // Required empty public constructor
  }

  public static ProductMenuFragment newInstance() {
    ProductMenuFragment fragment = new ProductMenuFragment();
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
    View view = inflater.inflate(R.layout.fragment_product_menu, container, false);
    productMenuRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_product_menu_double_opening);
    productMenuArrayList = addTestData();
    buildProductMenuRecyclerView();
    return view;
  }

  /// add TestData
  private ArrayList<ProductMenuModel> addTestData() {
    ArrayList<ProductMenuModel> ary = new ArrayList<>();

    for (int model : productMenuCurtainModels) {
      ProductMenuModel temp = new ProductMenuModel();
      temp.setMenuImage(model);
      if (model == R.drawable.icon_curtain_double) {
        temp.setItemViewType(0);
      } else if (model == R.drawable.icon_curtain_single){
        temp.setItemViewType(1);
      } else {
        temp.setItemViewType(2);
      }
      ary.add(temp);
    }
    return ary;
  }

  private void buildProductMenuRecyclerView(){

    productMenuAdapter = new ProductMenuRecyclerViewAdapter(this.getContext(), productMenuArrayList);
    productMenuRecyclerView.setAdapter(productMenuAdapter);
    productMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
  }




}