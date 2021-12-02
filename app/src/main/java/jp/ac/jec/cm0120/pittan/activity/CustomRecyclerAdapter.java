package jp.ac.jec.cm0120.pittan.activity;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import jp.ac.jec.cm0120.pittan.R;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder> {

  private final Context context;
  private ArrayList<ProductDataModel> productDataModelArrayList;
  private OnItemClickListener onItemClickListener;
  private ProductDataModel mRecentlyDeletedItem;
  private int mRecentlyDeletedItemPosition;
  private CustomRecyclerAdapter.SnackbarListener snackbarListener;

  public interface OnItemClickListener{
    void onItemClick(int position);
  }

  public interface SnackbarListener{
    void showUndoSnackbar(int position, String dataTitle);
  }

  public Context getContext() {
    return context;
  }

  public void setOnItemClickListener(OnItemClickListener listener){
      onItemClickListener = listener;
  }

  public void setSnackbarListener(SnackbarListener snackbarListener) {
    this.snackbarListener = snackbarListener;
  }

  public CustomRecyclerAdapter(ArrayList<ProductDataModel> productDataModelArrayList, Context context) {
    this.productDataModelArrayList = productDataModelArrayList;
    this.context = context;
  }

  @NonNull
  @Override
  public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
    return new CustomViewHolder(view, onItemClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
    ProductDataModel model = productDataModelArrayList.get(position);
    holder.textViewDataTitle.setText(model.getProductTitle());
    holder.textViewDataHeight.setText(model.getProductHeight());
    holder.textViewDataWidth.setText(model.getProductWeight());
    holder.textViewCategory.setText(model.getProductCategory());
  }

  @Override
  public int getItemCount() {
    return productDataModelArrayList.size();
  }

  public static class CustomViewHolder extends RecyclerView.ViewHolder {

    private final TextView textViewDataTitle;
    private final TextView textViewDataHeight;
    private final TextView textViewDataWidth;
    private final TextView textViewCategory;

    public CustomViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
      super(itemView);
      textViewDataTitle = itemView.findViewById(R.id.text_view_data_title);
      textViewDataHeight = itemView.findViewById(R.id.text_view_data_height);
      textViewDataWidth = itemView.findViewById(R.id.text_view_data_width);
      textViewCategory = itemView.findViewById(R.id.text_view_data_category);

      itemView.setOnClickListener(view -> {
        if (listener != null){
          int position = getAdapterPosition();
          if (position != RecyclerView.NO_POSITION) {
            listener.onItemClick(position);
          }
        }
      });
    }
  }

  // Cellを削除した時に呼ばれる
  public void deleteItem(int position) {
    mRecentlyDeletedItem = productDataModelArrayList.get(position);
    mRecentlyDeletedItemPosition = position;
    String dataTitle = productDataModelArrayList.get(position).getProductTitle();
    productDataModelArrayList.remove(position);
    notifyItemRemoved(position);
    snackbarListener.showUndoSnackbar(position, dataTitle);
  }

  public void undoDelete() {
    productDataModelArrayList.add(mRecentlyDeletedItemPosition,
            mRecentlyDeletedItem);
    notifyItemInserted(mRecentlyDeletedItemPosition);
  }
}
