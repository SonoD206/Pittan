package jp.ac.jec.cm0120.pittan.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.ac.jec.cm0120.pittan.app.AppConstant;
import jp.ac.jec.cm0120.pittan.app.AppLog;

public class PictureIO {

  public static void saveBitmapToDisk(Bitmap mBitmap, String filename) throws IOException {
    File out = new File(filename);
    if (!out.getParentFile().exists()) {
      out.getParentFile().mkdirs();
    }
    try (FileOutputStream outputStream = new FileOutputStream(filename);
         ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
      mBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
      outputData.writeTo(outputStream);
      outputStream.flush();
    } catch (IOException ex) {
      throw new IOException("Failed to save bitmap to disk", ex);
    }
  }
  public static Bitmap outputPicture(String filename) {
    Bitmap bitmap = null;
    if (isExternalStorageReadable()) {
      try {
        InputStream stream = new FileInputStream(filename);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);
        bitmap = BitmapFactory.decodeStream(bufferedInputStream);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    return bitmap;
  }

  private static boolean isExternalStorageReadable() {
    String state = Environment.getExternalStorageState();
    return (Environment.MEDIA_MOUNTED.equals(state) ||
            Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
  }
}
