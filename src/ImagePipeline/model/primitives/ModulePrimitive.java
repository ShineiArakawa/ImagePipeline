package ImagePipeline.model.primitives;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import ImagePipeline.util.Common;

public abstract class ModulePrimitive implements Serializable {
    abstract public ImagePrimitive applyEach(ImagePrimitive image);

    abstract protected HashMap<String, ConfigPrimitive> getDefaultConfig();

    abstract public String getModuleName();

    Logger _logger;
    protected HashMap<String, ConfigPrimitive> _config = null;

    public ModulePrimitive() {
        _logger = Logger.getLogger(getClass().getName());
        _logger.setLevel(Common.GLOBAL_LOG_LEVEL);
    }

    public HashMap<String, ConfigPrimitive> getConfig() {
        HashMap<String, ConfigPrimitive> config = null;
        if (_config == null) {
            config = getDefaultConfig();
        } else {
            config = _config;
        }
        return config;
    }

    public void setConfig(HashMap<String, ConfigPrimitive> config) {
        _config = config;
    }

    public ImagePrimitive[] apply(ImagePrimitive[] imagePrimitives) {
        if (imagePrimitives == null) {
            return null;
        }

        int nImages = imagePrimitives.length;
        _logger.info("Start processing " + nImages + " images ...");

        ImagePrimitive[] images = null;

        Runtime r = Runtime.getRuntime();
        int nCores = r.availableProcessors();
        _logger.info("nThreads=" + nCores);

        ExecutorService pool = Executors.newFixedThreadPool(nCores);
        ArrayList<Future<ImagePrimitive>> results = new ArrayList<Future<ImagePrimitive>>();

        for (int iImage = 0; iImage < nImages; iImage++) {
            Future<ImagePrimitive> result = pool.submit(new CallableApply(imagePrimitives[iImage]));
            results.add(result);
        }

        int nFailedImages = 0;
        try {
            _logger.info("Waiting for thread termination ...");
            pool.shutdown();

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
                _logger.warning("Failed to process" + nFailedImages + " images.");
            }
            images = new ImagePrimitive[imagesArray.size()];
            imagesArray.toArray(images);
        } catch (InterruptedException | ExecutionException e) {
            _logger.warning("Failed to complete processing images.");
            images = null;
        }

        _logger.info("Finish processing " + (nImages - nFailedImages) + " images !");

        return images;
    }

    class CallableApply implements Callable<ImagePrimitive> {
        protected ImagePrimitive _image;

        public CallableApply(ImagePrimitive image) {
            _image = image;
        }

        public ImagePrimitive call() throws Exception {
            return applyEach(_image);
        }
    }
}
