package ImagePipeline.control;

import java.awt.Font;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.swing.JFileChooser;

import ImagePipeline.resource.ResourceManager;
import ImagePipeline.view.MainFrame;

public class MainControl implements MainFrameEventListener {
    private static final String[] DEFAULT_AVAILABLE_EXTENSIONS = {
            "png",
            "jpg",
            "jpeg"
    };

    private Logger _logger;
    private ResourceManager _rm;
    private MainFrame _mainFrame;

    public MainControl(String[] args) {
        _logger = Logger.getLogger(getClass().getName());
        _logger.setLevel(Level.INFO);
        _rm = ResourceManager.getInstance();

        init();
    }

    private void init() {
        // _modelControl = new ModelControl(this);

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
            _logger.info("FolderPath=" + file.getAbsolutePath());
            String[] inputFilePaths = findFilesRecusively(file.getAbsolutePath(), DEFAULT_AVAILABLE_EXTENSIONS);
            if (inputFilePaths != null) {
                _mainFrame.refreshInputFilesList(inputFilePaths);
                _logger.info("Found " + inputFilePaths.length + " files.");
            }
        }
    }

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
            inputFilePaths = new String[inputFiles.length];

            for (int i = 0; i < inputFiles.length; i++) {
                inputFilePaths[i] = inputFiles[i].getAbsolutePath();
            }
        }

        return inputFilePaths;
    }

    private void exit() {
        _logger.info("Exit program");
        System.exit(0);
    }

    private boolean showConfirmationDialog(String message, String title) {
        return _mainFrame.showConfirmationDialog(message, title);
    }
}
