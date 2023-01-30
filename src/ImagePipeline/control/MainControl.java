package ImagePipeline.control;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

import ImagePipeline.model.primitives.ModulePrimitive;
import ImagePipeline.resource.ResourceManager;
import ImagePipeline.view.MainFrame;
import ImagePipeline.view.ModuleSelectionDialog;
import ImagePipeline.view.SimpleProgressDialog;

public class MainControl implements MainFrameEventListener {
    private static final String[] DEFAULT_AVAILABLE_EXTENSIONS = {
            "png",
            "jpg",
            "jpeg",
            "JPG",
            "JPEG"
    };

    private Logger _logger;
    private ResourceManager _rm;
    private MainFrame _mainFrame;
    private ModelControl _modelControl;

    public MainControl(String[] args) {
        _logger = Logger.getLogger(getClass().getName());
        _logger.setLevel(Level.INFO);
        _rm = ResourceManager.getInstance();

        init();
    }

    private void init() {
        _modelControl = new ModelControl(this);
        _mainFrame = new MainFrame(this);

        _mainFrame.setLocationRelativeTo(null);
        _mainFrame.setVisible(true);
    }

    public void start() {
        _logger.info("Called MainControl.start!");
        setEnabled(true);
    }

    private void setEnabled(boolean isEnabled) {
        if (_mainFrame != null) {
            _mainFrame.setEnabled(isEnabled);
        }
    }

    @Override
    public void action(String command) {
        _logger.info("Action on command : " + command);
        if (command.equalsIgnoreCase(MainFrame.COMMAND_FILE_EXIT)) {
            exitWithConfirmation();
        } else if (command.equalsIgnoreCase(MainFrame.COMMAND_FILE_OPEN_FOLDER)) {
            loadFilesFromFolder();
        } else if (command.equalsIgnoreCase(MainFrame.COMMAND_OPEN_FILE_DIALOG_OUTPUT_DIR_PATH)) {
            showFileDialogToGetOutputDirPath();
        } else if (command.equalsIgnoreCase(MainFrame.COMMAND_ADD_MODULE)) {
            showModuleSelectionDialog();
        } else if (command.equalsIgnoreCase(MainFrame.COMMAND_REMOVE_MODULE)) {
            removeModule();
        } else if (command.equalsIgnoreCase(MainFrame.COMMAND_RUN_PIPELINE)) {
            runPipeline();
        } else {
            _logger.info("nothing to do");
        }
    }

    @Override
    public void action(String command, String value) {
        _logger.info("Action on command : " + command + ", Value: " + value);
        if (command.equalsIgnoreCase(ModuleSelectionDialog.COMMAND_ADD)) {
            showModuleConfigDialog(value);
        } else {
            _logger.info("nothing to do");
        }
    }

    @Override
    public void action(String command, int value) {
        _logger.info("Action on command : " + command + ", Value: " + value);
        if (command.equalsIgnoreCase(MainFrame.COMMAND_EDIT_MODULE)) {
            showModuleConfigEditDialog(value);
        } else if (command.equalsIgnoreCase(MainFrame.COMMAND_UPDATE_IMAGE_VIEWER)) {
            _mainFrame.refreshImageViewer(value);
        } else {
            _logger.info("nothing to do");
        }
    }

    private void exitWithConfirmation() {
        String message = _rm.getString("exit.message");
        String title = _rm.getString("exit.title");
        boolean toExit = showConfirmationDialog(message, title);
        if (toExit) {
            exit();
        }
    }

    private void loadFilesFromFolder() {
        File file = _mainFrame.showOpenFileDialog(null, JFileChooser.DIRECTORIES_ONLY);

        if (file != null) {
            _logger.info("InputFolderPath=" + file.getAbsolutePath());
            String[] inputFilePaths = findFilesRecusively(file.getAbsolutePath(), DEFAULT_AVAILABLE_EXTENSIONS);
            if (inputFilePaths != null) {
                _mainFrame.refreshInputDirPath(file.getAbsolutePath());
                _mainFrame.refreshInputFilesList(inputFilePaths);
                _logger.info("Found " + inputFilePaths.length + " files.");
            }
        }
    }

    private void showFileDialogToGetOutputDirPath() {
        File previousDir = new File(_mainFrame.getOutputDirPath());
        File file = _mainFrame.showOpenFileDialog(previousDir, JFileChooser.DIRECTORIES_ONLY);

        if (file != null) {
            _logger.info("OutputFolderPath=" + file.getAbsolutePath());
            _mainFrame.refreshOutputDirPath(file.getAbsolutePath());
        }
    }

    private void showModuleSelectionDialog() {
        String[] moduleNames = _modelControl.getAllModuleNames();
        if (moduleNames != null) {
            _mainFrame.showModuleSelectionDialog(moduleNames);
        }
    };

    private void showModuleConfigDialog(String moduleName) {
        ModulePrimitive module = ModelControl.createModule(moduleName);

        if (moduleName != null) {
            _mainFrame.showModuleConfigDialog(module);
        }
    };

    private void showModuleConfigEditDialog(int index) {
        _mainFrame.showModuleConfigEditDialog(index);
    };

    public SimpleProgressDialog showProgressBarLoadingImage(String title, String message, int min, int max) {
        return _mainFrame.showProgressBarLoadingImage(title, message, min, max);
    }

    public void addModule(ModulePrimitive module, int index) {
        if (module != null) {
            _mainFrame.addModule(module, index);
        }
    }

    private void removeModule() {
        int index = _mainFrame.getSelectedModuleindex();
        _logger.info("Remove module : index=" + index);

        if (index >= 0) {
            _mainFrame.removeModule(index);
        } else {
            String message = _rm.getString("error.moduleUnselected.message");
            String title = _rm.getString("error.moduleUnselected.title");
            _mainFrame.showErrorMessageDialog(message, title);
        }
    };

    private void runPipeline() {
        String[] inputFilePaths = _mainFrame.getInputFilePaths();
        ModulePrimitive[] moduleLists = _mainFrame.getListedModules();
        String inputDirPath = _mainFrame.getInputDirPath();
        String outputDirPath = _mainFrame.getOutputDirPath();

        if (inputDirPath == null || inputDirPath.length() <= 0) {
            String message = _rm.getString("error.unselectedInputDirPath.message");
            String title = _rm.getString("error.unselectedInputDirPath.title");
            _mainFrame.showErrorMessageDialog(message, title);
            return;
        }

        if (inputFilePaths == null) {
            String message = _rm.getString("error.nullInputFilePaths.message");
            String title = _rm.getString("error.nullInputFilePaths.title");
            _mainFrame.showErrorMessageDialog(message, title);
            return;
        }

        if (outputDirPath == null || outputDirPath.length() <= 0) {
            String message = _rm.getString("error.unselectedOutputDirPath.message");
            String title = _rm.getString("error.unselectedOutputDirPath.title");
            _mainFrame.showErrorMessageDialog(message, title);
            return;
        }

        if (moduleLists == null) {
            String message = _rm.getString("error.nullModuleLists.message");
            String title = _rm.getString("error.nullModuleLists.title");
            _mainFrame.showErrorMessageDialog(message, title);
            return;
        }

        _modelControl.runPipeline(inputDirPath, inputFilePaths, moduleLists, outputDirPath);
    };

    private String[] findFilesRecusively(String folderPath, String[] extensions) {
        File rootFolder = new File(folderPath);
        File[] inputFiles = rootFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                boolean isAcceptable = false;
                for (int i = 0; i < extensions.length; i++) {
                    if (name.endsWith("." + extensions[i])) {
                        isAcceptable = true;
                        break;
                    }
                }
                return isAcceptable;
            }
        });

        String[] inputFilePaths = null;
        if (inputFiles != null) {
            URI rootFolderURI = rootFolder.toURI();
            inputFilePaths = new String[inputFiles.length];

            for (int i = 0; i < inputFiles.length; i++) {
                inputFilePaths[i] = rootFolderURI.relativize(inputFiles[i].toURI()).toString();
            }
        }

        return inputFilePaths;
    }

    private void exit() {
        _logger.info("Exit program");
        System.exit(0);
    }

    public void showErrorMessageDialog(String message, String title) {
        _mainFrame.showErrorMessageDialog(message, title);
    }

    public boolean showConfirmationDialog(String message, String title) {
        return _mainFrame.showConfirmationDialog(message, title);
    }
}
