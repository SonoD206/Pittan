package jp.ac.jec.cm0120.graduation_work.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jp.ac.jec.cm0120.graduation_work.R;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {

  private Context context;
  private ArrayList<ProductDataModel> productDataModelArrayList;

  public CustomRecyclerAdapter(Context context, ArrayList<ProductDataModel> productDataModelArrayList) {
    this.context = context;
    this.productDataModelArrayList = productDataModelArrayList;
  }

  @NonNull
  @Override
  public CustomRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull CustomRecyclerAdapter.ViewHolder holder, int position) {
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

  public class ViewHolder extends RecyclerView.ViewHolder {

    private TextView textViewDataTitle;
    private TextView textViewDataHeight;
    private TextView textViewDataWidth;
    private TextView textViewCategory;
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      textViewDataTitle = itemView.findViewById(R.id.text_view_data_title);
      textViewDataHeight = itemView.findViewById(R.id.text_view_data_height);
      textViewDataWidth = itemView.findViewById(R.id.text_view_data_width);
      textViewCategory = itemView.findViewById(R.id.text_view_data_category);

    }
  }
}
