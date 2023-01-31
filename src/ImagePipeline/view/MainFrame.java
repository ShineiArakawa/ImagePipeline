package ImagePipeline.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyListener;

import java.io.File;
import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import ImagePipeline.control.MainFrameEventListener;
import ImagePipeline.model.primitives.ModulePrimitive;
import ImagePipeline.resource.ResourceManager;
import ImagePipeline.util.Common;
import ImagePipeline.util.FileUtil;
import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame implements ActionListener, WindowListener {
    private static final int DEFAULT_WINDOW_WIDTH = 1000;
    private static final int DEFAULT_WINDOW_HEIGHT = 600;
    private static final int MIN_WINDOW_WIDTH = 600;
    private static final int MIN_WINDOW_HEIGHT = 600;
    private static final String LABEL_PRE = "label.";

    private static final String LABEL_OUTPUT_FOLDER = "outputFolder";

    public static final String COMMAND_FILE = "menu.file";
    public static final String COMMAND_FILE_OPEN_FOLDER = "menu.file.openFolder";
    public static final String COMMAND_FILE_EXIT = "menu.file.exit";

    public static final String COMMAND_THEME = "menu.theme";

    public static final String COMMAND_ADD_MODULE = "command.addModule";
    public static final String COMMAND_REMOVE_MODULE = "command.removeModule";
    public static final String COMMAND_RUN_PIPELINE = "command.runPipeline";
    public static final String COMMAND_OPEN_FILE_DIALOG_OUTPUT_DIR_PATH = "command.openFileDialogOutputDirPath";
    public static final String COMMAND_UPDATE_IMAGE_VIEWER = "command.updateImageViewer";

    public static final String COMMAND_EDIT_MODULE = "command.editModule";

    protected static final String[] LIST_ALPHABETS = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };
    protected static final int[] LIST_KEY_CODES = {
            KeyEvent.VK_A, KeyEvent.VK_B, KeyEvent.VK_C, KeyEvent.VK_D, KeyEvent.VK_E,
            KeyEvent.VK_F, KeyEvent.VK_G, KeyEvent.VK_H, KeyEvent.VK_I, KeyEvent.VK_J,
            KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.VK_M, KeyEvent.VK_N, KeyEvent.VK_O,
            KeyEvent.VK_P, KeyEvent.VK_Q, KeyEvent.VK_R, KeyEvent.VK_S, KeyEvent.VK_T,
            KeyEvent.VK_U, KeyEvent.VK_V, KeyEvent.VK_W, KeyEvent.VK_X, KeyEvent.VK_Y,
            KeyEvent.VK_Z
    };
    //@formatter:on

    protected Logger _logger;
    protected ResourceManager _rm;
    private MainFrameEventListener _eventListener;
    protected HashSet<String> _setCommandsOfButton;
    protected JList<String> _inputFileList;
    protected JList<ModulePrimitive> _pipelinesList;
    protected JFileChooser _fileChooser;
    protected JButton _btAddModule;
    protected JButton _btRemoveModule;
    protected JButton _btRunPipeline;
    protected JButton _btOpenFileDialogOutputDirPath;
    protected JLabel _labelOutputDirPath;
    protected JTextField _tfInputDirPath;
    protected JTextField _tfOutputDirPath;
    protected ProgressDialog _progressDialog;
    protected ModuleSelectionDialog _moduleSelectionDialog;
    protected ModuleConfigDialog _moduleConfigDialog;
    protected ImageViewerDialog _imageViewer;
    protected ThemeManager _themeManager;

    public MainFrame(MainFrameEventListener eventListener) {
        super();
        _logger = Logger.getLogger(getClass().getName());
        _logger.setLevel(Common.GLOBAL_LOG_LEVEL);
        _themeManager = new ThemeManager();

        addWindowListener(this);
        _rm = ResourceManager.getInstance();
        _setCommandsOfButton = new HashSet<String>();
        _eventListener = eventListener;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setWindowTitle(_rm.getString("window.main.title"));
        setLogoIcon(Common.LOGO_FILE_PATH);
        buildUI();
        setPreferredSize(new Dimension(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT));
        setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));

        pack();
        setLocationByPlatform(true);
        // setAlwaysOnTop(true);
        setAutoRequestFocus(true);

        _moduleSelectionDialog = new ModuleSelectionDialog(this, _eventListener);
        _moduleConfigDialog = new ModuleConfigDialog(this, _eventListener);
        _imageViewer = new ImageViewerDialog(this, _eventListener);
        _progressDialog = new ProgressDialog(this);
    }

    private void setLogoIcon(String filePath) {
        if (filePath != null) {
            ImageIcon icon = new ImageIcon(filePath);
            if (icon != null) {
                setIconImage(icon.getImage());
                super.setIconImage(icon.getImage());
            }
        }
    }

    public void setWindowTitle(String title) {
        if (title != null) {
            super.setTitle(title);
        }
    }

    private void buildUI() {
        buildMenu();
        JPanel mainPanel = buildMainPanel();

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(mainPanel, BorderLayout.CENTER);
    }

    private void buildMenu() {
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        JMenu menuFile = createMenu(COMMAND_FILE, "F");
        menubar.add(menuFile);

        //@formatter:off
        JMenuItem   miFileOpenFolder        = createMenuItem(COMMAND_FILE_OPEN_FOLDER,        "O");
        JMenuItem   miFileExit              = createMenuItem(COMMAND_FILE_EXIT,               "X");
        //@formatter:on

        menuFile.add(miFileOpenFolder);
        menuFile.add(miFileExit);

        JMenu menuTheme = new JMenu(COMMAND_THEME);
        menubar.add(menuTheme);
        ArrayList<String> themes = _themeManager.getThemes();
        ActionListener actionListenerForTheme = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String theme = e.getActionCommand();
                _themeManager.setTheme(theme);
            }
        };
        for (int i = 0; i < themes.size(); i++) {
            String themeName = themes.get(i);
            JMenuItem item = new JMenuItem(themeName);
            item.addActionListener(actionListenerForTheme);
            menuTheme.add(item);
        }

    }

    private JPanel buildMainPanel() {
        JPanel inputFilesPanel = buildInputFilesPanel();
        JPanel pipelinesPanel = buildPipelinesPanel();

        JPanel panel = new JPanel(new MigLayout("", "grow", "grow"));
        panel.add(inputFilesPanel, "grow");
        panel.add(pipelinesPanel, "grow");

        return panel;
    }

    private JPanel buildInputFilesPanel() {
        String strPre = LABEL_PRE + "inputFiles.";

        JPanel panelInputFile = new JPanel(new MigLayout("wrap 4",
                "[left, right, fill][left, right, fill, grow][left, right, fill, grow][left, right, fill, grow]",
                "[1cm, top, bottom, fill][top, bottom, fill, grow]"));
        String title = _rm.getString(strPre + "title");
        panelInputFile.setBorder(new TitledBorder(title));

        JLabel labelInputDirPath = new JLabel(_rm.getString(strPre + "label"));
        _tfInputDirPath = new JTextField();
        _tfInputDirPath.setEditable(false);
        _tfInputDirPath.setBorder(new EtchedBorder());

        _inputFileList = new JList<String>();
        _inputFileList.setBorder(new EtchedBorder());
        _inputFileList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1 && _imageViewer.isVisible()) {
                    int index = _inputFileList.locationToIndex(evt.getPoint());
                    if (index >= 0 && index < _inputFileList.getModel().getSize()) {
                        _eventListener.action(COMMAND_UPDATE_IMAGE_VIEWER, index);
                    }
                } else if (evt.getClickCount() == 2) {
                    int index = _inputFileList.locationToIndex(evt.getPoint());
                    if (index >= 0 && index < _inputFileList.getModel().getSize()) {
                        _eventListener.action(COMMAND_UPDATE_IMAGE_VIEWER, index);
                    }
                }
            }
        });
        _inputFileList.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if ((keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) && _imageViewer.isVisible()) {
                    int selectedIndex = _inputFileList.getSelectedIndex();
                    _eventListener.action(COMMAND_UPDATE_IMAGE_VIEWER, selectedIndex);
                }
            }

        });

        JScrollPane scrollPaneForInputFileList = new JScrollPane(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForInputFileList.setViewportView(_inputFileList);

        panelInputFile.add(labelInputDirPath, "span 1");
        panelInputFile.add(_tfInputDirPath, "span 3");
        panelInputFile.add(scrollPaneForInputFileList, "span 4");
        return panelInputFile;
    }

    private JPanel buildPipelinesPanel() {
        String strPre = LABEL_PRE + "pipelines.";
        JPanel panelPipelines = new JPanel(new MigLayout("wrap 1", "[left, right, fill, grow]",
                "[2cm, top, fill][top, bottom, fill, grow][1cm, bottom, fill]"));
        String title = _rm.getString(strPre + "title");
        panelPipelines.setBorder(new TitledBorder(title));

        // =========================================================================
        // setting =================================================================
        // =========================================================================
        JPanel panelPipelineSetting = new JPanel(
                new MigLayout(
                        "wrap 6",
                        "[2.5cm!, left, right, fill][left, right, fill, grow][left, right, fill, grow][left, right, fill, grow][left, right, fill, grow][2.5cm!, left, right, fill]",
                        "top, bottom, fill, grow"));
        panelPipelineSetting.setBorder(new EtchedBorder());

        _labelOutputDirPath = new JLabel(_rm.getString(strPre + LABEL_OUTPUT_FOLDER));

        _tfOutputDirPath = new JTextField();
        _tfOutputDirPath.setBorder(new EtchedBorder());

        _btOpenFileDialogOutputDirPath = createButton(COMMAND_OPEN_FILE_DIALOG_OUTPUT_DIR_PATH);
        _btAddModule = createButton(COMMAND_ADD_MODULE);
        _btRemoveModule = createButton(COMMAND_REMOVE_MODULE);

        panelPipelineSetting.add(_labelOutputDirPath);
        panelPipelineSetting.add(_tfOutputDirPath, "span 4");
        panelPipelineSetting.add(_btOpenFileDialogOutputDirPath, "wrap");
        panelPipelineSetting.add(_btAddModule, "span 3");
        panelPipelineSetting.add(_btRemoveModule, "span 3");

        // =========================================================================
        // Pipeline lists ==========================================================
        // =========================================================================
        JPanel panelPipelinesList = new JPanel(new BorderLayout());
        _pipelinesList = new JList<ModulePrimitive>();
        _pipelinesList.setBorder(new EtchedBorder());
        _pipelinesList.setDragEnabled(true);
        _pipelinesList.setDropMode(DropMode.INSERT);
        _pipelinesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _pipelinesList.setTransferHandler(new ListTransferHandler(_pipelinesList));
        _pipelinesList.setCellRenderer(new PipelineListCellRenderer());
        _pipelinesList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = _pipelinesList.locationToIndex(evt.getPoint());
                    if (index >= 0 && index < _pipelinesList.getModel().getSize()) {
                        _eventListener.action(COMMAND_EDIT_MODULE, index);
                    }
                }
            }
        });

        JScrollPane scrollPaneForPipelinesList = new JScrollPane(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForPipelinesList.setViewportView(_pipelinesList);
        scrollPaneForPipelinesList.setBorder(new EtchedBorder());
        panelPipelinesList.add(scrollPaneForPipelinesList, BorderLayout.CENTER);

        // =========================================================================
        // run =====================================================================
        // =========================================================================
        JPanel panelRunPipeline = new JPanel(new MigLayout("", "left, right, fill, grow", "top, bottom, fill, grow"));
        panelRunPipeline.setBorder(new EtchedBorder());
        _btRunPipeline = createButton(COMMAND_RUN_PIPELINE);
        panelRunPipeline.add(_btRunPipeline);

        panelPipelines.add(panelPipelineSetting);
        panelPipelines.add(panelPipelinesList);
        panelPipelines.add(panelRunPipeline);
        return panelPipelines;
    }

    protected JMenu createMenu(String command, String mnemonic) {
        JMenu tmpMenu = new JMenu();
        setMnemonic(tmpMenu, _rm.getString(command), mnemonic);
        return tmpMenu;
    }

    protected JMenuItem createMenuItem(String command, String mnemonic) {
        JMenuItem tmpMenuItem = new JMenuItem();
        setMnemonic(tmpMenuItem, _rm.getString(command), mnemonic);
        addActionListenerForButton(tmpMenuItem, command);
        return tmpMenuItem;
    }

    protected JButton createButton(String command) {
        JButton tmpButton = new JButton(_rm.getString(command));
        addActionListenerForButton(tmpButton, command);
        return tmpButton;
    }

    protected void addActionListenerForButton(AbstractButton button, String actionCommand) {
        button.addActionListener(this);
        button.setActionCommand(actionCommand);
        _setCommandsOfButton.add(actionCommand);
    }

    protected static void setMnemonic(AbstractButton button, String label, String mnemonic) {
        String labelMnemonic = null;
        int keyMnemonic = -1;
        if (mnemonic != null) {
            for (int i = 0; i < LIST_ALPHABETS.length; i++) {
                if (mnemonic.equalsIgnoreCase(LIST_ALPHABETS[i])) {
                    labelMnemonic = LIST_ALPHABETS[i];
                    keyMnemonic = LIST_KEY_CODES[i];
                }
            }
        }
        if (labelMnemonic == null) {
            labelMnemonic = "";
        } else {
            labelMnemonic = "(" + labelMnemonic + ")";
            button.setMnemonic(keyMnemonic);
        }
        button.setText(label + labelMnemonic);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command != null) {
            _eventListener.action(command);
        }
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        close();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void close() {
        _eventListener.action(COMMAND_FILE_EXIT);
    }

    protected JFileChooser getFileChooser() {
        if (_fileChooser == null) {
            File file = new File(System.getProperty("user.dir"));
            _fileChooser = new JFileChooser(file);
        }
        return _fileChooser;
    }

    public boolean showConfirmationDialog(String message, String title) {
        int result = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    public void showErrorMessageDialog(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public File showOpenFileDialog(File dir, int mode) {
        File file = null;

        JFileChooser fileChooser = getFileChooser();
        fileChooser.setFileSelectionMode(mode);

        if (dir != null && dir.isDirectory()) {
            fileChooser.setCurrentDirectory(dir);
        }
        fileChooser.setMultiSelectionEnabled(false);

        fileChooser.resetChoosableFileFilters();
        int intent = fileChooser.showOpenDialog(this);
        if (intent == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }

        return file;
    }

    public void showModuleSelectionDialog(String[] moduleNames) {
        if (moduleNames != null) {
            _moduleSelectionDialog.setModuleNames(moduleNames);
            _moduleSelectionDialog.setLocationRelativeTo(this);
            _moduleSelectionDialog.setVisible(true);
        }
    }

    public void showModuleConfigDialog(ModulePrimitive module) {
        if (module != null) {
            _moduleConfigDialog.setEdittingModuleIndex(-1);
            _moduleConfigDialog.initTable(module);
            _moduleConfigDialog.setLocationRelativeTo(this);
            _moduleConfigDialog.setVisible(true);
        }
    }

    public void showModuleConfigEditDialog(int index) {
        ModulePrimitive module = getModule(index);

        if (module != null) {
            _moduleConfigDialog.setEdittingModuleIndex(index);
            _moduleConfigDialog.initTable(module);
            _moduleConfigDialog.setLocationRelativeTo(this);
            _moduleConfigDialog.setVisible(true);
        }
    }

    public void showProgressDialog(String title, int max, ProgressDialogWorker worker) {
        _progressDialog.init(max, worker);
        _progressDialog.setTitle(title);
    }

    public void refreshInputFilesList(String[] inputFileList) {
        if (inputFileList != null && _inputFileList != null) {
            _inputFileList.setListData(inputFileList);
            repaint();
        }
    }

    public String getInputDirPath() {
        return _tfInputDirPath.getText().trim();
    }

    public void refreshInputDirPath(String inputDirPath) {
        if (inputDirPath != null && _tfInputDirPath != null) {
            _tfInputDirPath.setText(inputDirPath);
            repaint();
        }
    }

    public String getOutputDirPath() {
        return _tfOutputDirPath.getText().trim();
    }

    public void refreshOutputDirPath(String outputDirPath) {
        if (outputDirPath != null && _tfOutputDirPath != null) {
            _tfOutputDirPath.setText(outputDirPath);
            repaint();
        }
    }

    public void refreshImageViewer(int index) {
        String parentDir = getInputDirPath();
        String filePath = getInputFilePath(index);
        filePath = FileUtil.join(parentDir, filePath);

        _imageViewer.loadImage(filePath);

        if (!_imageViewer.isVisible()) {
            _imageViewer.setVisible(true);
        }

    }

    public ModulePrimitive[] getListedModules() {
        ListModel<ModulePrimitive> model = _pipelinesList.getModel();
        ModulePrimitive[] modules = null;

        if (model != null && model.getSize() > 0) {
            int nModules = model.getSize();
            modules = new ModulePrimitive[nModules];

            for (int iModule = 0; iModule < nModules; iModule++) {
                modules[iModule] = model.getElementAt(iModule);
            }
        }

        return modules;
    }

    public String getInputFilePath(int index) {
        ListModel<String> model = _inputFileList.getModel();
        String inputFilePath = null;

        if (model != null && index >= 0 && index < model.getSize()) {
            inputFilePath = model.getElementAt(index);
        }

        return inputFilePath;
    }

    public String[] getInputFilePaths() {
        ListModel<String> model = _inputFileList.getModel();
        String[] inputFilePaths = null;

        if (model != null && model.getSize() > 0) {
            int nInputFilePaths = model.getSize();
            inputFilePaths = new String[nInputFilePaths];

            for (int iFile = 0; iFile < nInputFilePaths; iFile++) {
                inputFilePaths[iFile] = model.getElementAt(iFile);
            }
        }

        return inputFilePaths;
    }

    public ModulePrimitive getModule(int index) {
        ListModel<ModulePrimitive> model = _pipelinesList.getModel();
        int nModules = model.getSize();
        ModulePrimitive module = null;

        if (model != null && index >= 0 && index < nModules) {
            module = model.getElementAt(index);
        }

        return module;
    }

    public ModulePrimitive getSelectedModule() {
        ModulePrimitive module = null;
        if (_pipelinesList != null) {
            module = _pipelinesList.getSelectedValue();
        }
        return module;
    }

    public int getSelectedModuleindex() {
        int index = -1;
        if (_pipelinesList != null) {
            index = _pipelinesList.getSelectedIndex();
        }
        return index;
    }

    public void removeModule(int index) {
        ListModel<ModulePrimitive> model = _pipelinesList.getModel();
        ArrayList<ModulePrimitive> modules = new ArrayList<ModulePrimitive>();

        for (int iModule = 0; iModule < model.getSize(); iModule++) {
            if (iModule != index) {
                modules.add(model.getElementAt(iModule));
            }
        }

        ModulePrimitive[] array_module = new ModulePrimitive[modules.size()];
        modules.toArray(array_module);

        _pipelinesList.removeAll();
        _pipelinesList.setListData(array_module);

        repaint();
    }

    public void addModule(ModulePrimitive module, int index) {
        if (module != null) {
            ListModel<ModulePrimitive> model = _pipelinesList.getModel();
            DefaultListModel<ModulePrimitive> newModel = new DefaultListModel<ModulePrimitive>();
            boolean isEditMode = false;

            for (int iModule = 0; iModule < model.getSize(); iModule++) {
                if (iModule == index) {
                    isEditMode = true;
                    newModel.addElement(module);
                } else {
                    newModel.addElement(model.getElementAt(iModule));
                }
            }

            if (!isEditMode) {
                newModel.addElement(module);
            }

            _pipelinesList.setModel(newModel);
            repaint();
        }
    }
}
