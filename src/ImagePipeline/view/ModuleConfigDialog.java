package ImagePipeline.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import ImagePipeline.control.MainFrameEventListener;
import ImagePipeline.model.primitives.ConfigPrimitive;
import ImagePipeline.model.primitives.ModulePrimitive;
import ImagePipeline.model.primitives.ValueType;
import ImagePipeline.resource.ResourceManager;
import ImagePipeline.util.Common;
import net.miginfocom.swing.MigLayout;

public class ModuleConfigDialog extends JDialog implements ActionListener {
    private static final String KEY_PRE = "dialog.moduleConfig.";

    private static final String COMMAND_CANCEL = KEY_PRE + "command.cancel";
    private static final String COMMAND_OK = KEY_PRE + "command.ok";

    private Logger _logger;
    protected ResourceManager _rm;
    private MainFrameEventListener _eventListener;
    private JButton _btCancel;
    private JButton _btOK;
    private JTable _table;
    private DefaultTableModel _tableModel;
    protected ModulePrimitive _module;
    protected HashMap<String, ConfigPrimitive> _defaultConfig;
    protected Map<String, String> _mapConfigNames;

    public ModuleConfigDialog(JFrame parent, MainFrameEventListener eventListener) {
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
        String[] columnNames = {
                _rm.getString(KEY_PRE + "column.key"),
                _rm.getString(KEY_PRE + "column.value") };

        _tableModel = new DefaultTableModel(columnNames, 0);

        _table = new JTable(_tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        };
        JScrollPane scrollPaneForModuleConfig = new JScrollPane(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForModuleConfig.setViewportView(_table);
        scrollPaneForModuleConfig.setBorder(new EtchedBorder());

        // =========================================================================
        // Cancel button ===========================================================
        // =========================================================================
        _btCancel = createButton(COMMAND_CANCEL);

        // =========================================================================
        // OK button ===============================================================
        // =========================================================================
        _btOK = createButton(COMMAND_OK);

        mainPanel.add(scrollPaneForModuleConfig, "span 2");
        mainPanel.add(_btCancel);
        mainPanel.add(_btOK);

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

    public void initTable(ModulePrimitive module) {
        _module = module;
        _mapConfigNames = new HashMap<String, String>();
        HashMap<String, ConfigPrimitive> config = module.getDefaultConfig();
        _defaultConfig = config;
        int nConfigs = config.size();

        int nCurrentRows = _tableModel.getRowCount();
        if (nConfigs < nCurrentRows) {
            for (int i = 0; i < nCurrentRows - nConfigs; i++) {
                _tableModel.removeRow(0);
            }
        } else if (nConfigs > nCurrentRows) {
            for (int i = 0; i < nConfigs - nCurrentRows; i++) {
                String[] rowData = {
                        "",
                        ""
                };
                _tableModel.addRow(rowData);
            }
        }

        Set<String> keys = config.keySet();
        int counter = 0;
        for (Iterator<String> itr = keys.iterator(); itr.hasNext();) {
            String key = itr.next();
            String keyDisplay = _rm.getString(key);
            String value = config.get(key).getStringValue();

            _tableModel.setValueAt(keyDisplay, counter, 0);
            _tableModel.setValueAt(value, counter, 1);

            _mapConfigNames.put(keyDisplay, key);
            counter++;
        }

        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        String command = e.getActionCommand();
        _logger.info("[ModuleConfigDialog] Action on command : " + command);
        if (command.equalsIgnoreCase(COMMAND_CANCEL)) {
            setVisible(false);
        } else if (command.equalsIgnoreCase(COMMAND_OK)) {
            addModule();
        } else {
            _logger.info("[ModuleConfigDialog] nothing to do");
        }
    }

    private void addModule() {
        boolean isOK = true;
        int nRows = _tableModel.getRowCount();
        HashMap<String, ConfigPrimitive> configs = new HashMap<String, ConfigPrimitive>();

        for (int i = 0; i < nRows; i++) {
            String key = _table.getValueAt(i, 0).toString();
            String value = _table.getValueAt(i, 1).toString();

            key = _mapConfigNames.get(key);

            ValueType type = _defaultConfig.get(key).getValueType();
            ConfigPrimitive config = ConfigPrimitive.getValue(value, type);

            if (config != null) {
                configs.put(key, config);
            } else {
                _logger.warning(String.format("Failed to parse value: key=%s, value=%s", key, value));
                isOK = false;
            }
        }

        if (isOK) {
            _module.setConfig(configs);
            setVisible(false);
            _eventListener.addModule(_module);
        }
    }

}
