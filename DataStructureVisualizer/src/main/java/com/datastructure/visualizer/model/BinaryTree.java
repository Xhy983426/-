package com.datastructure.visualizer.model;

import java.util.LinkedList;
import java.util.Queue;

public class BinaryTree<T extends Comparable<T>> implements DataStructure<T> {
    protected TreeNode<T> root;

    public BinaryTree() {
        this.root = null;
    }

    @Override
    public void insert(T data) {
        root = insertRec(root, data);
    }

    protected TreeNode<T> insertRec(TreeNode<T> node, T data) {
        if (node == null) {
            return new TreeNode<>(data);
        }

        // 普通二叉树使用层次遍历插入
        Queue<TreeNode<T>> queue = new LinkedList<>();
        queue.add(node);

        while (!queue.isEmpty()) {
            TreeNode<T> current = queue.poll();

            if (current.left == null) {
                current.left = new TreeNode<>(data);
                break;
            } else {
                queue.add(current.left);
            }

            if (current.right == null) {
                current.right = new TreeNode<>(data);
                break;
            } else {
                queue.add(current.right);
            }
        }
        return node;
    }

    @Override
    public void delete(T data) {
        root = deleteRec(root, data);
    }

    private TreeNode<T> deleteRec(TreeNode<T> node, T data) {
        if (node == null) return null;

        if (node.data.equals(data)) {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            TreeNode<T> minNode = findMin(node.right);
            node.data = minNode.data;
            node.right = deleteRec(node.right, minNode.data);
        } else {
            node.left = deleteRec(node.left, data);
            node.right = deleteRec(node.right, data);
        }
        return node;
    }

    protected TreeNode<T> findMin(TreeNode<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public boolean search(T data) {
        return searchRec(root, data);
    }

    private boolean searchRec(TreeNode<T> node, T data) {
        if (node == null) return false;
        if (node.data.equals(data)) return true;
        return searchRec(node.left, data) || searchRec(node.right, data);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public String visualize() {
        StringBuilder sb = new StringBuilder();
        inOrderTraversal(root, sb);
        return "In-order: " + sb.toString();
    }

    private void inOrderTraversal(TreeNode<T> node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.data).append(" ");
            inOrderTraversal(node.right, sb);
        }
    }

    @Override
    public int size() {
        return countNodes(root);
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    private int countNodes(TreeNode<T> node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    public TreeNode<T> getRoot() {
        return root;
    }
}