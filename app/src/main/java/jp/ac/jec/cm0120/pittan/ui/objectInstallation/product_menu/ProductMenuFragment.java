package jp.ac.jec.cm0120.pittan.ui.objectInstallation.product_menu;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import jp.ac.jec.cm0120.pittan.R;
import jp.ac.jec.cm0120.pittan.app.AppConstant;

public class ProductMenuFragment extends Fragment {

  /// Interface
  public interface OnClickRecyclerViewListener {
    void onClickRecyclerItem(String textureName, String beforeName);
  }

  /// Components
  private RecyclerView productMenuRecyclerView;

  /// Fields
  private ArrayList<ProductMenuModel> productMenuArrayList;
  private OnClickRecyclerViewListener mOnClickRecyclerViewListener;
  public static final   String[] MODEL_TYPES = {"両開き","片開き"};

  public ProductMenuFragment() {}

  public static ProductMenuFragment newInstance() {
    return new ProductMenuFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_product_menu, container, false);
    productMenuRecyclerView = view.findViewById(R.id.recycler_view_product_menu_double_opening);
    addProductData();
    buildProductMenuRecyclerView();
    return view;
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (context instanceof OnClickRecyclerViewListener) {
      mOnClickRecyclerViewListener = (OnClickRecyclerViewListener) context;
    }
  }

  private void addProductData() {
    ArrayList<String> productMenuCurtainName = getProductName();
    ArrayList<Integer> productMenuCurtainImage = getProductImage();
    productMenuArrayList = getProductMenuData(productMenuCurtainName, productMenuCurtainImage);
  }

  private ArrayList<ProductMenuModel> getProductMenuData(ArrayList<String> modelNames, ArrayList<Integer> modelImages) {
    ArrayList<ProductMenuModel> ary = new ArrayList<>();
    for (int i = 0; i < modelNames.size(); i++) {
      ProductMenuModel tmp = new ProductMenuModel();
      tmp.setItemModelName(modelNames.get(i));
      tmp.setItemModelImage(modelImages.get(i));
      tmp.setItemModelType(MODEL_TYPES[i]);
      ary.add(tmp);
    }
    return ary;
  }

  // Load the name from Assets/models
  private ArrayList<String> getProductName() {
    ArrayList<String> nameArrayList = new ArrayList<>();
    AssetManager assetManager = getResources().getAssets();
    String[] fileNames;
    try {
      fileNames = assetManager.list(AppConstant.Objection.ASSETS_FILE_NAME);
      nameArrayList.addAll(Arrays.asList(fileNames));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return nameArrayList;
  }

  private ArrayList<Integer> getProductImage() {
    ArrayList<Integer> images = new ArrayList<>();
    images.add(0,R.drawable.icon_curtain_double);
    images.add(1,R.drawable.icon_curtain_single);
    return images;
  }

  private void buildProductMenuRecyclerView() {
    ProductMenuRecyclerViewAdapter productMenuAdapter = new ProductMenuRecyclerViewAdapter(this.getContext(), productMenuArrayList);
    productMenuRecyclerView.setAdapter(productMenuAdapter);
    productMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
    AtomicReference<String> beforeName = new AtomicReference<>("");
    productMenuAdapter.setOnItemClickListener(modelName -> {
      if (mOnClickRecyclerViewListener != null) {
        mOnClickRecyclerViewListener.onClickRecyclerItem(modelName, beforeName.get());
        beforeName.set(modelName);
      }
    });
  }


}