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

  /// Components
  private SeekBar seekBarHeight;
  private SeekBar seekBarWidth;

  /// Fields
  private ChangeSeekbarListener changeSeekbarListener;

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
      changeSeekbarListener = (ChangeSeekbarListener) context;
    }
  }

  private void buildSeekbar(View view) {
    seekBarHeight = view.findViewById(R.id.seek_bar_model_vertical);
    seekBarHeight.setMax(10);
    seekBarHeight.setProgress(0);
    seekBarWidth = view.findViewById(R.id.seek_bar_model_horizontal);
    seekBarWidth.setMax(10);
    seekBarWidth.setProgress(0);
  }

  private void setListener() {
    seekBarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      float beforeChangeValue = 0.0f;
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        float tmpValue = seekBar.getProgress();
        float modelVerticalValue = tmpValue / 100;
        if (changeSeekbarListener != null){
          changeSeekbarListener.changeSeekbar(modelVerticalValue,beforeChangeValue,AppConstant.Objection.CHANGE_HEIGHT);
        }
        beforeChangeValue = modelVerticalValue;
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {}

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {}

    });

    seekBarWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      float beforeChangeValue = 0.0f;

      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        float tmpValue = seekBar.getProgress();
        float modelHorizontalValue = tmpValue / 100;
        if (changeSeekbarListener != null){
          changeSeekbarListener.changeSeekbar(modelHorizontalValue,beforeChangeValue, AppConstant.Objection.CHANGE_WIDTH);
        }
        beforeChangeValue = modelHorizontalValue;
      }
      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {}

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {}

    });
  }

  public void setSeekBarHeightValue(float modelHeightValue){
    seekBarHeight.setMax((int)modelHeightValue);
  }
  public void setSeekBarWidthValue(float modelWidthValue){
    seekBarHeight.setMax((int)modelWidthValue);
  }

}