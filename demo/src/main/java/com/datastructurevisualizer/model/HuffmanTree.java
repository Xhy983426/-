package com.datastructurevisualizer.model;

import java.util.*;
import java.util.ArrayList;

public class HuffmanTree {
    private HuffmanNode root;
    private List<HuffmanStep> operationSteps;
    private List<HuffmanNode> allNodes;

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

        @Override
        public String toString() {
            if (isLeaf()) {
                return "'" + character + "':" + frequency;
            } else {
                return "Node:" + frequency;
            }
        }
    }

    public class HuffmanStep {
        public String type; // "frequency", "create_node", "combine", "build_tree", "generate_code"
        public String description;
        public Map<Character, Integer> frequencyMap;
        public PriorityQueue<HuffmanNode> currentQueue;
        public List<HuffmanNode> combinedNodes;
        public Map<Character, String> huffmanCodes;
        public HuffmanNode currentNode;

        public HuffmanStep(String type, String description) {
            this.type = type;
            this.description = description;
            this.frequencyMap = new HashMap<>();
            this.currentQueue = new PriorityQueue<>();
            this.combinedNodes = new java.util.ArrayList<>();
            this.huffmanCodes = new HashMap<>();
        }
    }

    public HuffmanTree() {
        root = null;
        operationSteps = new java.util.ArrayList<HuffmanStep>();
    }

    // 带步骤演示的构建方法
    public List<HuffmanStep> buildTreeWithSteps(String text) {
        operationSteps.clear();
        allNodes = new ArrayList<HuffmanNode>();

        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        // 步骤1: 统计字符频率
        HuffmanStep step1 = new HuffmanStep("frequency", "统计字符频率");
        Map<Character, Integer> frequencyMap = new HashMap<Character, Integer>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        step1.frequencyMap = new HashMap<Character, Integer>(frequencyMap);
        operationSteps.add(step1);

        // 步骤2: 创建初始叶子节点
        HuffmanStep step2 = new HuffmanStep("create_node", "创建初始叶子节点");
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<HuffmanNode>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            HuffmanNode node = new HuffmanNode(entry.getKey(), entry.getValue());
            pq.offer(node);
            allNodes.add(node); // 保存节点
        }
        step2.currentQueue = new PriorityQueue<HuffmanNode>(pq);
        operationSteps.add(step2);

        // 步骤3: 逐步构建哈夫曼树
        int stepCount = 1;
        List<HuffmanNode> currentForest = new ArrayList<HuffmanNode>(pq);

        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();

            // 创建合并步骤，显示当前森林状态
            HuffmanStep forestStep = new HuffmanStep("forest",
                    "步骤 " + stepCount + ": 当前森林状态 - " + getForestDescription(currentForest));
            forestStep.currentQueue = new PriorityQueue<HuffmanNode>(pq);
            forestStep.combinedNodes = new ArrayList<HuffmanNode>(currentForest);
            operationSteps.add(forestStep);

            HuffmanStep combineStep = new HuffmanStep("combine",
                    "步骤 " + stepCount + ": 合并节点 " + left + " 和 " + right +
                            " → 新节点频率: " + (left.frequency + right.frequency));
            combineStep.combinedNodes.add(left);
            combineStep.combinedNodes.add(right);

            // 创建父节点并建立连接
            HuffmanNode parent = new HuffmanNode(left.frequency + right.frequency, left, right);
            pq.offer(parent);
            allNodes.add(parent); // 保存新节点

            // 更新当前森林
            currentForest.remove(left);
            currentForest.remove(right);
            currentForest.add(parent);

            combineStep.currentQueue = new PriorityQueue<HuffmanNode>(pq);
            combineStep.combinedNodes.add(parent);
            operationSteps.add(combineStep);



            stepCount++;
        }

        root = pq.poll();

        // 步骤4: 构建完成，显示最终树
        HuffmanStep buildStep = new HuffmanStep("build_tree", "哈夫曼树构建完成");
        buildStep.currentNode = root;
        operationSteps.add(buildStep);

        // 步骤5: 生成编码
        HuffmanStep codeStep = new HuffmanStep("generate_code", "生成哈夫曼编码");
        Map<Character, String> huffmanCodes = new HashMap<Character, String>();
        generateCodesWithSteps(root, "", huffmanCodes, codeStep);
        codeStep.huffmanCodes = new HashMap<Character, String>(huffmanCodes);
        operationSteps.add(codeStep);

        // 步骤6: 显示结果
        HuffmanStep resultStep = new HuffmanStep("result", "哈夫曼编码结果");
        resultStep.huffmanCodes = new HashMap<Character, String>(huffmanCodes);

        // 计算压缩信息
        int originalBits = text.length() * 8;
        int compressedBits = 0;
        for (char c : text.toCharArray()) {
            compressedBits += huffmanCodes.get(c).length();
        }
        double compressionRatio = (1 - (double)compressedBits / originalBits) * 100;

        resultStep.description = String.format("原始大小: %d 位, 压缩后: %d 位, 压缩率: %.2f%%",
                originalBits, compressedBits, compressionRatio);
        operationSteps.add(resultStep);

        return new ArrayList<HuffmanStep>(operationSteps);
    }

    private void generateCodesWithSteps(HuffmanNode node, String code,
                                        Map<Character, String> huffmanCodes,
                                        HuffmanStep codeStep) {
        if (node == null) return;

        if (node.isLeaf()) {
            huffmanCodes.put(node.character, code.isEmpty() ? "0" : code);
        } else {
            generateCodesWithSteps(node.left, code + "0", huffmanCodes, codeStep);
            generateCodesWithSteps(node.right, code + "1", huffmanCodes, codeStep);
        }
    }


    private HuffmanNode getCurrentRoot(List<HuffmanNode> forest) {
        if (forest.isEmpty()) return null;
        HuffmanNode root = forest.get(0);
        for (HuffmanNode node : forest) {
            if (node.frequency < root.frequency) {
                root = node;
            }
        }
        return root;
    }
    private String getForestDescription(List<HuffmanNode> forest) {
        if (forest.isEmpty()) return "空森林";
        StringBuilder sb = new StringBuilder();
        for (HuffmanNode node : forest) {
            sb.append(node.toString()).append(" ");
        }
        return sb.toString().trim();
    }
    public HuffmanNode getRoot() {
        return root;
    }


}