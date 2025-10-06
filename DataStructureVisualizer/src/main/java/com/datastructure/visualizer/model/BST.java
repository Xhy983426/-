package com.datastructure.visualizer.model;

public class BST<T extends Comparable<T>> extends BinaryTree<T> {

    @Override
    public void insert(T data) {
        root = insertRec(root, data);
    }

    protected TreeNode<T> insertRec(TreeNode<T> node, T data) {
        if (node == null) {
            return new TreeNode<>(data);
        }

        if (data.compareTo(node.data) < 0) {
            node.left = insertRec(node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            node.right = insertRec(node.right, data);
        }

        return node;
    }

    @Override
    public void delete(T data) {
        root = deleteRec(root, data);
    }

    private TreeNode<T> deleteRec(TreeNode<T> node, T data) {
        if (node == null) return null;

        if (data.compareTo(node.data) < 0) {
            node.left = deleteRec(node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            node.right = deleteRec(node.right, data);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            TreeNode<T> minNode = findMin(node.right);
            node.data = minNode.data;
            node.right = deleteRec(node.right, minNode.data);
        }
        return node;
    }

    @Override
    public boolean search(T data) {
        return searchRec(root, data);
    }

    private boolean searchRec(TreeNode<T> node, T data) {
        if (node == null) return false;

        int cmp = data.compareTo(node.data);
        if (cmp == 0) return true;
        else if (cmp < 0) return searchRec(node.left, data);
        else return searchRec(node.right, data);
    }
}