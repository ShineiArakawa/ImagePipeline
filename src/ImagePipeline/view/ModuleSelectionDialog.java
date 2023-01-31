package ImagePipeline.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import ImagePipeline.control.MainFrameEventListener;
import ImagePipeline.resource.ResourceManager;
import ImagePipeline.util.Common;
import net.miginfocom.swing.MigLayout;

public class ModuleSelectionDialog extends JDialog implements ActionListener {
    private static final String KEY_PRE = "dialog.moduleSelection.";

    public static final String COMMAND_CANCEL = KEY_PRE + "command.cancel";
    public static final String COMMAND_ADD = KEY_PRE + "command.add";

    private Logger _logger;
    protected ResourceManager _rm;
    private MainFrameEventListener _eventListener;
    private JList<String> _moduleList;
    private JButton _btCancel;
    private JButton _btAdd;

    public ModuleSelectionDialog(JFrame parent, MainFrameEventListener eventListener) {
        super(parent, true);
        _logger = Logger.getLogger(getClass().getName());
        _logger.setLevel(Common.GLOBAL_LOG_LEVEL);

        _rm = ResourceManager.getInstance();
        _eventListener = eventListener;

        setWindowTitle(_rm.getString(KEY_PRE + "title"));
        buildUI();

        pack();
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(
                new MigLayout("wrap 2", "left, right, fill, grow",
                        "[top, bottom, fill, grow][2.5cm, top, bottom, fill]"));

        // =========================================================================
        // Module List =============================================================
        // =========================================================================
        _moduleList = new JList<String>();
        _moduleList.setBorder(new EtchedBorder());
        JScrollPane scrollPaneForModuleList = new JScrollPane(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForModuleList.setViewportView(_moduleList);
        scrollPaneForModuleList.setBorder(new EtchedBorder());

        // =========================================================================
        // Cancel button ===========================================================
        // =========================================================================
        _btCancel = createButton(COMMAND_CANCEL);

        // =========================================================================
        // Add button ==============================================================
        // =========================================================================
        _btAdd = createButton(COMMAND_ADD);

        mainPanel.add(scrollPaneForModuleList, "span 2");
        mainPanel.add(_btCancel);
        mainPanel.add(_btAdd);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(mainPanel, BorderLayout.CENTER);
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

    public void setWindowTitle(String title) {
        if (title != null) {
            super.setTitle(title);
        }
    }

    public void setModuleNames(String[] moduleNames) {
        if (moduleNames != null) {
            _moduleList.removeAll();
            _moduleList.setListData(moduleNames);
            repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        _logger.info("[ModuleSelectionDialog] Action on command : " + command);
        if (command.equalsIgnoreCase(COMMAND_CANCEL)) {
            setVisible(false);
        } else if (command.equalsIgnoreCase(COMMAND_ADD)) {
            addModule();
        } else {
            _logger.info("[ModuleSelectionDialog] nothing to do");
        }
    }

    private void addModule() {
        int selectedIndex = _moduleList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String moduleName = _moduleList.getModel().getElementAt(selectedIndex);
            setVisible(false);
            _eventListener.action(COMMAND_ADD, moduleName);
        } else {
            String message = _rm.getString(KEY_PRE + "errorMessage");
            String title = _rm.getString(KEY_PRE + "errorTitle");
            _eventListener.showErrorMessageDialog(message, title);
        }
    }
}
