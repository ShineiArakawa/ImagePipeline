package ImagePipeline.control;

import ImagePipeline.model.primitives.ModulePrimitive;
import ImagePipeline.view.ProgressDialogWorker;

public interface MainFrameEventListener {
    public void action(String command);

    public void action(String command, String value);

    public void action(String command, int value);

    public void addModule(ModulePrimitive module, int index);

    public void showErrorMessageDialog(String message, String title);

    public void showProgressDialog(String title, int max, ProgressDialogWorker worker);
}
