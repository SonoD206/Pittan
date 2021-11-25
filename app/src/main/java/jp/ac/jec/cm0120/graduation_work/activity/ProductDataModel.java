package jp.ac.jec.cm0120.graduation_work.activity;

public class ProductDataModel {

  private String productTitle;
  private String productHeight;
  private String productWidth;
  private String productCategory;

  public ProductDataModel(String title, String height, String width, String category) {
    productTitle = title;
    productHeight = height;
    productWidth = width;
    productCategory = category;
  }

  public String getProductTitle() {
    return productTitle;
  }

  public void setProductTitle(String productTitle) {
    this.productTitle = productTitle;
  }

  public String getProductHeight() {
    return productHeight;
  }

  public void setProductHeight(String productHeight) {
    this.productHeight = productHeight;
  }

  public String getProductWeight() {
    return productWidth;
  }

  public void setProductWidth(String productWidth) {
    this.productWidth = productWidth;
  }

  public String getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(String productCategory) {
    this.productCategory = productCategory;
  }
}
