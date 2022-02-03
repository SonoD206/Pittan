package jp.ac.jec.cm0120.pittan.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.util.ArrayList;


public class PittanSQLiteOpenHelper extends SQLiteOpenHelper {

  public static final String DB_NAME = "Pittan";
  public static final int DB_VERSION = 1;
  public static final String TABLE_PRODUCT = "product";
  public static final String TABLE_PLACE = "place";
  public static final String TABLE_PRODUCT_IMAGE = "product_image";
  public static final  String TAG = "###";

  public PittanSQLiteOpenHelper(@Nullable Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {

    //　テーブル作成
    String createProductTable = "CREATE TABLE " +
            TABLE_PRODUCT +
            "(product_id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", product_category TEXT" +
            ", product_color_code CAHR(6)" +
            ", product_design TEXT" +
            ", product_type TEXT" +
            ", product_comment TEXT" +
            ", product_recommended_size REAL" +
            ", product_height REAL" +
            ", product_width REAL)";

    String createProductImageTable = "CREATE TABLE " +
            TABLE_PRODUCT_IMAGE +
            "(product_image_id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", product_image_path TEXT" +
            ", product_id INTEGER" +
            ", FOREIGN KEY (product_id) REFERENCES " + TABLE_PRODUCT + "(product_id))";

    String createPlaceTable = "CREATE TABLE " +
            TABLE_PLACE +
            "(place_id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", place_name TEXT" +
            ", place_delete_flag TINYINT(1)" +
            ", product_id INTEGER" +
            ", FOREIGN KEY (product_id) REFERENCES " + TABLE_PRODUCT + "(product_id))";

    db.execSQL(createProductTable);
    db.execSQL(createProductImageTable);
    db.execSQL(createPlaceTable);

    // TestData
    // 一つ目
    db.execSQL("INSERT INTO product ('product_category','product_color_code','product_design','product_type','product_comment','product_recommended_size','product_height','product_width') VALUES('カーテン','hoge','hogehoge','両開き','コメント',100.0,1200,900)");
    db.execSQL("INSERT INTO place ('place_name','place_delete_flag','product_id') VALUES('子供部屋',0,1)");
    db.execSQL("INSERT INTO product_image ('product_image_path','product_id') VALUES ('hoge_path',1)");
    // 二つ目
    db.execSQL("INSERT INTO product ('product_category','product_color_code','product_design','product_type','product_comment','product_recommended_size','product_height','product_width') VALUES('カーテン','hoge','hogehoge','両開き','コメント',100.0,1200,900)");
    db.execSQL("INSERT INTO place ('place_name','place_delete_flag','product_id') VALUES('子供部屋',0,2)");
    db.execSQL("INSERT INTO product_image ('product_image_path','product_id') VALUES ('hoge_path',2)");
    // 三つ目
    db.execSQL("INSERT INTO product ('product_category','product_color_code','product_design','product_type','product_comment','product_recommended_size','product_height','product_width') VALUES('カーテン','hoge','hogehoge','両開き','コメント',100.0,1200,900)");
    db.execSQL("INSERT INTO place ('place_name','place_delete_flag','product_id') VALUES('子供部屋',0,3)");
    db.execSQL("INSERT INTO product_image ('product_image_path','product_id') VALUES ('hoge_path',3)");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int i, int i1) {

  }

  // region SQLite SELECT statement

  // Get Data Display In CardView
  public ArrayList<PittanProductDataModel> getSelectCardData(){
    ArrayList<PittanProductDataModel> arrayList = new ArrayList<>();
    String selectAllSql = "SELECT place_name,product_height,product_width,product_category,product_image_path,place_id FROM product " +
            "LEFT OUTER JOIN place ON product.product_id = place.product_id " +
            "LEFT OUTER JOIN product_image ON product.product_id = product_image.product_id " +
            "WHERE place_delete_flag = 0";

    SQLiteDatabase db = getReadableDatabase();
    if (db == null){
      return null;
    }

    try {
      @SuppressLint("Recycle")
      Cursor cursor = db.rawQuery(selectAllSql,null);
      while (cursor.moveToNext()){
        PittanProductDataModel temps = new PittanProductDataModel();
        temps.setPlaceName(cursor.getString(0));
        temps.setProductHeight(cursor.getFloat(1));
        temps.setProductWidth(cursor.getFloat(2));
        temps.setProductCategory(cursor.getString(3));
        temps.setProductImagePath(cursor.getString(4));
        temps.setPlaceID(cursor.getInt(5));

        arrayList.add(temps);
      }

    } catch (SQLiteException e){
      e.printStackTrace();
    } finally {
      db.close();
    }

    return arrayList;
  }

  public ArrayList<PittanProductDataModel> getSelectDetailData(int placeID){
    ArrayList<PittanProductDataModel> ary = new ArrayList<>();
    String selectDetailItemSql = "SELECT place_name,product_height,product_width,product_category,product_comment,product_image_path,place_id FROM product " +
            "LEFT OUTER JOIN place ON product.product_id = place.product_id " +
            "LEFT OUTER JOIN product_image ON product.product_id = product_image.product_id " +
            "WHERE place_id = " + placeID;

    SQLiteDatabase db = getReadableDatabase();
    if (db == null){
      return null;
    }

    try {
      @SuppressLint("Recycle")
      Cursor cursor = db.rawQuery(selectDetailItemSql,null);
      while (cursor.moveToNext()){
        PittanProductDataModel tmp = new PittanProductDataModel();
        tmp.setPlaceName(cursor.getString(0));
        tmp.setProductHeight(cursor.getFloat(1));
        tmp.setProductWidth(cursor.getFloat(2));
        tmp.setProductCategory(cursor.getString(3));
        tmp.setProductComment(cursor.getString(4));
        tmp.setProductImagePath(cursor.getString(5));
        ary.add(tmp);
      }
    } catch (SQLiteException e){
      e.printStackTrace();
    } finally {
      db.close();
    }
    return ary;
  }

  // endregion

  //region SQLite INSERT statement
  // Insert　product TABLE
  public boolean insertProductData(PittanProductDataModel item) {
    ContentValues contentValues = new ContentValues();
    contentValues.put("product_category", item.getProductCategory());
    contentValues.put("product_color_code", item.getProductColorCode());
    contentValues.put("product_design", item.getProductDesign());
    contentValues.put("product_type", item.getProductType());
    contentValues.put("product_comment", item.getProductComment());
    contentValues.put("product_recommended_size", item.getProductComment());
    contentValues.put("product_height", item.getProductHeight());
    contentValues.put("product_width", item.getProductWidth());

    SQLiteDatabase db = getWritableDatabase();
    long ret;
    boolean isSuccess = false;
    try {
      ret = db.insert(TABLE_PRODUCT, null, contentValues);
      if (ret > 0) {
        isSuccess = isInsertPlaceData(item, ret);
        isSuccess = isInsertProductImageData(item, ret);
      }
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      db.close();
    }

    return isSuccess;
  }

  // Insert place TABLE
  public boolean isInsertPlaceData(PittanProductDataModel item, long productID) {
    ContentValues contentValues = new ContentValues();
    contentValues.put("place_name", item.getPlaceName());
    contentValues.put("place_delete_flag", item.getPlaceDeleteFlag());
    contentValues.put("product_id", productID);

    SQLiteDatabase db = getWritableDatabase();
    long ret = -1;

    try {

      ret = db.insert(TABLE_PLACE, null, contentValues);
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      db.close();
    }

    return ret > 0;
  }

  // Insert　product_image TABLE
  public boolean isInsertProductImageData(PittanProductDataModel item, long productID) {
    ContentValues contentValues = new ContentValues();
    contentValues.put("product_image_path", item.getProductImagePath());
    contentValues.put("product_id", productID);
    SQLiteDatabase db = getWritableDatabase();
    long ret = -1;

    try {
      ret = db.insert(TABLE_PRODUCT_IMAGE, null, contentValues);
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      db.close();
    }

    return ret > 0;
  }

  // Update place TABLE
  public boolean isUpdatePlaceTable(String placeID){
    ContentValues contentValues = new ContentValues();
    contentValues.put("place_delete_flag",1);

    SQLiteDatabase db = getWritableDatabase();
    long ret = -1;

    try {
      ret = db.update(TABLE_PLACE,contentValues,"place_id = " + placeID,null);
      Log.i(TAG, "isUpdatePlaceTable: "+ret);
    } catch (SQLiteException e){
      e.printStackTrace();
    } finally {
      db.close();
    }
    return ret > 0;
  }
  // endregion
}
