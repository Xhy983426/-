package com.datastructure.visualizer.model;

public class TreeNode<T extends Comparable<T>> {
    public T data;
    public TreeNode<T> left;
    public TreeNode<T> right;

    public TreeNode(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}