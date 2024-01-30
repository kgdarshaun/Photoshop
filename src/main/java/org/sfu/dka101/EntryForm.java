package org.sfu.dka101;

import org.sfu.dka101.enums.Operations;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class EntryForm extends JFrame{

    private BufferedImage bufferedImage;

    private Operations lastOperation;

    private JPanel EntryPanel;
    private JButton openFileButton;
    private JButton exitButton;
    private JButton grayscaleButton;
    private JButton huffmanButton;
    private JButton orderedDitheringButton;
    private JButton autoLevelButton;
    private JButton button1;
    private JButton button2;
    private JLabel image1;
    private JLabel image2;
    private JPanel Images;
    private JPanel OptOperations;
    private JPanel CoreOperations;
    private JPanel OpenClose;

    public EntryForm() {
        setContentPane(EntryPanel);
        setTitle("Test Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(2048, 1536);
        setLocationRelativeTo(null);
        setVisible(true);
        openFileButton.requestFocus();

        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                if (jFileChooser.showOpenDialog(EntryForm.this) == JFileChooser.APPROVE_OPTION) {
                    File f = jFileChooser.getSelectedFile();
                    try {
                        bufferedImage = ImageIO.read(f);
                        image1.setIcon(new ImageIcon(bufferedImage));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        grayscaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bufferedImage != null) {
                    ColorModel colorModel = bufferedImage.getColorModel();
                    boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
                    WritableRaster writableRaster = bufferedImage.copyData(null);
                    BufferedImage oldImage = new BufferedImage(colorModel, writableRaster, isAlphaPremultiplied, null);

                    for (int row=0; row < bufferedImage.getWidth(); row++) {
                        for (int col = 0; col < bufferedImage.getHeight(); col++) {
                            int rgbValue = bufferedImage.getRGB(row, col);
                            float rValue = new Color(rgbValue).getRed();
                            float gValue = new Color(rgbValue).getBlue();
                            float bValue = new Color(rgbValue).getGreen();
                            float aValue = new Color(rgbValue).getAlpha();
                            int grayscale = (int) ((0.299 * rValue) + (0.587 * gValue) + (0.114 * bValue));
                            bufferedImage.setRGB(row, col, new Color(grayscale, grayscale, grayscale).getRGB());
                        }
                    }
                    image1.setIcon(new ImageIcon(oldImage));
                    image2.setIcon(new ImageIcon(bufferedImage));
                    enableLastOperation();
                    lastOperation = Operations.GRAYSCALE;
                    disableLastOperation();
                }
            }
        });
    }

    public void enableLastOperation() {
        changeLastOperationStatus(true);
    }

    public void disableLastOperation() {
        changeLastOperationStatus(false);
    }

    public void changeLastOperationStatus(boolean value) {
        if (lastOperation != null)
            switch (lastOperation){
                case GRAYSCALE -> grayscaleButton.setEnabled(value);
            }
    }

    public static void main(String[] args) {
        new EntryForm();
    }
}
