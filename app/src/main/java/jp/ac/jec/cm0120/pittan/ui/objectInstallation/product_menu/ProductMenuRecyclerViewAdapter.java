package jp.ac.jec.cm0120.pittan.ui.objectInstallation.product_menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jp.ac.jec.cm0120.pittan.R;

public class ProductMenuRecyclerViewAdapter extends RecyclerView.Adapter<ProductMenuRecyclerViewAdapter.ProductMenuRecyclerViewHolder> {

  /// Interface
  public interface OnItemClickListener {
    void onItemClick(String textureName);
  }

  /// Constant
  private static final String TAG = "###";

  // Fields
  private OnItemClickListener onItemClickListener;
  private LayoutInflater mInflater;
  private ArrayList<ProductMenuModel> productMenuModelArrayList;

  public ProductMenuRecyclerViewAdapter(Context context, ArrayList<ProductMenuModel> imageModels) {
    mInflater = LayoutInflater.from(context);
    productMenuModelArrayList = imageModels;
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    onItemClickListener = listener;
  }

  @NonNull
  @Override
  public ProductMenuRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view = mInflater.inflate(R.layout.product_menu_item, parent, false);

    return new ProductMenuRecyclerViewHolder(view);
  }

  /// Itemのクリック時のActionをセット
  @Override
  public void onBindViewHolder(@NonNull ProductMenuRecyclerViewHolder holder, int position) {

    Bitmap itemMenuImage = productMenuModelArrayList.get(position).getItemTextureImage();
    holder.imageViewTexture.setImageBitmap(itemMenuImage);
    holder.itemView.setOnClickListener(view -> {
      if (onItemClickListener != null) {
        if (position != RecyclerView.NO_POSITION) {
          String textureName = productMenuModelArrayList.get(position).getItemTextureName();
          onItemClickListener.onItemClick(textureName);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return productMenuModelArrayList.size();
  }

  public static class ProductMenuRecyclerViewHolder extends RecyclerView.ViewHolder {

    private final ImageView imageViewTexture;
    public ProductMenuRecyclerViewHolder(@NonNull View itemView) {
      super(itemView);

      imageViewTexture = itemView.findViewById(R.id.image_view_model);
    }
  }
}
