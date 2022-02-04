package jp.ac.jec.cm0120.pittan.ui.objectInstallation;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.Sceneform;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.RenderableInstance;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.gorisse.thomas.sceneform.ArSceneViewKt;
import com.gorisse.thomas.sceneform.light.LightEstimationConfig;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.ac.jec.cm0120.pittan.R;
import jp.ac.jec.cm0120.pittan.app.AppConstant;
import jp.ac.jec.cm0120.pittan.app.AppLog;
import jp.ac.jec.cm0120.pittan.ui.add_data.AddDataActivity;
import jp.ac.jec.cm0120.pittan.ui.objectInstallation.product_menu.ProductMenuFragment;
import jp.ac.jec.cm0120.pittan.util.PictureIO;

public class ObjectInstallationActivity extends AppCompatActivity implements FragmentOnAttachListener, BaseArFragment.OnTapArPlaneListener, BaseArFragment.OnSessionConfigurationListener, ArFragment.OnViewCreatedListener, ProductMenuFragment.OnClickRecyclerViewListener {

  /// Components
  private TabLayout mTabLayout;
  private ViewPager2 mViewPager2;
  private ImageButton imageButtonClose;
  private ImageButton imageButtonDelete;
  private ImageButton imageButtonShutter;
  private ArFragment arFragment;
  private View viewPhotoPreview;
  private Button buttonPhotoSave;
  private ImageView imagePhotoPreview;

  /// Fields
  private String userPhotoFileName;
  private Bitmap mPreviewBitmap;
  private Intent mIntent;
  private final float[] mModelScales = new float[3];
  private int transitionNum;

  /// ARCore
  private Renderable mRenderModel;
  private TransformableNode mModel;
  private AnchorNode anchorNode;
  private Texture texture;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_object_installation);

    initialize(savedInstanceState);
    buildViewPager2();
    setListener();
  }

  private void initialize(Bundle savedInstanceState) {

    mTabLayout = findViewById(R.id.tab_menu_category);
    mViewPager2 = findViewById(R.id.view_pager2_menu_item);
    imageButtonClose = findViewById(R.id.image_button_close);
    imageButtonDelete = findViewById(R.id.image_button_delete);
    imageButtonShutter = findViewById(R.id.image_button_shutter);
    viewPhotoPreview = findViewById(R.id.view_preview);
    buttonPhotoSave = viewPhotoPreview.findViewById(R.id.button_save_photo);
    imagePhotoPreview = viewPhotoPreview.findViewById(R.id.image_view_photo);

    getSupportFragmentManager().addFragmentOnAttachListener(this);
    setTransitionNum();
    if (savedInstanceState == null) {
      if (Sceneform.isSupported(this)) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.arFragment, ArFragment.class, null)
                .commit();
      }
    }

    loadModels(getPath(AppConstant.Objection.MODEL_NUM, AppConstant.Objection.FIRST_MODEL));
  }

  private void setTransitionNum() {
    mIntent = getIntent();
    transitionNum = mIntent.getIntExtra(AppConstant.Home.EXTRA_TRANSITION_TAG, 0);
    mIntent = null;
  }

  private void setListener() {

    imageButtonClose.setOnClickListener(view -> finish());

    imageButtonDelete.setOnClickListener(view -> {
    });

    buttonPhotoSave.setOnClickListener(view -> showAlertDialog());

    imageButtonShutter.setOnClickListener(view -> {
      viewPhotoPreview.setVisibility(View.VISIBLE);
      takePhoto();
      new Handler().postDelayed(() -> imagePhotoPreview.setImageBitmap(mPreviewBitmap), 1000);
    });

  }

  private void buildViewPager2() {
    /// Fields
    BottomMenuAdapter bottomMenuAdapter = new BottomMenuAdapter(this);
    mViewPager2.setUserInputEnabled(false);
    mViewPager2.setAdapter(bottomMenuAdapter);

    new TabLayoutMediator(mTabLayout, mViewPager2,
            (tab, position) -> {
              switch (position) {
                case 0:
                  tab.setText(getString(R.string.object_installation_tab_title_first));
                  break;
                case 1:
                  tab.setText(getString(R.string.object_installation_tab_title_second));
                  break;
                default:
                  break;
              }
            }
    ).attach();
  }

  @Override
  public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
    if (fragment.getId() == R.id.arFragment) {
      arFragment = (ArFragment) fragment;
      arFragment.setOnSessionConfigurationListener(this);
      arFragment.setOnViewCreatedListener(this);
      arFragment.setOnTapArPlaneListener(this);
    }
  }

  @Override
  public void onViewCreated(ArSceneView arSceneView) {
    arFragment.setOnViewCreatedListener(null);
    // Fine adjust the maximum frame rate
    arSceneView.setFrameRateFactor(SceneView.FrameRate.FULL);
  }

  @Override
  public void onSessionConfiguration(Session session, Config config) {
    if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
      config.setDepthMode(Config.DepthMode.AUTOMATIC);
    }
  }

  @Override
  public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
    if (mRenderModel == null) {
      Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
      return;
    }

    ArSceneViewKt.setLightEstimationConfig(arFragment.getArSceneView(), LightEstimationConfig.DISABLED);

    /// 垂直面と平面の分岐
    if (plane.getType().equals(Plane.Type.HORIZONTAL_UPWARD_FACING) && mModel == null) {
      // Create the Anchor.
      Anchor anchor = hitResult.createAnchor();
      anchorNode = new AnchorNode(anchor);
      anchorNode.setParent(arFragment.getArSceneView().getScene());

      // Create the transformable model and add it to the anchor;
      mModel = new TransformableNode(arFragment.getTransformationSystem());
      mModel.getScaleController().setMaxScale(5.0f);
      mModel.getScaleController().setMinScale(0.01f);

      /// ここを変えたら最初のポジションが変わる
      mModel.setLocalPosition(new Vector3(0.0f, 0.0f, 0.0f));

      /// ここを変えたら最初の大きさが変わる
      mModel.setLocalScale(new Vector3(0.3f, 0.3f, 0.3f));

      /// モデルのサイズ
      mModelScales[0] = mModel.getLocalScale().x;
      mModelScales[1] = mModel.getLocalScale().y;
      mModelScales[2] = mModel.getLocalScale().z;

      mModel.setParent(anchorNode);

      ///切り分け
      if (texture != null) {
        RenderableInstance modelInstance = mModel.setRenderable(this.mRenderModel);
        modelInstance.getMaterial().setInt(AppConstant.Objection.BASE_COLOR_INDEX, 0);
        modelInstance.getMaterial().setTexture(AppConstant.Objection.BASE_COLOR_MAP, texture);
      } else {
        mModel.setRenderable(mRenderModel);
      }
      mModel.select();
    }
  }

  /// 3Dモデルのロード
  private void loadModels(String modelPath) {
    WeakReference<ObjectInstallationActivity> weakActivity = new WeakReference<>(this);
    ModelRenderable.builder()
            .setSource(this, Uri.parse(modelPath))
            .setIsFilamentGltf(true)
            .setAsyncLoadEnabled(true)
            .build()
            .thenAccept(model -> {
              ObjectInstallationActivity activity = weakActivity.get();
              if (activity != null) {
                activity.mRenderModel = model;
              }
            })
            .exceptionally(throwable -> {
              Toast.makeText(ObjectInstallationActivity.this, "Unable to load model", Toast.LENGTH_LONG).show();
              return null;
            });
  }

  /// Textureのロード
  private void loadTexture(String texturePath) {
    WeakReference<ObjectInstallationActivity> weakActivity = new WeakReference<>(this);
    Texture.builder()
            .setSampler(Texture.Sampler.builder()
                    .setMinFilter(Texture.Sampler.MinFilter.LINEAR_MIPMAP_LINEAR)
                    .setMagFilter(Texture.Sampler.MagFilter.LINEAR)
                    .setWrapMode(Texture.Sampler.WrapMode.REPEAT)
                    .build())
            .setSource(this, Uri.parse(texturePath))
            .setUsage(Texture.Usage.COLOR_MAP)
            .build()
            .thenAccept(
                    texture -> {
                      ObjectInstallationActivity activity = weakActivity.get();
                      if (activity != null) {
                        activity.texture = texture;
                      }
                    })
            .exceptionally(
                    throwable -> {
                      Toast.makeText(this, "Unable to load texture", Toast.LENGTH_LONG).show();
                      return null;
                    });
  }

  /// PATHの生成
  private String getPath(int kind, String name) {
    final int modelNum = 1;
    final int textureNum = 2;
    String path = "";
    if (kind == modelNum) {
      path = String.format(AppConstant.Objection.MODELS_PATH_FORMAT_PATTERN,name);
    } else if (kind == textureNum) {
      path = String.format(AppConstant.Objection.TEXTURES_PATH_FORMAT_PATTERN,name);
    } else {
      AppLog.info( "Not the right kind.");
    }
    return path;
  }

  /// Interfaceの実装
  @Override
  public void onClickRecyclerItem(String textureName) {
    String path = getPath(AppConstant.Objection.TEXTURE_NUM, textureName);
    loadTexture(path);
  }

  ///写真を撮る
  private void takePhoto() {
    new Thread(() -> {
      userPhotoFileName = generateFilename(true);
      ArSceneView view = arFragment.getArSceneView();
      if (mPreviewBitmap == null) {
        // Reduce size of the resulting Bitmap
        mPreviewBitmap = Bitmap.createBitmap(
                view.getWidth(),
                view.getHeight(),
                Bitmap.Config.RGB_565);
      }
      // Create a handler thread to offload the processing of the image.
      final HandlerThread handlerThread = new HandlerThread(AppConstant.Objection.HANDLER_THREAD_NAME);
      handlerThread.start();
      // Make the request to copy.
      PixelCopy.request(view, mPreviewBitmap, (copyResult) -> {
        if (copyResult == PixelCopy.SUCCESS) {
          try {
            PictureIO.saveBitmapToDisk(mPreviewBitmap,generateFilename(false));
          } catch (IOException e) {
            e.printStackTrace();
          }
        } else {
          new Handler().post(() -> {
            Toast toast = Toast.makeText(this,
                    "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
            toast.show();
          });
        }
        handlerThread.quitSafely();
      }, new Handler(handlerThread.getLooper()));
    }).start();
  }

  private String generateFilename(boolean isPhoto) {
    String pathHeader = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator;
    String fileName = pathHeader + AppConstant.Objection.TEMP_PICTURE_NAME;
    if (isPhoto){
      String date = new SimpleDateFormat(AppConstant.Objection.DATE_FORMAT_PATTERN, java.util.Locale.getDefault()).format(new Date());
      fileName = pathHeader + String.format(AppConstant.Objection.USER_PICTURE_NAME_FORMAT_PATTERN,date);
    }
    return fileName;
  }

  private void showAlertDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(getString(R.string.object_installation_alert_title))
            .setMessage(String.format(AppConstant.Objection.ALERT_MESSAGE_FORMAT,mModelScales[0],mModelScales[1]))
            .setPositiveButton(getString(R.string.ok), null)
            .setNegativeButton(getString(R.string.cancel), null);
    AlertDialog dialog = builder.show();

    Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
    positiveButton.setOnClickListener(view -> judgeOriginalTransition(transitionNum, dialog));

    Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
    negativeButton.setOnClickListener(view -> {
      dialog.dismiss();
      viewPhotoPreview.setVisibility(View.INVISIBLE);
    });
  }

  private void judgeOriginalTransition(int transitionNum, AlertDialog dialog) {
    if (transitionNum == 0) {
      mIntent = new Intent(this, AddDataActivity.class);
      mIntent.putExtra(AppConstant.Objection.EXTRA_IMAGE_TEMP_FILE_PATH, generateFilename(false));
      mIntent.putExtra(AppConstant.Objection.EXTRA_IMAGE_FILE_PATH, userPhotoFileName);
      mIntent.putExtra(AppConstant.EXTRA_TRANSITION_NAME, AppConstant.Objection.ACTIVITY_NAME);
      startActivity(mIntent);
      dialog.dismiss();
    } else if (transitionNum == 1) {
      mIntent = getIntent();
      mIntent.putExtra(AppConstant.Objection.EXTRA_IMAGE_TEMP_FILE_PATH, generateFilename(false));
      mIntent.putExtra(AppConstant.Objection.EXTRA_IMAGE_FILE_PATH, userPhotoFileName);
      setResult(RESULT_OK, mIntent);
      dialog.dismiss();
      finish();
    } else {
      AppLog.info("You transitioned from an unexpected screen.");
    }
  }
}