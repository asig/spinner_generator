import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends JPanel {

    private double R = 20;
    private int dots = 12;
    private int ofs = 0;

    public Main() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        int delay = 75; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ofs = (ofs + dots - 1) % dots;
                repaint();
            }
        };
        new Timer(delay, taskPerformer).start();
    }

    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }

    private double getRadius(int p) {
        double x = p;
        double s = (p >= 8) ?  1: 1 +  Math.exp(-Math.pow((x-4)/2,2));
        return 2 * s;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        Rectangle bounds = this.getBounds();
        g.setColor(Color.WHITE);
        g2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        int cx = bounds.x + bounds.width/2;
        int cy = bounds.y + bounds.height/2;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        double angle = 2*Math.PI/dots;
        for (int i = 0; i < dots; i++) {
            int x = (int)(R * Math.sin(i*angle));
            int y = (int) (R * Math.cos(i*angle));
            int w = (int)(2 * getRadius((ofs + i) % dots));
            g.fillOval(cx+x-w/2, cy-y-w/2,w,w);
        }
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
        JFrame f = new JFrame("Swing Paint Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new Main());
        f.pack();
        f.setVisible(true);
    }
}

