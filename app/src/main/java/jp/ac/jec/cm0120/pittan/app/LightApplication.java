package jp.ac.jec.cm0120.pittan.app;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class LightApplication extends Application {
  static {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
  }
}
