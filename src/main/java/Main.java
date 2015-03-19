import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Main extends JPanel {

    private final int dots = 12;
    private final int SIZE = 52;
    private final SpinnerGenerator spinnerGenerator = new SpinnerGenerator(dots, 20, SIZE, SIZE);
    private int ofs = 0;

    public Main() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        int delay = 75; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                repaint();
            }
        };
        new Timer(delay, taskPerformer).start();
    }

    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        ofs = (ofs + dots - 1) % dots;
        BufferedImage img = spinnerGenerator.generate(ofs);

        Rectangle bounds = this.getBounds();
        int px = bounds.x + bounds.width/2 - SIZE/2;
        int py = bounds.y + bounds.height/2 - SIZE/2;
        g2d.drawImage(img, px, py, SIZE, SIZE, null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? " +
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Spinner Generator");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new Main());
        f.pack();
        f.setVisible(true);
    }
}

