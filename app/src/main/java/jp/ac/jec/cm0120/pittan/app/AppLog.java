package jp.ac.jec.cm0120.pittan.app;

import android.util.Log;

public class AppLog {

  public static final int INDEX = 4;
  public static final String TAG = "###";
  public static final String FORMAT = "[%s.%s()] %s";

  private static String getText(String message) {
    StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[INDEX];
    String text = String.format(FORMAT,
            stackTrace.getClassName(), stackTrace.getMethodName(), message);
    return text;
  }
  public static void info(String message) {
    Log.i(TAG, getText(message));
  }
}
