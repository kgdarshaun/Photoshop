package org.sfu.dka101.huffman;

import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanEncoding {
    public static HashMap<Integer, String> getEncodingMap(HashMap<Integer, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        for (int pixelValue : frequencyMap.keySet()) {
            priorityQueue.add(new HuffmanNode(pixelValue, frequencyMap.get(pixelValue)));
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            HuffmanNode newNode = new HuffmanNode(-1, left.frequency + right.frequency);
            newNode.left = left;
            newNode.right = right;
            priorityQueue.add(newNode);
        }

        HashMap<Integer, String> codeMap = new HashMap<>();
        buildCodeFromHuffmanTree(priorityQueue.peek(), "", codeMap);

        return codeMap;
    }

    private static void buildCodeFromHuffmanTree(HuffmanNode root, String code, HashMap<Integer, String> codeMap) {
        if (root == null) return;
        if (root.left == null && root.right == null) {
            codeMap.put(root.pixelValue, code);
            return;
        }
        buildCodeFromHuffmanTree(root.left, code + "0", codeMap);
        buildCodeFromHuffmanTree(root.right, code + "1", codeMap);
    }
}
