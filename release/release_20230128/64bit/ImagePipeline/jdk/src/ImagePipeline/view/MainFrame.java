package ImagePipeline.view;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import com.formdev.flatlaf.FlatLightLaf;
import com.jogamp.opengl.awt.GLJPanel;
import javax.swing.UIManager;
import javax.swing.event.AncestorListener;
import javax.swing.AbstractButton;
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
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import ImagePipeline.control.MainFrameEventListener;
import ImagePipeline.resource.ResourceManager;
import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame implements ActionListener, WindowListener {
    private static final int DEFAULT_WINDOW_WIDTH = 800;
    private static final int DEFAULT_WINDOW_HEIGHT = 600;
    private static final int MIN_WINDOW_WIDTH = 600;
    private static final int MIN_WINDOW_HEIGHT = 400;
    private static final String LABEL_PRE = "label.";

    public static final String COMMAND_FILE = "menu.file";
    public static final String COMMAND_FILE_OPEN_FOLDER = "menu.file.openFolder";
    public static final String COMMAND_FILE_EXIT = "menu.file.exit";

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
    protected JList<String> _pipelinesList;
    protected JFileChooser _fileChooser;

    public MainFrame(MainFrameEventListener eventListener) {
        super();
        _logger = Logger.getLogger(getClass().getName());
        _logger.setLevel(Level.INFO);

        addWindowListener(this);
        _rm = ResourceManager.getInstance();
        _setCommandsOfButton = new HashSet<String>();
        _eventListener = eventListener;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setWindowTitle("ImagePipeline");
        buildUI();
        setPreferredSize(new Dimension(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT));
        setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));

        pack();
        setLocationRelativeTo(null);

    }

    public void setWindowTitle(String title) {
        if (title != null) {
            super.setTitle(title);
        }
    }

    private void buildUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        buildMenu();
        JPanel mainPanel = buildMainPanel();

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(mainPanel, BorderLayout.CENTER);
    }

    private void buildMenu() {
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        //@formatter:off
        JMenu       menuFile                = createMenu(    COMMAND_FILE,                    "F");
        JMenuItem   miFileOpenFolder        = createMenuItem(COMMAND_FILE_OPEN_FOLDER,        "O");
        JMenuItem   miFileExit              = createMenuItem(COMMAND_FILE_EXIT,               "X");
        //@formatter:on

        menubar.add(menuFile);
        menuFile.add(miFileOpenFolder);
        menuFile.add(miFileExit);
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

        JPanel panelInputFile = new JPanel(new MigLayout("", "grow", "grow"));
        String title = _rm.getString(strPre + "title");
        panelInputFile.setBorder(new TitledBorder(title));

        _inputFileList = new JList<String>();
        _inputFileList.setBorder(new EtchedBorder());
        JScrollPane scrollPaneForInputFileList = new JScrollPane(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForInputFileList.setViewportView(_inputFileList);

        panelInputFile.add(scrollPaneForInputFileList, "grow");
        return panelInputFile;
    }

    private JPanel buildPipelinesPanel() {
        String strPre = LABEL_PRE + "pipelines.";
        JPanel panelPipelines = new JPanel(new MigLayout("wrap 1", "[left, right, fill, grow]",
                "[2cm!, top, bottom, fill][top, bottom, fill, grow][2cm!, top, bottom, fill]"));
        String title = _rm.getString(strPre + "title");
        panelPipelines.setBorder(new TitledBorder(title));

        // setting
        // ==============================================================================
        JPanel panelPipelineSetting = new JPanel(new MigLayout("", "grow", "grow"));
        panelPipelineSetting.setBorder(new EtchedBorder());

        // Pipeline lists
        // =======================================================================
        _pipelinesList = new JList<String>();
        _pipelinesList.setBorder(new EtchedBorder());
        JScrollPane scrollPaneForPipelinesList = new JScrollPane(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForPipelinesList.setViewportView(_pipelinesList);
        scrollPaneForPipelinesList.setBorder(new EtchedBorder());

        // run
        // ==================================================================================
        JPanel panelRunPipeline = new JPanel(new MigLayout("", "grow", "grow"));
        panelRunPipeline.setBorder(new EtchedBorder());

        panelPipelines.add(panelPipelineSetting);
        panelPipelines.add(scrollPaneForPipelinesList);
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
        Object source = e.getSource();
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

    public void refreshInputFilesList(String[] inputFileList) {
        if (inputFileList != null) {
            _inputFileList.setListData(inputFileList);
            repaint();
        }
    }
}
