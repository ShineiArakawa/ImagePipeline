package ImagePipeline.view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

public class ProgressListener implements PropertyChangeListener {
    public static final String COMMANT_PROGRESS = "progress";

    private ProgressMonitor _monitor;

    public ProgressListener(ProgressMonitor monitor) {
        _monitor = monitor;
        _monitor.setProgress(0);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        Object o = e.getSource();
        String strPropertyName = e.getPropertyName();

        if (strPropertyName.equals(COMMANT_PROGRESS) && o instanceof SwingWorker) {
            SwingWorker<String, String> task = (SwingWorker<String, String>) o;
            _monitor.setProgress((Integer) e.getNewValue());
            if (_monitor.isCanceled() || task.isDone()) {

                task.cancel(true);
            }
        }
    }
}