import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class SpinnerGenerator {
    private int radius;
    private int w;
    private int h;
    private int dots;
    private int dotRadius;
    private Color foreground;
    private Color background;

    public SpinnerGenerator(int dots, int dotRadius, int radius, int w, int h, Color background, Color foreground) {
        this.dots = dots;
        this.dotRadius = dotRadius;
        this.radius = radius;
        this.w = w;
        this.h = h;
        this.background = background;
        this.foreground = foreground;
    }

    public int getDots() {
        return dots;
    }

    public int getRadius() {
        return radius;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setDots(int dots) {
        this.dots = dots;
    }

    public int getDotRadius() {
        return dotRadius;
    }

    public void setDotRadius(int dotRadius) {
        this.dotRadius = dotRadius;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    private double computeDotRadius(double p) {
        double s = (p >= 8) ?  1: 1 +  Math.exp(-Math.pow((p-4)/2,2));
        return dotRadius * s;
    }

    protected BufferedImage generate(int phase) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D)img.getGraphics();

        g2d.setColor(background);
        g2d.fillRect(0, 0, w, h);

        int cx = w/2;
        int cy = h/2;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(foreground);
        double angle = 2*Math.PI/dots;
        for (int i = 0; i < dots; i++) {
            int x = (int)(radius * Math.sin(i*angle));
            int y = (int) (radius * Math.cos(i*angle));
            int w = (int)(2 * computeDotRadius((phase + i) % dots));
            g2d.fillOval(cx + x - w / 2, cy - y - w /2,w,w);
        }
        return img;
    }


}
