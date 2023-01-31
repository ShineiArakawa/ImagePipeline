package ImagePipeline;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;

import ImagePipeline.control.MainControl;

public class ImagePipelineMain {
    public static void main(String[] args) {
        new ImagePipelineMain(args);
    }

    public ImagePipelineMain(String[] args) {
        FlatIntelliJLaf.setup();
        MainControl mainControl = new MainControl(args);
        mainControl.start();
    }
}
