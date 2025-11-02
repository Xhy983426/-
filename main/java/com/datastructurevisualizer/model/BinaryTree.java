package com.datastructurevisualizer.model;

import java.util.LinkedList;
import java.util.Queue;

public class BinaryTree {
    private TreeNode root;

    public BinaryTree() {
        root = null;
    }

    // 方法1: 使用层次遍历插入（推荐）
    public void insert(int value) {
        root = insertLevelOrder(root, value);
    }

    private TreeNode insertLevelOrder(TreeNode root, int value) {
        if (root == null) {
            return new TreeNode(value);
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();

            // 如果左子树为空，插入到左子树
            if (current.getLeft() == null) {
                current.setLeft(new TreeNode(value));
                break;
            } else {
                queue.offer(current.getLeft());
            }

            // 如果右子树为空，插入到右子树
            if (current.getRight() == null) {
                current.setRight(new TreeNode(value));
                break;
            } else {
                queue.offer(current.getRight());
            }
        }

        return root;
    }

    // 方法2: 使用递归插入（保持树的大致平衡）
    public void insertBalanced(int value) {
        root = insertBalancedRecursive(root, value);
    }

    private TreeNode insertBalancedRecursive(TreeNode current, int value) {
        if (current == null) {
            return new TreeNode(value);
        }

        // 比较左右子树的高度，选择较矮的一边插入
        int leftHeight = getHeight(current.getLeft());
        int rightHeight = getHeight(current.getRight());

        if (leftHeight <= rightHeight) {
            current.setLeft(insertBalancedRecursive(current.getLeft(), value));
        } else {
            current.setRight(insertBalancedRecursive(current.getRight(), value));
        }

        return current;
    }

    // 方法3: 随机选择左右子树插入
    public void insertRandom(int value) {
        root = insertRandomRecursive(root, value);
    }

    private TreeNode insertRandomRecursive(TreeNode current, int value) {
        if (current == null) {
            return new TreeNode(value);
        }

        // 随机选择左子树或右子树
        if (Math.random() < 0.5) {
            current.setLeft(insertRandomRecursive(current.getLeft(), value));
        } else {
            current.setRight(insertRandomRecursive(current.getRight(), value));
        }

        return current;
    }

    // 计算树的高度
    private int getHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight()));
    }

    // 计算树的节点数
    public int getSize() {
        return getSizeRecursive(root);
    }

    private int getSizeRecursive(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + getSizeRecursive(node.getLeft()) + getSizeRecursive(node.getRight());
    }

    // 获取树的高度
    public int getHeight() {
        return getHeight(root);
    }

    // 判断树是否平衡
    public boolean isBalanced() {
        return isBalancedRecursive(root);
    }

    private boolean isBalancedRecursive(TreeNode node) {
        if (node == null) {
            return true;
        }

        int leftHeight = getHeight(node.getLeft());
        int rightHeight = getHeight(node.getRight());

        return Math.abs(leftHeight - rightHeight) <= 1
                && isBalancedRecursive(node.getLeft())
                && isBalancedRecursive(node.getRight());
    }

    // 遍历方法
    public String preOrderTraversal() {
        StringBuilder result = new StringBuilder();
        preOrderRecursive(root, result);
        return result.toString();
    }

    private void preOrderRecursive(TreeNode node, StringBuilder result) {
        if (node != null) {
            result.append(node.getValue()).append(" ");
            preOrderRecursive(node.getLeft(), result);
            preOrderRecursive(node.getRight(), result);
        }
    }

    public String inOrderTraversal() {
        StringBuilder result = new StringBuilder();
        inOrderRecursive(root, result);
        return result.toString();
    }

    private void inOrderRecursive(TreeNode node, StringBuilder result) {
        if (node != null) {
            inOrderRecursive(node.getLeft(), result);
            result.append(node.getValue()).append(" ");
            inOrderRecursive(node.getRight(), result);
        }
    }

    public String postOrderTraversal() {
        StringBuilder result = new StringBuilder();
        postOrderRecursive(root, result);
        return result.toString();
    }

    private void postOrderRecursive(TreeNode node, StringBuilder result) {
        if (node != null) {
            postOrderRecursive(node.getLeft(), result);
            postOrderRecursive(node.getRight(), result);
            result.append(node.getValue()).append(" ");
        }
    }

    public String levelOrderTraversal() {
        StringBuilder result = new StringBuilder();
        if (root == null) {
            return result.toString();
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            result.append(current.getValue()).append(" ");

            if (current.getLeft() != null) {
                queue.offer(current.getLeft());
            }
            if (current.getRight() != null) {
                queue.offer(current.getRight());
            }
        }

        return result.toString();
    }

    public TreeNode getRoot() { return root; }
    public boolean isEmpty() { return root == null; }
}