package ImagePipeline.resource;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * リソースファイルの管理を行うメソッドである。
 * 
 * @author araka
 *
 */
public class ResourceManager {
  //@formatter:off
  /**
   * リソースファイル名
   */
  private static final String NAME_RESOURCE      = "resource";
  /**
   * ビルド番号を書いたファイル名
   */
  private static final String NAME_BUILD_NUMBER  = "buildNumber";
  /**
   * ユーザーが追記できるリソースファイル
   */
  private static final String NAME_USER_DEF      = "resource_user_def";
  //@formatter:on
    /**
     * シングルトンのためのオブジェクト。
     */
    private static ResourceManager _instance;
    /**
     * リソースの読み込み・管理を行うオブジェクト(ビルド番号用)
     */
    private ResourceBundle _bundleBuildNumber;
    /**
     * リソースの読み込み・管理を行うオブジェクト
     */
    private ResourceBundle _bundle;
    /**
     * ユーザーが追記できるリソースオブジェクト
     */
    private ResourceBundle _bundleUserDef;

    /**
     * ログハンドリングクラス
     */
    private Logger _logger;

    /**
     * コンストラクタ。<br>
     * シングルトンのために、外部から呼べないようにしている
     * ユーザーが作成したソースファイルからも読み込むことができる
     */
    private ResourceManager() {
        _logger = Logger.getLogger(getClass().getName());
        _logger.setLevel(Level.INFO);
        Locale locale = Locale.getDefault();

        String packageName = getClass().getPackage().getName();
        String pathResource = packageName + "." + NAME_RESOURCE;
        String pathBuildNo = packageName + "." + NAME_BUILD_NUMBER;
        String pathResourceUserDef = NAME_USER_DEF;

        try {
            _bundle = ResourceBundle.getBundle(pathResource, locale);
        } catch (Exception e) {
            _logger.warning("Bundle not found for base name " + pathResource + " , locale " + locale);
        }
        try {
            _bundleBuildNumber = ResourceBundle.getBundle(pathBuildNo, locale);
        } catch (Exception e) {
            _logger.warning("Bundle not found for base name " + pathBuildNo + " , locale " + locale);
        }
        try {
            String currentDir = System.getProperty("user.dir");
            File dirUserResource = new File(currentDir);
            URL[] urls = { dirUserResource.toURI().toURL() };
            ClassLoader classLoader = new URLClassLoader(urls);
            _bundleUserDef = ResourceBundle.getBundle(pathResourceUserDef, locale, classLoader);
        } catch (Exception e) {
            _logger.warning("Bundle not found for base name " + pathResourceUserDef + " , locale " + locale);
        }
    }

    /**
     * ResourceManagerオブジェクトを返す。初めて取得する際にはオブジェクトの生成も行う
     * 
     * @return ResourceManagerのシングルトンオブジェクト。
     */
    public static ResourceManager getInstance() {
        if (_instance == null) {
            try {
                _instance = new ResourceManager();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return _instance;
    }

    /**
     * 引数のキー文字列に対応するリソース文字列を返す。
     * キー文字列に対応するリソースが存在しない場合は、キー文字列そのものを返す。
     * 
     * @param key キー文字列。
     * @return 引数の文字列をキーとするリソース文字列。
     */
    public String getString(String key) {
        String value = null;
        if (key == null) {
            value = "";
        } else {
            boolean flgExistValue = false;
            if (flgExistValue == false) {
                try {
                    value = _bundleBuildNumber.getString(key);
                    flgExistValue = true;
                } catch (Exception e) {
                    flgExistValue = false;
                }
            }
            if (flgExistValue == false) {
                try {
                    value = _bundle.getString(key);
                    flgExistValue = true;
                } catch (Exception e) {
                    flgExistValue = false;
                }
            }
            if (flgExistValue == false) {
                try {
                    value = _bundleUserDef.getString(key);
                    flgExistValue = true;
                } catch (Exception e) {
                    flgExistValue = false;
                }
            }
            if (flgExistValue == false) {
                if (value == null) {
                    value = key;
                }
            }
        }
        return value;
    }
}
