package jp.ac.jec.cm0120.pittan.ui.objectInstallation.product_menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jp.ac.jec.cm0120.pittan.R;

public class ProductMenuRecyclerViewAdapter extends RecyclerView.Adapter<ProductMenuRecyclerViewAdapter.ProductMenuRecyclerViewHolder> {

  /// Interface
  public interface OnItemClickListener {
    void onItemClick(String textureName);
  }

  // Fields
  private OnItemClickListener onItemClickListener;
  private final LayoutInflater mInflater;
  private final ArrayList<ProductMenuModel> productMenuModelArrayList;

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

    int itemMenuImage = productMenuModelArrayList.get(position).getItemModelImage();
    holder.imageViewModel.setImageResource(itemMenuImage);
    holder.textViewModelType.setText(productMenuModelArrayList.get(position).getItemModelType());
    holder.itemView.setOnClickListener(view -> {
      if (onItemClickListener != null) {
        if (position != RecyclerView.NO_POSITION) {
//          holder.imageViewType.setVisibility(View.VISIBLE);
          String modelName = productMenuModelArrayList.get(position).getItemModelName();
          onItemClickListener.onItemClick(modelName);

        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return productMenuModelArrayList.size();
  }

  public static class ProductMenuRecyclerViewHolder extends RecyclerView.ViewHolder {
    private final ImageView imageViewModel;
    private final ImageView imageViewType;
    private final TextView textViewModelType;

    public ProductMenuRecyclerViewHolder(@NonNull View itemView) {
      super(itemView);
      imageViewModel = itemView.findViewById(R.id.image_view_model);
      textViewModelType = itemView.findViewById(R.id.text_view_model_type);
      imageViewType = itemView.findViewById(R.id.image_view_check);
    }
  }
}
