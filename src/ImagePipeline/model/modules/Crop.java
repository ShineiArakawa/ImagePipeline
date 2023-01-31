package ImagePipeline.model.modules;

import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import ImagePipeline.model.primitives.ConfigPrimitive;
import ImagePipeline.model.primitives.ImagePrimitive;
import ImagePipeline.model.primitives.IntConfig;
import ImagePipeline.model.primitives.ModulePrimitive;

public class Crop extends ModulePrimitive {
    public static final String MODULE_NAME = "Crop";
    public static final String KEY_PRE = "config." + MODULE_NAME + ".";

    public static final String KEY_CONFIG_WIDTH = KEY_PRE + "width";
    public static final String KEY_CONFIG_HEIGHT = KEY_PRE + "height";
    public static final String KEY_CONFIG_LEFT_TOP_X = KEY_PRE + "PosLeftTop_X";
    public static final String KEY_CONFIG_LEFT_TOP_Y = KEY_PRE + "PosLeftTop_Y";

    @Override
    public ImagePrimitive applyEach(ImagePrimitive image) {
        int cropWidth = _config.get(KEY_CONFIG_WIDTH).getValueInt();
        int cropHeight = _config.get(KEY_CONFIG_HEIGHT).getValueInt();
        int leftTopX = _config.get(KEY_CONFIG_LEFT_TOP_X).getValueInt();
        int leftTopY = _config.get(KEY_CONFIG_LEFT_TOP_Y).getValueInt();

        Mat mat_image = image.getImage();
        Rect rect = new Rect(leftTopX, leftTopY, cropWidth, cropHeight);
        Mat cropped = new Mat(mat_image, rect);

        image.setImage(cropped);

        return image;
    }

    @Override
    protected HashMap<String, ConfigPrimitive> getDefaultConfig() {
        HashMap<String, ConfigPrimitive> config = new HashMap<String, ConfigPrimitive>();

        IntConfig width = new IntConfig(0);
        config.put(KEY_CONFIG_WIDTH, width);

        IntConfig height = new IntConfig(0);
        config.put(KEY_CONFIG_HEIGHT, height);

        IntConfig posLeftTopX = new IntConfig(0);
        config.put(KEY_CONFIG_LEFT_TOP_X, posLeftTopX);

        IntConfig posLeftTopY = new IntConfig(0);
        config.put(KEY_CONFIG_LEFT_TOP_Y, posLeftTopY);

        return config;
    }

    @Override
    public String getModuleName() {
        // TODO Auto-generated method stub
        return MODULE_NAME;
    }

}
