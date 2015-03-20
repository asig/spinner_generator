// Copyright 2015 Andreas Signer. All rights reserved.

import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class SpinnerView extends JLabel {

    private final SpinnerGenerator generator;
    private final Timer timer;
    private int phaseDelay;
    private int phase;

    public SpinnerView(final SpinnerGenerator generator) {
        this.generator = generator;
        this.phase = 0;
        this.phaseDelay = 75;
        this.timer = new Timer(this.phaseDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                phase = (phase + generator.getDots()-1) % generator.getDots();
                repaint();
            }
        });
        this.timer.start();
    }

    public int getDots() {
        return generator.getDots();
    }

    public int getRadius() {
        return generator.getRadius();
    }

    public int getW() {
        return generator.getW();
    }

    public int getH() {
        return generator.getH();
    }

    public void setRadius(int radius) {
        generator.setRadius(radius);
    }

    public void setW(int w) {
        generator.setW(w);
    }

    public void setH(int h) {
        generator.setH(h);
    }

    public void setDots(int dots) {
        generator.setDots(dots);
    }

    public int getDotRadius() {
        return generator.getDotRadius();
    }

    public void setDotRadius(int dotRadius) {
        generator.setDotRadius(dotRadius);
    }

    public int getPhaseDelay() {
        return phaseDelay;
    }

    public void setPhaseDelay(int phaseDelay) {
        this.phaseDelay = phaseDelay;
        timer.setDelay(phaseDelay);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        BufferedImage img = generator.generate(phase);
        Rectangle bounds = this.getBounds();
        int px =  bounds.width/2 - generator.getW()/2;
        int py = bounds.height/2 - generator.getH()/2;
        g2d.drawImage(img, px, py, generator.getW(), generator.getH(), null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(generator.getW(), generator.getH());
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(generator.getW(), generator.getH());
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(generator.getW(), generator.getH());
    }

}
