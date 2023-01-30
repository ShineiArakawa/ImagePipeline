package ImagePipeline.control;

import ImagePipeline.model.primitives.ModulePrimitive;
import ImagePipeline.view.SimpleProgressDialog;

public interface MainFrameEventListener {
    public void action(String command);

    public void action(String command, String value);

    public void action(String command, int value);

    public void addModule(ModulePrimitive module, int index);

    public void showErrorMessageDialog(String message, String title);

    public SimpleProgressDialog showProgressBarLoadingImage(String title, String message, int min, int max);

}
