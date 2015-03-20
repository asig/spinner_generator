import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpinnerGeneratorDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonSave;
    private JButton buttonQuit;
    private JTextField spinnerWidthCtrl;
    private JTextField spinnerHeightCtrl;
    private JTextField spinnerRadiusCtrl;
    private JTextField spinnerDotsCtrl;
    private JTextField dotRadiusCtrl;
    private JTextField phaseDelayCtrl;
    private SpinnerView spinnerView;
    private JButton buttonForeground;
    private JButton buttonBackground;

    private SpinnerGenerator spinnerGenerator = new SpinnerGenerator(12, 2, 20, 52, 52, Color.WHITE, Color.BLACK);

    public SpinnerGeneratorDialog() {
        $$$setupUI$$$();

        spinnerWidthCtrl.setText(Integer.toString(spinnerView.getW()));
        spinnerHeightCtrl.setText(Integer.toString(spinnerView.getH()));
        spinnerRadiusCtrl.setText(Integer.toString(spinnerView.getRadius()));
        spinnerDotsCtrl.setText(Integer.toString(spinnerView.getDots()));
        dotRadiusCtrl.setText(Integer.toString(spinnerView.getDotRadius()));
        phaseDelayCtrl.setText(Integer.toString(spinnerView.getPhaseDelay()));

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonSave);

        buttonSave.addActionListener(e -> onSave());
        buttonQuit.addActionListener(e -> onQuit());
        buttonBackground.addActionListener(e -> chooseBackgroundColor());
        buttonForeground.addActionListener(e -> chooseForegroundColor());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onQuit();
            }
        });

        spinnerWidthCtrl.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                try {
                    spinnerView.setW(Integer.parseInt(spinnerWidthCtrl.getText()));
                    pack();
                    repaint();
                } catch (NumberFormatException ex) {
                }
                spinnerWidthCtrl.setText(Integer.toString(spinnerView.getW()));
            }
        });

        spinnerHeightCtrl.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                try {
                    spinnerView.setH(Integer.parseInt(spinnerHeightCtrl.getText()));
                    pack();
                    repaint();
                } catch (NumberFormatException ex) {
                }
                spinnerHeightCtrl.setText(Integer.toString(spinnerView.getH()));
            }
        });
        spinnerRadiusCtrl.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                try {
                    spinnerView.setRadius(Integer.parseInt(spinnerRadiusCtrl.getText()));
                } catch (NumberFormatException ex) {
                }
                spinnerRadiusCtrl.setText(Integer.toString(spinnerView.getRadius()));
            }
        });
        spinnerDotsCtrl.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                try {
                    spinnerView.setDots(Integer.parseInt(spinnerDotsCtrl.getText()));
                } catch (NumberFormatException ex) {
                }
                spinnerDotsCtrl.setText(Integer.toString(spinnerView.getDots()));
            }
        });
        dotRadiusCtrl.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                try {
                    spinnerView.setDotRadius(Integer.parseInt(dotRadiusCtrl.getText()));
                } catch (NumberFormatException ex) {
                }
                dotRadiusCtrl.setText(Integer.toString(spinnerView.getDotRadius()));
            }
        });
        phaseDelayCtrl.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                try {
                    spinnerView.setPhaseDelay(Integer.parseInt(phaseDelayCtrl.getText()));
                } catch (NumberFormatException ex) {
                }
                phaseDelayCtrl.setText(Integer.toString(spinnerView.getPhaseDelay()));
            }
        });
    }

    private void chooseBackgroundColor() {
        Color c = chooseColor(spinnerGenerator.getBackground());
        spinnerGenerator.setBackground(c);
        buttonBackground.setBackground(c);
        spinnerView.repaint();
    }

    private void chooseForegroundColor() {
        Color c = chooseColor(spinnerGenerator.getForeground());
        spinnerGenerator.setForeground(c);
        buttonForeground.setBackground(c);
        spinnerView.repaint();
    }

    private Color chooseColor(Color initial) {
        final Holder<Color> color = new Holder<Color>(initial);
        final JColorChooser colorChooser = new JColorChooser(initial);

//        // Remove all panels but "RGB"
//        AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
//        for (AbstractColorChooserPanel p : panels) {
//            String displayName = p.getDisplayName();
//            if (!displayName.equals("RGB")) {
//                colorChooser.removeChooserPanel(p);
//            }
//        }
//
//        // remove the preview panel
//        colorChooser.setPreviewPanel(new JPanel());
//
        // Finally, show the dialog.
        JDialog dialog = JColorChooser.createDialog(this, "Select Color", true, colorChooser, e -> color.set(colorChooser.getColor()), null);
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                Window w = (Window) e.getComponent();
                w.dispose();
            }
        });
        dialog.setVisible(true);
        return color.get();
    }

    private void onSave() {
        try {
            ImageOutputStream os = new FileImageOutputStream(new File("/tmp/spinner.gif"));
            GifSequenceWriter writer = new GifSequenceWriter(os, BufferedImage.TYPE_INT_ARGB, spinnerView.getPhaseDelay(), true);
            for (int i = spinnerGenerator.getDots() - 1; i >= 0; i--) {
                writer.writeToSequence(spinnerGenerator.generate(i));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onQuit() {
        dispose();
    }

    public static void main(String[] args) {
        SpinnerGeneratorDialog dialog = new SpinnerGeneratorDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        spinnerView = new SpinnerView(spinnerGenerator);

        buttonForeground = new JButton();
        buttonForeground.setBorderPainted(true);
        buttonForeground.setFocusPainted(false);
        buttonForeground.setContentAreaFilled(true);
        buttonForeground.setBackground(spinnerGenerator.getForeground());

        buttonBackground = new JButton();
        buttonBackground.setBorderPainted(true);
        buttonBackground.setFocusPainted(false);
        buttonBackground.setContentAreaFilled(true);
        buttonBackground.setBackground(spinnerGenerator.getBackground());
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(4, 3, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonSave = new JButton();
        buttonSave.setText("Save");
        panel2.add(buttonSave, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonQuit = new JButton();
        buttonQuit.setText("Quit");
        panel2.add(buttonQuit, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 6, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Width:");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerWidthCtrl = new JTextField();
        spinnerWidthCtrl.setText("");
        panel3.add(spinnerWidthCtrl, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Height:");
        panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerHeightCtrl = new JTextField();
        panel3.add(spinnerHeightCtrl, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Radius:");
        panel3.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerRadiusCtrl = new JTextField();
        panel3.add(spinnerRadiusCtrl, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Dots:");
        panel3.add(label4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerDotsCtrl = new JTextField();
        spinnerDotsCtrl.setText("");
        panel3.add(spinnerDotsCtrl, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Dot radius:");
        panel3.add(label5, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dotRadiusCtrl = new JTextField();
        panel3.add(dotRadiusCtrl, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Delay (ms):");
        panel3.add(label6, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        phaseDelayCtrl = new JTextField();
        phaseDelayCtrl.setText("");
        panel3.add(phaseDelayCtrl, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Background:");
        panel3.add(label7, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Foreground:");
        panel3.add(label8, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonForeground.setText(" ");
        panel3.add(buttonForeground, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonBackground.setText(" ");
        panel3.add(buttonBackground, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        contentPane.add(spinnerView, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
