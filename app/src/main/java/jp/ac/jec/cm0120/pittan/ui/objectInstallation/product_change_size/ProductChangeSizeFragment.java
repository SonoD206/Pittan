package jp.ac.jec.cm0120.pittan.ui.objectInstallation.product_change_size;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import jp.ac.jec.cm0120.pittan.R;
import jp.ac.jec.cm0120.pittan.app.AppConstant;

public class ProductChangeSizeFragment extends Fragment {

  /// Interface
  public interface ChangeSeekbarListener {
    void changeSeekbar(float size, String kindName);
  }

  /// Components
  private SeekBar seekBarHeight;
  private SeekBar seekBarWidth;

  /// Fields
  private ChangeSeekbarListener mListener;

  public ProductChangeSizeFragment() {}

  public static ProductChangeSizeFragment newInstance() { return new ProductChangeSizeFragment(); }

  @Override
  public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.fragment_product_change_size, container, false);
     buildSeekbar(view);
     setListener();
    return view;
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (context instanceof  ChangeSeekbarListener){
      mListener = (ChangeSeekbarListener) context;
    }
  }

  private void buildSeekbar(View view) {
    seekBarHeight = view.findViewById(R.id.seek_bar_model_vertical);
    seekBarHeight.setMax(10);
    seekBarWidth = view.findViewById(R.id.seek_bar_model_horizontal);
    seekBarWidth.setMax(10);
  }

  private void setListener() {
    seekBarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        float tmpValue = seekBar.getProgress();
        float modelChangeValue = tmpValue / 10;
        if (mListener != null){
          mListener.changeSeekbar(modelChangeValue, AppConstant.Objection.CHANGE_HEIGHT);
        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {}

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {}

    });

    seekBarWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        float tmpValue = seekBar.getProgress();
        float modelVerticalValue = tmpValue / 10;
        if (mListener != null){
          mListener.changeSeekbar(modelVerticalValue, AppConstant.Objection.CHANGE_WIDTH);
        }
      }
      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {}

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {}

    });
  }

}