package ImagePipeline;

import javax.swing.UIManager;

import org.opencv.core.Core;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatSolarizedDarkIJTheme;

import ImagePipeline.control.MainControl;

public class ImagePipelineMain {
    public static void main(String[] args) {
        new ImagePipelineMain(args);
    }

    public ImagePipelineMain(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatSolarizedDarkIJTheme());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        } catch (Exception ex) {
            System.err.println("Failed to link OpenCV");
        }

        MainControl mainControl = new MainControl(args);
        mainControl.start();
    }
}
