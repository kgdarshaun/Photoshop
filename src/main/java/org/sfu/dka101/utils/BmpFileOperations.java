package org.sfu.dka101.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BmpFileOperations {
    public static JFileChooser getBmpFileChooser() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".bmp");
            }

            @Override
            public String getDescription() {
                return "BMP file";
            }
        });
        return jFileChooser;
    }

    public static BufferedImage openBmpFile(File inputFile, JFrame parentFrame) {
        BufferedImage image = null;

        if (!inputFile.getName().toLowerCase().endsWith(".bmp")) {
            JOptionPane.showMessageDialog(parentFrame, "Only uncompressed BMP file is supported (*.bmp)", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            image = ImageUtil.readImage(inputFile);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Error opening photo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(ex);
        }
        return image;
    }

    public static void saveBmpFile(BufferedImage image, File outputFile, JFrame parentFrame) {
        String fileName = outputFile.getName().toLowerCase();
        if (fileName.contains(".")) {
            if (!fileName.contains(".bmp")) {
                JOptionPane.showMessageDialog(parentFrame, "Error saving photo: Only BMP filetype supported", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            if (!fileName.endsWith(".bmp")) {
                outputFile = new File(outputFile.getParentFile(), outputFile.getName() + ".bmp");
            }
        }

        try {
            ImageIO.write(image, "BMP", outputFile);
            JOptionPane.showMessageDialog(parentFrame, "Photo saved successfully!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Error saving photo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(ex);
        }
    }
}
