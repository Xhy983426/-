package com.datastructurevisualizer.model;

import java.util.*;

public class HuffmanTree {
    private HuffmanNode root;

    public class HuffmanNode implements Comparable<HuffmanNode> {
        public char character;
        public int frequency;
        public HuffmanNode left, right;

        public HuffmanNode(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }

        public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public int compareTo(HuffmanNode other) {
            return Integer.compare(this.frequency, other.frequency);
        }
    }

    public Map<Character, String> buildTree(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            pq.offer(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode parent = new HuffmanNode(left.frequency + right.frequency, left, right);
            pq.offer(parent);
        }

        root = pq.poll();

        Map<Character, String> huffmanCodes = new HashMap<>();
        generateCodes(root, "", huffmanCodes);

        return huffmanCodes;
    }

    private void generateCodes(HuffmanNode node, String code, Map<Character, String> huffmanCodes) {
        if (node == null) return;

        if (node.isLeaf()) {
            huffmanCodes.put(node.character, code.isEmpty() ? "0" : code);
        } else {
            generateCodes(node.left, code + "0", huffmanCodes);
            generateCodes(node.right, code + "1", huffmanCodes);
        }
    }

    public HuffmanNode getRoot() { return root; }
}