package org.sfu.dka101;

import org.sfu.dka101.enums.Operations;
import org.sfu.dka101.exceptions.BmpFileException;
import org.sfu.dka101.huffman.EncodeImage;
import org.sfu.dka101.operations.TransformSelector;
import org.sfu.dka101.utils.BmpFileOperations;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

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
    private JButton lensBlurButton;
    private JButton vignetteButton;
    private JLabel oldImage;
    private JLabel newImage;
    private JPanel Images;
    private JPanel OptOperations;
    private JPanel CoreOperations;
    private JPanel OpenClose;
    private JButton saveFileButton;

    public EntryForm() {
        setContentPane(EntryPanel);
        setTitle("Photoshop");
        setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(2048, 1536);
        setLocationRelativeTo(null);
        setVisible(true);
        openFileButton.requestFocus();

        openFileButton.addActionListener(e -> {
            openFileAction();
        });

        saveFileButton.addActionListener(e -> {
            saveFileAction();
        });

        exitButton.addActionListener(e -> System.exit(0));
        
        grayscaleButton.addActionListener(e -> eventListener(Operations.GRAYSCALE));
        orderedDitheringButton.addActionListener(e -> eventListener(Operations.ORDERED_DITHERING));
        autoLevelButton.addActionListener(e -> eventListener(Operations.AUTO_LEVEL));
        lensBlurButton.addActionListener(e-> eventListener(Operations.LENS_BLUR));
        vignetteButton.addActionListener(e -> eventListener(Operations.VIGNETTE));
        huffmanButton.addActionListener(e -> eventListener(Operations.HUFFMAN));
    }

    private void eventListener(Operations operation){
        if (currentImage != null) {
            if (operation == Operations.HUFFMAN) {
                try {
                    String outputText = EncodeImage.getHuffmanEncodeDetails(currentImage);
                    oldImage.setIcon(new ImageIcon(currentImage));
                    newImage.setIcon(null);
                    newImage.setText(outputText);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Cannot compute Huffman: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                BufferedImage oldImage = currentImage;
                currentImage = TransformSelector.selectTransform(operation).transform(currentImage);
                updateImageInForm(oldImage);
                updateLastOperation(operation);
            }
        }
    }

    private void updateImageInForm(BufferedImage oldImage) {
        this.oldImage.setIcon(new ImageIcon(oldImage));
        newImage.setText(null);
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
                case ORDERED_DITHERING -> orderedDitheringButton.setEnabled(value);
                case AUTO_LEVEL -> autoLevelButton.setEnabled(value);
                case LENS_BLUR -> lensBlurButton.setEnabled(value);
            }
    }

    private void openFileAction() {
        JFileChooser bmpFileChooser = BmpFileOperations.getBmpFileChooser();
        if (bmpFileChooser.showOpenDialog(EntryForm.this) == JFileChooser.APPROVE_OPTION) {
            File inputFile = bmpFileChooser.getSelectedFile();
            try {
                currentImage = BmpFileOperations.openBmpFile(inputFile);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(currentImage != null){
            oldImage.setIcon(new ImageIcon(currentImage));
            newImage.setText(null);
            newImage.setIcon(null);
            updateLastOperation(Operations.FILE);
        }
    }

    private void saveFileAction() {
        if (newImage.getIcon() == null) {
            JOptionPane.showMessageDialog(this, "Not photo to save. Please perform operations", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JFileChooser bmpFileChooser = BmpFileOperations.getBmpFileChooser();
            if (bmpFileChooser.showSaveDialog(EntryForm.this) == JFileChooser.APPROVE_OPTION) {
                File outputFile = bmpFileChooser.getSelectedFile();
                try {
                    if (BmpFileOperations.saveBmpFile(currentImage, outputFile, this)) {
                        JOptionPane.showMessageDialog(this, "Saved Photo successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        updateLastOperation(Operations.FILE);
                    }
                } catch (BmpFileException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static void main(String[] args) {
        new EntryForm();
    }
}
