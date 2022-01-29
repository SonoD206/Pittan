package jp.ac.jec.cm0120.pittan.ui.objectInstallation;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.lang.ref.WeakReference;
import java.util.function.Consumer;
import java.util.function.Function;

import jp.ac.jec.cm0120.pittan.R;
import jp.ac.jec.cm0120.pittan.ui.objectInstallation.product_menu.ProductMenuFragment;

public class ObjectInstallationActivity extends AppCompatActivity implements FragmentOnAttachListener, BaseArFragment.OnTapArPlaneListener, BaseArFragment.OnSessionConfigurationListener, ArFragment.OnViewCreatedListener, ProductMenuFragment.OnClickRecyclerViewListener {

  /// Constants
  private static final int MODEL_NUM = 1;
  private static final int TEXTURE_NUM = 2;
  private static final String TAG = "###";
  private static final String FIRST_MODEL = "white_curtain.glb";
  private static final String BASE_COLOR_INDEX = "baseColorIndex";
  private static final String BASE_COLOR_MAP = "baseColorMap";
  private static final String TEXTURES_PATH_HEADER = "textures/";
  private static final String MODELS_PATH_HEADER = "models/" ;

  /// Components
  private TabLayout mTabLayout;
  private ViewPager2 mViewPager2;
  private ImageButton imageButtonClose;
  private ImageButton imageButtonDelete;
  private ImageButton imageButtonShutter;
  private ArFragment arFragment;
  private View photoPreview;
  private Button saveButton;

  /// Fields
  private BottomMenuAdapter bottomMenuAdapter;

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
    photoPreview = findViewById(R.id.view_preview);
    saveButton = photoPreview.findViewById(R.id.button_save_photo);

    getSupportFragmentManager().addFragmentOnAttachListener(this);

    if (savedInstanceState == null) {
      if (Sceneform.isSupported(this)) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.arFragment, ArFragment.class, null)
                .commit();
      }
    }

    loadModels(getPath(MODEL_NUM, FIRST_MODEL));
  }

  private void setListener() {

    imageButtonClose.setOnClickListener(view -> {
      finish();
    });

    imageButtonDelete.setOnClickListener(view -> {
    });

    saveButton.setOnClickListener(view -> {
      // TODO: 2022/01/29 SQLiteにファイル名を格納
    });

    imageButtonShutter.setOnClickListener(view -> {
      photoPreview.setVisibility(View.VISIBLE);
      // TODO: 2022/01/29 写真を撮る & 画像を表示させる
    });

  }

  private void buildViewPager2() {
    bottomMenuAdapter = new BottomMenuAdapter(this);
    mViewPager2.setUserInputEnabled(false);
    mViewPager2.setAdapter(bottomMenuAdapter);

    new TabLayoutMediator(mTabLayout, mViewPager2,
            (tab, position) -> {

              switch (position) {
                case 0:
                  tab.setText("設置");
                  break;
                case 1:
                  tab.setText("サイズ");
                  break;
                default:
                  tab.setText("");
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
      Log.i("###", "onTapPlane: model Size" + mModel.getLocalScale());

      mModel.setParent(anchorNode);

      ///切り分け
      if (texture != null) {
        RenderableInstance modelInstance = mModel.setRenderable(this.mRenderModel);
        modelInstance.getMaterial().setInt(BASE_COLOR_INDEX, 0);
        modelInstance.getMaterial().setTexture(BASE_COLOR_MAP, texture);
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
            .thenAccept(new Consumer<ModelRenderable>() {
              @Override
              public void accept(ModelRenderable model) {
                ObjectInstallationActivity activity = weakActivity.get();
                if (activity != null) {
                  activity.mRenderModel = model;
                }
              }
            })
            .exceptionally(new Function<Throwable, Void>() {
              @Override
              public Void apply(Throwable throwable) {
                Toast.makeText(ObjectInstallationActivity.this, "Unable to load model", Toast.LENGTH_LONG).show();
                return null;
              }
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
    StringBuilder builder = new StringBuilder();

    if (kind == modelNum) {
      builder.append(MODELS_PATH_HEADER)
              .append(name);
      path = builder.toString();
    } else if (kind == textureNum) {
      builder.append(TEXTURES_PATH_HEADER)
              .append(name);
      path = builder.toString();
    } else {
      Log.i(TAG, "getUri: Not the right kind.");
    }
    return path;
  }

  @Override
  public void onClickRecyclerItem(String textureName) {
    String path = getPath(TEXTURE_NUM, textureName);
    loadTexture(path);
  }
}