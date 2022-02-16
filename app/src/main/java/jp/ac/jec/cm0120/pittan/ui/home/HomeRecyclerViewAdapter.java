package jp.ac.jec.cm0120.pittan.ui.home;

import android.annotation.SuppressLint;
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
import jp.ac.jec.cm0120.pittan.app.AppConstant;
import jp.ac.jec.cm0120.pittan.database.PittanProductDataModel;
import jp.ac.jec.cm0120.pittan.util.PictureIO;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.CustomViewHolder> {

  /// interface
  public interface OnItemClickListener {
    void onItemClick(int position);
  }

  public interface SnackbarListener {
    void showUndoSnackbar(int position, String placeName, String placeID);
  }


  /// Fields
  private final Context context;
  private final ArrayList<PittanProductDataModel> pittanProductDataModelArrayList;
  private int mRecentlyDeletedItemPosition;
  private OnItemClickListener onItemClickListener;
  private PittanProductDataModel mRecentlyDeletedItem;
  private HomeRecyclerViewAdapter.SnackbarListener snackbarListener;

  public Context getContext() {
    return context;
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    onItemClickListener = listener;
  }

  public void setSnackbarListener(SnackbarListener snackbarListener) {
    this.snackbarListener = snackbarListener;
  }

  public HomeRecyclerViewAdapter(ArrayList<PittanProductDataModel> pittanProductDataModelArrayList, Context context) {
    this.pittanProductDataModelArrayList = pittanProductDataModelArrayList;
    this.context = context;
  }

  @NonNull
  @Override
  public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_product_item, parent, false);
    return new CustomViewHolder(view, onItemClickListener);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
    PittanProductDataModel model = pittanProductDataModelArrayList.get(position);
    holder.textViewDataTitle.setText(model.getPlaceName());
    holder.textViewDataHeight.setText(String.format(AppConstant.Objection.DATA_SIZE_FORMAT,model.getProductHeight()));
    holder.textViewDataWidth.setText(String.format(AppConstant.Objection.DATA_SIZE_FORMAT,model.getProductWidth()));
    holder.textViewCategory.setText(model.getProductCategory());
    if (model.getProductImagePath() != null){
      holder.imageViewPhoto.setImageBitmap(PictureIO.outputPicture(model.getProductImagePath()));
    }
  }

  @Override
  public int getItemCount() {
    return pittanProductDataModelArrayList.size();
  }

  public static class CustomViewHolder extends RecyclerView.ViewHolder {

    private final TextView textViewDataTitle;
    private final TextView textViewDataHeight;
    private final TextView textViewDataWidth;
    private final TextView textViewCategory;
    private final ImageView imageViewPhoto;

    public CustomViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
      super(itemView);
      textViewDataTitle = itemView.findViewById(R.id.text_view_data_title);
      textViewDataHeight = itemView.findViewById(R.id.text_view_data_height);
      textViewDataWidth = itemView.findViewById(R.id.text_view_data_width);
      textViewCategory = itemView.findViewById(R.id.text_view_data_category);
      imageViewPhoto = itemView.findViewById(R.id.home_image_view_data_photo);

      itemView.setOnClickListener(view -> {
        if (listener != null) {
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
    mRecentlyDeletedItem = pittanProductDataModelArrayList.get(position);
    mRecentlyDeletedItemPosition = position;

    String placeName = pittanProductDataModelArrayList.get(position).getPlaceName();
    String placeID = String.valueOf(pittanProductDataModelArrayList.get(position).getPlaceID());

    pittanProductDataModelArrayList.remove(position);
    notifyItemRemoved(position);

    snackbarListener.showUndoSnackbar(position, placeName, placeID);
  }

  public void undoDelete() {
    pittanProductDataModelArrayList.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
    notifyItemInserted(mRecentlyDeletedItemPosition);
  }
}
