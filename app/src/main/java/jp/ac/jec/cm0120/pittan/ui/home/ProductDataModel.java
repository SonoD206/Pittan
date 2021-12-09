package jp.ac.jec.cm0120.pittan.ui.home;

public class ProductDataModel {

  // place table
  private int placeID;
  private String placeName;
  private int placeDeleteFlag;

  // product table
  private int productID;
  private String productCategory;
  private String productColorCode;
  private String productDesign;
  private String productType;
  private String productComment;
  private float productRecommendedSize;
  private float productWidth;
  private float productHeight;

  // product_image table
  private int productImageID;
  private String productImagePath;

  // 各Getter,Setter
  public int getPlaceID() {
    return placeID;
  }

  public void setPlaceID(int placeID) {
    this.placeID = placeID;
  }

  public String getPlaceName() {
    return placeName;
  }

  public void setPlaceName(String placeName) {
    this.placeName = placeName;
  }

  public int getPlaceDeleteFlag() {
    return placeDeleteFlag;
  }

  public void setPlaceDeleteFlag(int placeDeleteFlag) {
    this.placeDeleteFlag = placeDeleteFlag;
  }

  public int getProductID() {
    return productID;
  }

  public void setProductID(int productID) {
    this.productID = productID;
  }

  public String getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(String productCategory) {
    this.productCategory = productCategory;
  }

  public String getProductColorCode() {
    return productColorCode;
  }

  public void setProductColorCode(String productColorCode) {
    this.productColorCode = productColorCode;
  }

  public String getProductDesign() {
    return productDesign;
  }

  public void setProductDesign(String productDesign) {
    this.productDesign = productDesign;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public String getProductComment() {
    return productComment;
  }

  public void setProductComment(String productComment) {
    this.productComment = productComment;
  }

  public float getProductRecommendedSize() {
    return productRecommendedSize;
  }

  public void setProductRecommendedSize(float productRecommendedSize) {
    this.productRecommendedSize = productRecommendedSize;
  }

  public float getProductWidth() {
    return productWidth;
  }

  public void setProductWidth(float productWidth) {
    this.productWidth = productWidth;
  }

  public float getProductHeight() {
    return productHeight;
  }

  public void setProductHeight(float productHeight) {
    this.productHeight = productHeight;
  }

  public int getProductImageID() {
    return productImageID;
  }

  public void setProductImageID(int productImageID) {
    this.productImageID = productImageID;
  }

  public String getProductImagePath() {
    return productImagePath;
  }

  public void setProductImagePath(String productImagePath) {
    this.productImagePath = productImagePath;
  }
}
