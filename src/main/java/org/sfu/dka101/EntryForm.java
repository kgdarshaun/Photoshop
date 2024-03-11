package org.sfu.dka101;

import org.sfu.dka101.enums.Operations;
import org.sfu.dka101.huffman.HuffmanEncoding;
import org.sfu.dka101.operations.TransformSelector;
import org.sfu.dka101.utils.Entropy;
import org.sfu.dka101.utils.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

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

    public EntryForm() {
        setContentPane(EntryPanel);
        setTitle("Photoshop");
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
                    currentImage = ImageUtil.readImage(f);
                    oldImage.setIcon(new ImageIcon(currentImage));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            updateLastOperation(Operations.OPEN);
            newImage.setText(null);
            newImage.setIcon(null);
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
                String outputText = huffmanProcessing();
                oldImage.setIcon(new ImageIcon(currentImage));
                newImage.setIcon(null);
                newImage.setText(outputText);
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
    
    public String huffmanProcessing() {
        if (ImageUtil.isImageGreyscale(currentImage)) {
            HashMap<Integer, Integer> frequencyMap = new HashMap<>();
            for (int row=0; row < currentImage.getWidth(); row++) {
                for (int col = 0; col < currentImage.getHeight(); col++) {
                    int pixelValue = new Color(currentImage.getRGB(row, col)).getRed();
                    frequencyMap.put(pixelValue, frequencyMap.getOrDefault(pixelValue, 0) + 1);
                }
            }

            HashMap<Integer, String> codeMap = HuffmanEncoding.getEncodingMap(frequencyMap);

            long imageCodeLength = 0;
            for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
                imageCodeLength += (long) entry.getValue() * codeMap.get(entry.getKey()).length();
            }

            int totalPixels = currentImage.getHeight() * currentImage.getWidth();
            float averageCodeLength = (float) imageCodeLength / totalPixels;
            double entropy = Entropy.getEntropy(new ArrayList<>(frequencyMap.values()));

            return String.format("Entropy = %.4f \n Average Huffman Code Length = %.4f", entropy, averageCodeLength);
        } else {
            ErrorDialog huffmanDialog = new ErrorDialog();
            huffmanDialog.setDialogLabelText("The image is not Grayscale!");
            huffmanDialog.pack();
            huffmanDialog.setLocationRelativeTo(this);
            huffmanDialog.setVisible(true);
            return "";
        }
    }

    public static void main(String[] args) {
        new EntryForm();
    }
}
