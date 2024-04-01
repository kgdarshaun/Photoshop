package org.sfu.dka101.utils;

import org.sfu.dka101.exceptions.BmpFileException;

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

    public static BufferedImage openBmpFile(File inputFile) throws BmpFileException {
        BufferedImage image = null;

        if (!inputFile.getName().toLowerCase().endsWith(".bmp")) {
            throw new BmpFileException("Only uncompressed BMP file is supported (*.bmp)", new Throwable());
        }

        try {
            image = ImageUtil.readImage(inputFile);
        } catch (IOException ex) {
            throw new BmpFileException(ex.getMessage(),ex);
        }
        return image;
    }

    public static boolean saveBmpFile(BufferedImage image, File outputFile, JFrame parentFrame) throws BmpFileException {
        String fileName = outputFile.getName().toLowerCase();
        if (fileName.contains(".")) {
            if (!fileName.contains(".bmp")) {
                throw new BmpFileException("Only BMP filetype supported", new Throwable());
            }
        } else {
            if (!fileName.endsWith(".bmp")) {
                outputFile = new File(outputFile.getParentFile(), outputFile.getName() + ".bmp");
            }
        }

        if (outputFile.exists()) {
            int response = JOptionPane.showConfirmDialog(parentFrame,
                    "File already exists. Do you want to overwrite it?", "Confirm Overwrite",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (response != JOptionPane.YES_OPTION) {
                return false;
            }
        }

        try {
            ImageIO.write(image, "BMP", outputFile);
            return true;
        } catch (IOException ex) {
            throw new BmpFileException(ex.getMessage(), ex);
        }
    }
}
