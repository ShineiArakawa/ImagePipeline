package ImagePipeline.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;

import ImagePipeline.control.MainFrameEventListener;
import ImagePipeline.resource.ResourceManager;
import ImagePipeline.util.Common;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class SimpleProgressDialog extends JDialog implements ActionListener {
    private static final int WINDOW_HEIGHT = 256;
    private static final int WINDOW_WIDHT = 256;

    private static final String COMMAND_CANCEL = "progressDialog.cancel";

    protected Logger _logger;
    protected ResourceManager _rm;

    protected JProgressBar _progressBar;
    protected JButton _btCancel;
    protected JLabel _label;

    private boolean _isCanceled;

    public SimpleProgressDialog(JFrame parent, boolean modal) {
        super(parent, modal);
        _logger = Logger.getLogger(getClass().getName());
        _logger.setLevel(Common.GLOBAL_LOG_LEVEL);
        _rm = ResourceManager.getInstance();
    }

    public void init(String title, String message, int min, int max) {
        _isCanceled = false;

        setTitle(title);
        setSize(WINDOW_WIDHT, WINDOW_HEIGHT);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new MigLayout("wrap 1", "[left, right, fill, grow]",
                "[top, bottom, fill, grow][top, bottom, fill, grow][top, bottom, fill, grow]"));
        _label = new JLabel(message);

        _progressBar = new JProgressBar(min, max);
        _btCancel = createButton(COMMAND_CANCEL);

        panel.add(_label);
        panel.add(_progressBar);
        panel.add(_btCancel);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(panel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    public JProgressBar getProgressBar() {
        return _progressBar;
    }

    protected JButton createButton(String command) {
        JButton tmpButton = new JButton(_rm.getString(command));
        addActionListenerForButton(tmpButton, command);
        return tmpButton;
    }

    protected void addActionListenerForButton(AbstractButton button, String actionCommand) {
        button.addActionListener(this);
        button.setActionCommand(actionCommand);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        String command = e.getActionCommand();
        if (command != null && command == COMMAND_CANCEL) {
            _isCanceled = true;
            setVisible(false);
            dispose();
        }
    }

    public boolean isCanceled() {
        return _isCanceled;
    }

    public void refresh(int progress, String message) {
        _progressBar.setValue(progress);
        if (message != null) {
            _label.setText(message);
        }
        // repaint();
    }
}