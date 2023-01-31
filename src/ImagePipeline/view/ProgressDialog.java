package ImagePipeline.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.JPanel;

import ImagePipeline.resource.ResourceManager;
import ImagePipeline.util.Common;

import net.miginfocom.swing.MigLayout;

public class ProgressDialog extends JDialog {
    private static final int DEFAULT_WINDOW_WIDTH = 600;
    private static final int DEFAULT_WINDOW_HEIGHT = 400;
    private static final int MIN_WINDOW_WIDTH = 300;
    private static final int MIN_WINDOW_HEIGHT = 200;

    private static final String KEY_PRE = "dialog.progressDialog.";

    private Logger _logger;
    protected ResourceManager _rm;

    private ProgressMonitor _monitor;
    private JTextArea _textArea;
    private JButton _btRun;
    private ActionListener _actionListener;

    public ProgressDialog(JFrame parent) {
        super(parent);
        _logger = Logger.getLogger(getClass().getName());
        _logger.setLevel(Common.GLOBAL_LOG_LEVEL);
        _rm = ResourceManager.getInstance();

        setPreferredSize(new Dimension(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT));
        setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));
        setTitle(_rm.getString(KEY_PRE + "title"));
        buildUI();

        pack();
        setLocationByPlatform(true);
    }

    public void buildUI() {
        JPanel mainPanel = new JPanel(
                new MigLayout("wrap 1", "left, right, fill, grow",
                        "[top, bottom, fill, grow][2cm, top, bottom, fill][2cm, top, bottom, fill]"));

        _textArea = new JTextArea();
        _textArea.setEditable(false);

        _monitor = new ProgressMonitor(this, "message", "note", 0, 100);

        _btRun = new JButton("run");
        initWorker(null);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(mainPanel, BorderLayout.CENTER);
    }

    public void initWorker(SwingWorker<String, String> worker) {
        _btRun.removeActionListener(_actionListener);
        _actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _btRun.setEnabled(false);
                _monitor.setProgress(0);
                if (worker != null) {
                    worker.addPropertyChangeListener(new ProgressListener(_monitor));
                    worker.execute();
                }
            }
        };
        _btRun.addActionListener(_actionListener);
    }

    public void init(int max, ProgressDialogWorker worker) {
        _monitor.setProgress(0);
        _monitor.setMaximum(max);
        initWorker(worker);

        _textArea.setText(null);

        worker.init(this, _monitor, _textArea, _btRun);
    }
}
