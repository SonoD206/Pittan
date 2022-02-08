package jp.ac.jec.cm0120.pittan.ui.setting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


import jp.ac.jec.cm0120.pittan.R;

public class SettingRecyclerViewAdapter extends RecyclerView.Adapter<SettingRecyclerViewAdapter.CustomViewHolder> {

  /// Interfaces
  public interface OnItemClickListener {
    void onItemClick(int position);
  }

  /// Fields
  private ArrayList<String> settingItems = new ArrayList<>();
  private OnItemClickListener mListener;

  public SettingRecyclerViewAdapter(ArrayList<String> settingItems) {
    this.settingItems = settingItems;
  }

  public void setOnItemClickListener(SettingRecyclerViewAdapter.OnItemClickListener listener) {
    mListener = listener;
  }

  @NonNull
  @Override
  public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_list_item,parent,false);
    return new CustomViewHolder(view, mListener);
  }

  @Override
  public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
    holder.textViewLicenseTitle.setText(settingItems.get(0));
  }

  @Override
  public int getItemCount() {
    return settingItems.size();
  }

  public static class CustomViewHolder extends RecyclerView.ViewHolder {

    private final TextView textViewLicenseTitle;

    public CustomViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
      super(itemView);
      textViewLicenseTitle = itemView.findViewById(R.id.text_view_licence);
      itemView.setOnClickListener(view -> {
        if ( listener != null) {
          int position = getAdapterPosition();
          if (position != RecyclerView.NO_POSITION) {
            listener.onItemClick(position);
          }
        }
      });
    }
  }
}
