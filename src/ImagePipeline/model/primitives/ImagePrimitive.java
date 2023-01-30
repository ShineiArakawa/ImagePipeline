package ImagePipeline.model.primitives;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import ImagePipeline.util.FileUtil;

public class ImagePrimitive {
    private Mat _image;

    public ImagePrimitive() {

    }

    public Mat getImage() {
        return _image;
    }

    public void setImage(Mat image) {
        if (image != null) {
            _image = image;
        }
    }

    public static ImagePrimitive getImagePrimitive(String filePath) {
        Mat image = Imgcodecs.imread(filePath);

        ImagePrimitive imagePrimitive = null;
        if (image != null) {
            imagePrimitive = new ImagePrimitive();
            imagePrimitive.setImage(image);
        }

        return imagePrimitive;
    }

    public void saveImage(String filePath) {
        FileUtil.mkdirs(FileUtil.dirPath(filePath));
        Imgcodecs.imwrite(filePath, _image);
    }
}
