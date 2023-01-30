package ImagePipeline.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.logging.Logger;

import ImagePipeline.control.MainFrameEventListener;
import ImagePipeline.resource.ResourceManager;
import ImagePipeline.util.Common;

public class ImageViewerDialog extends JFrame implements ActionListener {
    private static final int DEFAULT_WINDOW_WIDTH = 1200;
    private static final int DEFAULT_WINDOW_HEIGHT = 800;
    private static final int MIN_WINDOW_WIDTH = 400;
    private static final int MIN_WINDOW_HEIGHT = 400;
    private static final String KEY_PRE = "dialog.imageViewer.";

    private Logger _logger;
    protected ResourceManager _rm;
    private MainFrameEventListener _eventListener;

    private ImageCanvas _canvas;

    public ImageViewerDialog(JFrame parent, MainFrameEventListener eventListener) {
        super();
        _logger = Logger.getLogger(getClass().getName());
        _logger.setLevel(Common.GLOBAL_LOG_LEVEL);

        _rm = ResourceManager.getInstance();
        _eventListener = eventListener;

        setPreferredSize(new Dimension(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT));
        setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));
        setWindowTitle(_rm.getString(KEY_PRE + "title"));
        buildUI();

        pack();
        setLocationByPlatform(true);
    }

    private void buildUI() {
        _canvas = new ImageCanvas();

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(_canvas, BorderLayout.CENTER);
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

    public void loadImage(String filePath) {
        if (filePath != null) {
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                _logger.warning("Failed to load image: " + filePath);
            }
            _canvas.setImage(img);
        }
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        _logger.info("[ImageViewerDialog] Action on command : " + command);

        if (command.equalsIgnoreCase("")) {
        } else {
            _logger.info("[ImageViewerDialog] nothing to do");
        }
    }
}

class ImageCanvas extends JPanel {
    BufferedImage _img = null;
    private Logger _logger;

    public ImageCanvas() {
        super(true);

        _logger = Logger.getLogger(getClass().getName());
        _logger.setLevel(Common.GLOBAL_LOG_LEVEL);
        setBackground(Color.black);
    }

    public void setImage(BufferedImage img) {
        if (img != null) {
            _img = img;
        }
    }

    public void paintComponent(Graphics g) {
        if (_img != null) {
            int canvasWidth = getSize().width;
            int canvasHeight = getSize().height;
            int imgWidth = _img.getWidth();
            int imgHeight = _img.getHeight();
            int width, height;

            if ((double) canvasWidth / canvasHeight > (double) imgWidth / imgHeight) {
                height = canvasHeight;
                width = imgWidth * canvasHeight / imgHeight;
            } else {
                width = canvasWidth;
                height = imgHeight * canvasWidth / imgWidth;
            }

            g.drawImage(
                    _img,
                    (canvasWidth - width) / 2,
                    (canvasHeight - height) / 2,
                    width,
                    height,
                    this);
        }
    }
}