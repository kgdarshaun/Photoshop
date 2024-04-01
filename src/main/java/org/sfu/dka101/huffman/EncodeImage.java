package org.sfu.dka101.huffman;

import org.sfu.dka101.exceptions.HuffmanException;
import org.sfu.dka101.utils.Entropy;
import org.sfu.dka101.utils.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EncodeImage {
    public static String getHuffmanEncodeDetails(BufferedImage image) throws HuffmanException {
        if (ImageUtil.isImageGreyscale(image)) {
            HashMap<Integer, Integer> frequencyMap = new HashMap<>();
            for (int row=0; row < image.getWidth(); row++) {
                for (int col = 0; col < image.getHeight(); col++) {
                    int pixelValue = new Color(image.getRGB(row, col)).getRed();
                    frequencyMap.put(pixelValue, frequencyMap.getOrDefault(pixelValue, 0) + 1);
                }
            }

            HashMap<Integer, String> codeMap = HuffmanEncoding.getEncodingMap(frequencyMap);

            long imageCodeLength = 0;
            for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
                imageCodeLength += (long) entry.getValue() * codeMap.get(entry.getKey()).length();
            }

            int totalPixels = image.getHeight() * image.getWidth();
            float averageCodeLength = (float) imageCodeLength / totalPixels;
            double entropy = Entropy.getEntropy(new ArrayList<>(frequencyMap.values()));

            return String.format("Entropy = %.4f \n Average Huffman Code Length = %.4f", entropy, averageCodeLength);
        } else {
            throw new HuffmanException("Image is not grayscale!", new Throwable());
        }
    }
}
