package com.datastructure.visualizer.model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTree {
    public static class HuffmanNode implements Comparable<HuffmanNode> {
        public char character;
        public int frequency;
        public HuffmanNode left, right;

        public HuffmanNode(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public int compareTo(HuffmanNode other) {
            return this.frequency - other.frequency;
        }

        @Override
        public String toString() {
            return character == '\0' ?
                    "Freq:" + frequency :
                    "'" + character + "'(" + frequency + ")";
        }
    }

    private HuffmanNode root;
    private Map<Character, String> huffmanCodes;
    private String originalText;

    public void buildTree(String text) {
        if (text == null || text.isEmpty()) return;

        this.originalText = text;

        // 计算字符频率
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        // 创建优先队列
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            queue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        // 构建哈夫曼树
        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();

            HuffmanNode parent = new HuffmanNode('\0', left.frequency + right.frequency);
            parent.left = left;
            parent.right = right;

            queue.add(parent);
        }

        root = queue.poll();
        huffmanCodes = new HashMap<>();
        buildCodes(root, "", huffmanCodes);
    }

    private void buildCodes(HuffmanNode node, String code, Map<Character, String> codes) {
        if (node == null) return;

        if (node.isLeaf()) {
            codes.put(node.character, code.isEmpty() ? "0" : code);
        }

        buildCodes(node.left, code + "0", codes);
        buildCodes(node.right, code + "1", codes);
    }

    public Map<Character, String> getHuffmanCodes() {
        return huffmanCodes;
    }

    public HuffmanNode getRoot() {
        return root;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getEncodedText() {
        if (originalText == null || huffmanCodes == null) return "";

        StringBuilder encoded = new StringBuilder();
        for (char c : originalText.toCharArray()) {
            encoded.append(huffmanCodes.get(c));
        }
        return encoded.toString();
    }
}