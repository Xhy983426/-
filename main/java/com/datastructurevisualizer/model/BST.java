package com.datastructurevisualizer.model;

import java.util.ArrayList;
import java.util.List;

public class BST {
    private TreeNode root;
    private List<SearchStep> searchSteps;

    public class SearchStep {
        public TreeNode currentNode;
        public int targetValue;
        public String description;
        public boolean found;

        public SearchStep(TreeNode currentNode, int targetValue, String description, boolean found) {
            this.currentNode = currentNode;
            this.targetValue = targetValue;
            this.description = description;
            this.found = found;
        }
    }

    public BST() {
        root = null;
        searchSteps = new ArrayList<>();
    }

    public void insert(int value) {
        root = insertRecursive(root, value);
    }

    private TreeNode insertRecursive(TreeNode current, int value) {
        if (current == null) {
            return new TreeNode(value);
        }

        if (value < current.getValue()) {
            current.setLeft(insertRecursive(current.getLeft(), value));
        } else if (value > current.getValue()) {
            current.setRight(insertRecursive(current.getRight(), value));
        }

        return current;
    }

    // 增强的搜索方法，记录每一步
    public List<SearchStep> searchWithSteps(int value) {
        searchSteps.clear();
        boolean found = searchRecursiveWithSteps(root, value);

        if (!found) {
            searchSteps.add(new SearchStep(null, value, "未找到值为 " + value + " 的节点", false));
        }

        return new ArrayList<>(searchSteps);
    }

    private boolean searchRecursiveWithSteps(TreeNode current, int value) {
        if (current == null) {
            return false;
        }

        // 记录当前步骤
        if (value == current.getValue()) {
            searchSteps.add(new SearchStep(current, value,
                    "找到目标节点: " + value, true));
            return true;
        } else if (value < current.getValue()) {
            searchSteps.add(new SearchStep(current, value,
                    value + " < " + current.getValue() + "，转向左子树", false));
            return searchRecursiveWithSteps(current.getLeft(), value);
        } else {
            searchSteps.add(new SearchStep(current, value,
                    value + " > " + current.getValue() + "，转向右子树", false));
            return searchRecursiveWithSteps(current.getRight(), value);
        }
    }

    // 原来的简单搜索方法（保持兼容性）
    public boolean search(int value) {
        return searchRecursive(root, value);
    }

    private boolean searchRecursive(TreeNode current, int value) {
        if (current == null) return false;
        if (value == current.getValue()) return true;
        return value < current.getValue() ?
                searchRecursive(current.getLeft(), value) :
                searchRecursive(current.getRight(), value);
    }

    public void delete(int value) {
        root = deleteRecursive(root, value);
    }

    private TreeNode deleteRecursive(TreeNode current, int value) {
        if (current == null) return null;

        if (value == current.getValue()) {
            if (current.getLeft() == null) return current.getRight();
            if (current.getRight() == null) return current.getLeft();

            int smallestValue = findSmallestValue(current.getRight());
            current.setValue(smallestValue);
            current.setRight(deleteRecursive(current.getRight(), smallestValue));
            return current;
        }

        if (value < current.getValue()) {
            current.setLeft(deleteRecursive(current.getLeft(), value));
        } else {
            current.setRight(deleteRecursive(current.getRight(), value));
        }

        return current;
    }

    private int findSmallestValue(TreeNode root) {
        return root.getLeft() == null ? root.getValue() : findSmallestValue(root.getLeft());
    }

    // 获取树的大小
    public int size() {
        return sizeRecursive(root);
    }

    private int sizeRecursive(TreeNode node) {
        if (node == null) return 0;
        return 1 + sizeRecursive(node.getLeft()) + sizeRecursive(node.getRight());
    }

    // 获取树的高度
    public int height() {
        return heightRecursive(root);
    }

    private int heightRecursive(TreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(heightRecursive(node.getLeft()), heightRecursive(node.getRight()));
    }

    public TreeNode getRoot() { return root; }
    public boolean isEmpty() { return root == null; }
}