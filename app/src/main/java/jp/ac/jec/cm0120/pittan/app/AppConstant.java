package jp.ac.jec.cm0120.pittan.app;

public final class AppConstant {
  private AppConstant() {
  }

  public static final String EXTRA_TRANSITION_NAME = "TransitionName";

  public static class Log {
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
  }

  public static class Home {
    public static final String EXTRA_PLACE_ID = "placeID";
    public static final String EXTRA_TRANSITION_TAG = "transitionNum";
  }

  public static class Detail {
    public static final String EXTRA_MODEL = "PittanProductDataModel";
    public static final String ACTIVITY_NAME = "Detail";
  }

  public static class Objection {
    public static final int MODEL_NUM = 1;
    public static final int TEXTURE_NUM = 2;
    public static final int FRAGMENT_PAGE_NUM = 2;
    public static final String ACTIVITY_NAME = "Objection";
    public static final String EXTRA_IMAGE_TEMP_FILE_PATH = "imageTempPath";
    public static final String EXTRA_IMAGE_FILE_PATH = "imagePath";
    public static final String FIRST_MODEL = "white_curtain.glb";
    public static final String BASE_COLOR_INDEX = "baseColorIndex";
    public static final String BASE_COLOR_MAP = "baseColorMap";
    public static final String HANDLER_THREAD_NAME = "PixelCopier";
    public static final String DATE_FORMAT_PATTERN = "yyyy_MM_dd_HHmm";
    public static final String TEMP_PICTURE_NAME = "temp.jpg";
    public static final String ALERT_MESSAGE_FORMAT = "縦幅：%scm\n" + "横幅：%scm";
    public static final String USER_PICTURE_NAME_FORMAT_PATTERN = "Pittan/%s_3dModel.jpg";
    public static final String MODELS_PATH_FORMAT_PATTERN = "models/%s";
    public static final String TEXTURES_PATH_FORMAT_PATTERN = "textures/%s";
    public static final String ASSETS_FILE_NAME = "models";
    public static final String DATA_SIZE_FORMAT = "%scm";
    public static final String CHANGE_HEIGHT = "height";
    public static final String CHANGE_WIDTH = "width";
    public static final String EXTRA_MODEL_SIZE_WIDTH = "widthSize";
    public static final String EXTRA_MODEL_SIZE_HEIGHT = "heightSize";
  }
}
