package ImagePipeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.UnsatisfiedLinkError;
import javax.swing.UIManager;

import org.opencv.core.Core;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatSolarizedDarkIJTheme;

import ImagePipeline.control.MainControl;

public class ImagePipelineMain {
    public static void main(String[] args) {
        new ImagePipelineMain(args);
    }

    public ImagePipelineMain(String[] args) {
        // Link FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatSolarizedDarkIJTheme());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        // Link OpenCV
        try {
            // for "ant run"
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        } catch (UnsatisfiedLinkError e) {
            // for "ant jar"
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                // for Windows
                loadJarDll(Core.NATIVE_LIBRARY_NAME + ".dll");
            } else {
                // for UNIX
                loadJarDll("lib" + Core.NATIVE_LIBRARY_NAME + ".dylib");
            }
        }

        MainControl mainControl = new MainControl(args);
        mainControl.start();
    }

    public void loadJarDll(String fileName) {
        if (fileName == null) {
            return;
        }

        try {
            InputStream in = getClass().getClassLoader().getResource(fileName).openStream();
            byte[] buffer = new byte[1024];
            int read = -1;
            File temp = File.createTempFile(fileName, "");
            FileOutputStream fos = new FileOutputStream(temp);

            while ((read = in.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            fos.close();
            in.close();

            System.load(temp.getAbsolutePath());
        } catch (Exception e) {
            System.err.println(e.toString());
            System.err.println("Failed to link OpenCV");
        }
    }
}
