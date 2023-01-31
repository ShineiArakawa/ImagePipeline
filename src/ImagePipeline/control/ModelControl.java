package ImagePipeline.control;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import ImagePipeline.model.pipeline.Pipeline;
import ImagePipeline.model.primitives.ImagePrimitive;
import ImagePipeline.model.primitives.ModulePrimitive;
import ImagePipeline.resource.ResourceManager;
import ImagePipeline.util.Common;
import ImagePipeline.util.FileUtil;
import ImagePipeline.view.ProgressDialogWorker;

public class ModelControl {
    private Logger _logger;
    protected ResourceManager _rm;
    private MainFrameEventListener _eventListener;
    private Pipeline _pipeline;

    public ModelControl(MainFrameEventListener eventListener) {
        _logger = Logger.getLogger(getClass().getName());
        _logger.setLevel(Common.GLOBAL_LOG_LEVEL);
        _rm = ResourceManager.getInstance();

        _eventListener = eventListener;
        _pipeline = new Pipeline();
    }

    public String[] getAllModuleNames() {
        return _pipeline.getAllModuleNames();
    }

    public void runPipeline(String inputDirPath, String[] inputFileRelativePaths, ModulePrimitive[] moduleList,
            String outputDirPath) {
        if (inputFileRelativePaths == null || moduleList == null || outputDirPath == null) {
            return;
        }

        int nImages = inputFileRelativePaths.length;
        int nModules = moduleList.length;

        // Log output
        StringBuffer sb = new StringBuffer();
        sb.append("[InputFiles]" + Common.EOL);
        for (int iImage = 0; iImage < nImages; iImage++) {
            sb.append(iImage + ": " + inputFileRelativePaths[iImage] + Common.EOL);
        }
        sb.append("[Pipeline]" + Common.EOL);
        for (int iModule = 0; iModule < nModules; iModule++) {
            sb.append(iModule + ": " + moduleList[iModule] + Common.EOL);
        }
        sb.append("[OutputDir]" + Common.EOL);
        sb.append(outputDirPath + Common.EOL);
        _logger.info(sb.toString());

        // Read image
        String[] inputFileAbsPaths = FileUtil.join(inputDirPath, inputFileRelativePaths);
        ImagePrimitive[] images = loadImages(inputFileAbsPaths);

        // Run pipeline
        _pipeline.setModules(moduleList);
        images = _pipeline.runPipeline(images);

        // Save image
        String[] outputFileAbsPaths = FileUtil.join(outputDirPath, inputFileRelativePaths);
        saveImages(images, outputFileAbsPaths);

        Runtime r = Runtime.getRuntime();
        r.gc();
    }

    private ImagePrimitive[] loadImages(String[] inputFilePaths) {
        int nImages = inputFilePaths.length;
        _logger.info("Start loading " + nImages + " images ...");

        ImagePrimitive[] images = null;

        Runtime r = Runtime.getRuntime();
        int nCores = r.availableProcessors();
        _logger.info("nThreads=" + nCores);

        ExecutorService pool = Executors.newFixedThreadPool(nCores);
        ArrayList<Future<ImagePrimitive>> results = new ArrayList<Future<ImagePrimitive>>();

        for (int iImage = 0; iImage < nImages; iImage++) {
            Future<ImagePrimitive> result = pool.submit(new CallableLoadImage(inputFilePaths[iImage]));
            results.add(result);
        }

        int nFailedImages = 0;
        try {
            _logger.info("Waiting for thread termination ...");
            pool.shutdown();
            _logger.info("Terminated !");

            ArrayList<ImagePrimitive> imagesArray = new ArrayList<ImagePrimitive>();
            for (int iImage = 0; iImage < nImages; iImage++) {
                ImagePrimitive image = results.get(iImage).get();
                if (image != null) {
                    imagesArray.add(image);
                } else {
                    nFailedImages++;
                }
            }
            if (nFailedImages > 0) {
                _logger.warning("Failed to load " + nFailedImages + " images.");
            }
            images = new ImagePrimitive[imagesArray.size()];
            imagesArray.toArray(images);
        } catch (InterruptedException | ExecutionException e) {
            _logger.info("Failed to complete loading images.");
        }
        _logger.info("Finish loading " + (nImages - nFailedImages) + " images !");

        return images;
    }

    private void saveImages(ImagePrimitive[] images, String[] outputFilePaths) {
        if (images == null || outputFilePaths == null || (images.length != outputFilePaths.length)) {
            _logger.warning("Saving images was aborted.");
            return;
        }

        int nImages = outputFilePaths.length;
        _logger.info("Start saving " + nImages + " images ...");

        Runtime r = Runtime.getRuntime();
        int nCores = r.availableProcessors();
        _logger.info("nThreads=" + nCores);

        ExecutorService pool = Executors.newFixedThreadPool(nCores);

        for (int iImage = 0; iImage < nImages; iImage++) {
            pool.execute(new RunnableSaveImage(images[iImage], outputFilePaths[iImage]));
        }

        pool.shutdown();

        _logger.info("Finish saving " + nImages + " images !");
    }

    public static ModulePrimitive createModule(String moduleName) {
        return Pipeline.createModule(moduleName);
    }
}

class CallableLoadImage implements Callable<ImagePrimitive> {
    protected String _filePath;

    public CallableLoadImage(String filePath) {
        _filePath = filePath;
    }

    public ImagePrimitive call() throws Exception {
        ImagePrimitive imagePrimitive = ImagePrimitive.getImagePrimitive(_filePath);
        return imagePrimitive;
    }
}

class RunnableSaveImage implements Runnable {
    protected ImagePrimitive _image;
    protected String _filePath;

    public RunnableSaveImage(ImagePrimitive image, String filePath) {
        _image = image;
        _filePath = filePath;
    }

    @Override
    public void run() {
        _image.saveImage(_filePath);
    }
}