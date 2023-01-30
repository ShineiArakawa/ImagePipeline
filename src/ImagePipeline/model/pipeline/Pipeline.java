package ImagePipeline.model.pipeline;

import java.util.logging.Logger;

import ImagePipeline.model.modules.Crop;
import ImagePipeline.model.modules.Resize;
import ImagePipeline.model.primitives.ImagePrimitive;
import ImagePipeline.model.primitives.ModulePrimitive;
import ImagePipeline.util.Common;

public class Pipeline {
    private static final String[] AVAILABLE_MODULE_NAMES = {
            Resize.MODULE_NAME,
            Crop.MODULE_NAME
    };

    private Logger _logger;
    ModulePrimitive[] _modules;

    public Pipeline() {
        _logger = Logger.getLogger(getClass().getName());
        _logger.setLevel(Common.GLOBAL_LOG_LEVEL);

        _modules = null;
    }

    public void setModules(ModulePrimitive[] modules) {
        if (modules != null) {
            _modules = modules;
        }
    }

    public ImagePrimitive[] runPipeline(ImagePrimitive[] images) {
        if (_modules != null && images != null) {
            for (int i = 0; i < _modules.length; i++) {
                if (images == null) {
                    break;
                }
                images = _modules[i].apply(images);
            }
        }

        return images;
    }

    public String[] getAllModuleNames() {
        return AVAILABLE_MODULE_NAMES;
    }

    public static ModulePrimitive createModule(String moduleName) {
        Logger logger = Logger.getLogger(Pipeline.class.getName());
        logger.setLevel(Common.GLOBAL_LOG_LEVEL);
        ModulePrimitive module = null;

        if (moduleName != null) {
            if (moduleName.equals(Resize.MODULE_NAME)) {
                module = new Resize();
            } else if (moduleName.equals(Crop.MODULE_NAME)) {
                module = new Crop();
            } else {
                logger.warning("Unknown module name: " + moduleName);
            }
        }

        return module;
    }
}
