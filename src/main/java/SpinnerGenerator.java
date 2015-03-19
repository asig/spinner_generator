import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class SpinnerGenerator {
    private final int dots;
    private final double radius;
    private final int w;
    private final int h;

    public SpinnerGenerator(int dots, double radius, int w, int h) {
        this.dots = dots;
        this.radius = radius;
        this.w = w;
        this.h = h;
    }

    private double getRadius(double p) {
        double s = (p >= 8) ?  1: 1 +  Math.exp(-Math.pow((p-4)/2,2));
        return 2 * s;
    }

    protected BufferedImage generate(int phase) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D)img.getGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);

        int cx = w/2;
        int cy = h/2;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        double angle = 2*Math.PI/dots;
        for (int i = 0; i < dots; i++) {
            int x = (int)(radius * Math.sin(i*angle));
            int y = (int) (radius * Math.cos(i*angle));
            int w = (int)(2 * getRadius((phase + i) % dots));
            g2d.fillOval(cx + x - w / 2, cy - y - w /2,w,w);
        }
        return img;
    }


}
