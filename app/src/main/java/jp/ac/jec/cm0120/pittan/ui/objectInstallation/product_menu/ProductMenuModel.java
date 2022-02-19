package jp.ac.jec.cm0120.pittan.ui.objectInstallation.product_menu;

import android.graphics.Bitmap;

public class ProductMenuModel {

  private int itemModelImage;
  private String itemModelName;
  private String itemModelType;

  public String getItemModelType() {
    return itemModelType;
  }

  public void setItemModelType(String itemModelType) {
    this.itemModelType = itemModelType;
  }

  public int getItemModelImage() {
    return itemModelImage;
  }

  public void setItemModelImage(int itemModelImage) {
    this.itemModelImage = itemModelImage;
  }

  public String getItemModelName() {
    return itemModelName;
  }

  public void setItemModelName(String itemModelName) {
    this.itemModelName = itemModelName;
  }
}
