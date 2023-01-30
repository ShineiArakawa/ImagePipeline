package ImagePipeline.model.modules;

import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import ImagePipeline.model.primitives.ConfigPrimitive;
import ImagePipeline.model.primitives.ImagePrimitive;
import ImagePipeline.model.primitives.IntConfig;
import ImagePipeline.model.primitives.ModulePrimitive;

public class Resize extends ModulePrimitive {
    private int _interpolation = Imgproc.INTER_AREA;

    public static final String MODULE_NAME = "Resize";
    public static final String KEY_PRE = "config." + MODULE_NAME + ".";

    public static final String KEY_CONFIG_WIDTH = KEY_PRE + "width";
    public static final String KEY_CONFIG_HEIGHT = KEY_PRE + "height";

    public Resize() {
        super();
    }

    public void setIntepolationMethod(int method) {
        _interpolation = method;
    }

    @Override
    public ImagePrimitive applyEach(ImagePrimitive image) {
        int targetWidth = _config.get(KEY_CONFIG_WIDTH).getValueInt();
        int targetHeight = _config.get(KEY_CONFIG_HEIGHT).getValueInt();

        Mat mat_image = image.getImage();
        Mat mat_newImage = new Mat();
        Imgproc.resize(mat_image, mat_newImage, new Size(targetWidth, targetHeight), 0.0, 0.0, _interpolation);
        image.setImage(mat_newImage);
        return image;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public HashMap<String, ConfigPrimitive> getDefaultConfig() {
        HashMap<String, ConfigPrimitive> config = new HashMap<String, ConfigPrimitive>();

        IntConfig width = new IntConfig(0);
        config.put(KEY_CONFIG_WIDTH, width);

        IntConfig height = new IntConfig(0);
        config.put(KEY_CONFIG_HEIGHT, height);

        return config;
    }

}
