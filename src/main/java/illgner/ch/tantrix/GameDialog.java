package illgner.ch.tantrix;

import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameDialog extends JDialog {

    private static final Logger LOGGER = Logger.getLogger(GameDialog.class.getName());

    Game game = new Game(this);

    public GameDialog() throws HeadlessException {
        setTitle("HexaTiles");
        setSize(600, 550);
        Container contentPane = getContentPane();
        JPanel mainPanel = new JPanel();
        contentPane.add(new JScrollPane(mainPanel));
        mainPanel.setLayout(new BorderLayout());
        JButton startButton = new JButton("start");
        JButton screenshotButton = new JButton("screenshot");
        final JPanel gamePanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                game.draw(g2);
            }
        };
        final JSpinner levelSpinner = new JSpinner(new SpinnerNumberModel(15, 3, Piece.Pieces.length, 1));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(screenshotButton);
        buttonPanel.add(startButton);
        buttonPanel.add(levelSpinner);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        startButton.addActionListener(e -> {
            Thread t = new Thread(() -> {
                long start = System.currentTimeMillis();
                boolean b = game.start((Integer) levelSpinner.getValue());
                LOGGER.info("Calculation time: " + (System.currentTimeMillis() - start) + "ms");
                repaint();
                LOGGER.info("Finished " + b);
            });
            t.start();
        });
        screenshotButton.addActionListener(e -> {
            try {
                Robot r = new Robot();
                BufferedImage image = r.createScreenCapture(gamePanel.getBounds());
                ImageWriter iw = ImageIO.getImageWritersByFormatName("jpg").next();
                File f = new File("test.jpg");
                ImageOutputStream ios = ImageIO.createImageOutputStream(f);
                iw.setOutput(ios);
                iw.write(image);
            } catch (AWTException | IOException e1) {
                LOGGER.severe("Could not write screenshot: " + e1.getMessage());
            }
        });
    }

    /**
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        GameDialog dlg = new GameDialog();
        dlg.setVisible(true);
    }

}
