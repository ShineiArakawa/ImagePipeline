package ImagePipeline.view;

import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

public abstract class ProgressDialogWorker extends SwingWorker<String, String> {
    protected JDialog _dialog;
    protected ProgressMonitor _monitor;
    protected JTextArea _textArea;
    protected JButton _runButton;

    public ProgressDialogWorker() {
    };

    protected void init(JDialog dialog,
            ProgressMonitor monitor,
            JTextArea textArea,
            JButton runButton) {
        _dialog = dialog;
        _monitor = monitor;
        _textArea = textArea;
        _runButton = runButton;
    }

    @Override
    protected void process(List<String> chunks) {
        if (isCancelled()) {
            return;
        }
        if (!_dialog.isDisplayable()) {
            cancel(true);
            return;
        }
        for (String message : chunks) {
            _monitor.setNote(message);
        }
    }

    @Override
    public void done() {
        _runButton.setEnabled(true);
        _monitor.close();
        try {
            if (isCancelled()) {
                _textArea.append("Cancelled\n");
            } else {
                _textArea.append(get() + "\n");
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            _textArea.append("Exception\n");
        }
        _textArea.setCaretPosition(_textArea.getDocument().getLength());
    }
}