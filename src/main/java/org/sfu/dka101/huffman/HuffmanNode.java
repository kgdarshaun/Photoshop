package org.sfu.dka101.huffman;

public class HuffmanNode implements Comparable<HuffmanNode> {
    int pixelValue;
    int frequency;
    HuffmanNode left, right;

    public HuffmanNode(int pixelValue, int frequency) {
        this.pixelValue = pixelValue;
        this.frequency = frequency;
        left = right = null;
    }

    public int compareTo(HuffmanNode node) {
        return this.frequency - node.frequency;
    }
}
