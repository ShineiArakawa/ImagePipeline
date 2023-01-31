package ImagePipeline.resource;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ���\�[�X�t�@�C���̊Ǘ����s�����\�b�h�ł���B
 * 
 * @author araka
 *
 */
public class ResourceManager {
  //@formatter:off
  /**
   * ���\�[�X�t�@�C����
   */
  private static final String NAME_RESOURCE      = "resource";
  /**
   * �r���h�ԍ����������t�@�C����
   */
  private static final String NAME_BUILD_NUMBER  = "buildNumber";
  /**
   * ���[�U�[���ǋL�ł��郊�\�[�X�t�@�C��
   */
  private static final String NAME_USER_DEF      = "resource_user_def";
  //@formatter:on
    /**
     * �V���O���g���̂��߂̃I�u�W�F�N�g�B
     */
    private static ResourceManager _instance;
    /**
     * ���\�[�X�̓ǂݍ��݁E�Ǘ����s���I�u�W�F�N�g(�r���h�ԍ��p)
     */
    private ResourceBundle _bundleBuildNumber;
    /**
     * ���\�[�X�̓ǂݍ��݁E�Ǘ����s���I�u�W�F�N�g
     */
    private ResourceBundle _bundle;
    /**
     * ���[�U�[���ǋL�ł��郊�\�[�X�I�u�W�F�N�g
     */
    private ResourceBundle _bundleUserDef;

    /**
     * ���O�n���h�����O�N���X
     */
    private Logger _logger;

    /**
     * �R���X�g���N�^�B<br>
     * �V���O���g���̂��߂ɁA�O������ĂׂȂ��悤�ɂ��Ă���
     * ���[�U�[���쐬�����\�[�X�t�@�C��������ǂݍ��ނ��Ƃ��ł���
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
     * ResourceManager�I�u�W�F�N�g��Ԃ��B���߂Ď擾����ۂɂ̓I�u�W�F�N�g�̐������s��
     * 
     * @return ResourceManager�̃V���O���g���I�u�W�F�N�g�B
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
     * �����̃L�[������ɑΉ����郊�\�[�X�������Ԃ��B
     * �L�[������ɑΉ����郊�\�[�X�����݂��Ȃ��ꍇ�́A�L�[�����񂻂̂��̂�Ԃ��B
     * 
     * @param key �L�[������B
     * @return �����̕�������L�[�Ƃ��郊�\�[�X������B
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
