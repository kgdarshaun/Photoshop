package org.sfu.dka101;

import org.sfu.dka101.enums.Operations;
import org.sfu.dka101.operations.TransformSelector;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EntryForm extends JFrame{

    private BufferedImage currentImage;

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
    private JLabel oldImage;
    private JLabel newImage;
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

        openFileButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            if (jFileChooser.showOpenDialog(EntryForm.this) == JFileChooser.APPROVE_OPTION) {
                File f = jFileChooser.getSelectedFile();
                try {
                    currentImage = ImageIO.read(f);
                    oldImage.setIcon(new ImageIcon(currentImage));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            updateLastOperation(Operations.OPEN);
            newImage.setIcon(null   );
        });

        exitButton.addActionListener(e -> System.exit(0));
        
        grayscaleButton.addActionListener(e -> {
            Operations operation = Operations.GRAYSCALE;
            if (currentImage != null) {
                BufferedImage oldImage = currentImage;
                currentImage = TransformSelector.selectTransform(operation).transform(currentImage);
                updateImageInForm(oldImage);
                updateLastOperation(operation);
            }
        });
    }

    private void updateImageInForm(BufferedImage oldImage) {
        this.oldImage.setIcon(new ImageIcon(oldImage));
        newImage.setIcon(new ImageIcon(currentImage));
    }

    private void updateLastOperation(Operations operation) {
        enableLastOperation();
        lastOperation = operation;
        disableLastOperation();
    }

    private void enableLastOperation() {
        changeLastOperationStatus(true);
    }

    private void disableLastOperation() {
        changeLastOperationStatus(false);
    }

    private void changeLastOperationStatus(boolean value) {
        if (lastOperation != null)
            switch (lastOperation){
                case GRAYSCALE -> grayscaleButton.setEnabled(value);
            }
    }

    public static void main(String[] args) {
        new EntryForm();
    }
}
