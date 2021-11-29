package jp.ac.jec.cm0120.pittan.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jp.ac.jec.cm0120.pittan.R;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder> {

  private ArrayList<ProductDataModel> productDataModelArrayList;
  private OnItemClickListener onItemClickListener;

  public interface OnItemClickListener{
      void onItemClick(int position);
  }

  public void setOnItemClickListener(OnItemClickListener listener){
      onItemClickListener = listener;
  }

  public static class CustomViewHolder extends RecyclerView.ViewHolder {

    public TextView textViewDataTitle;
    public TextView textViewDataHeight;
    public TextView textViewDataWidth;
    public TextView textViewCategory;

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

  public CustomRecyclerAdapter(ArrayList<ProductDataModel> productDataModelArrayList) {
    this.productDataModelArrayList = productDataModelArrayList;
  }

  @NonNull
  @Override
  public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
    CustomViewHolder holder = new CustomViewHolder(view, onItemClickListener);
    return holder;
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
}
