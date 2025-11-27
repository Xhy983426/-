package com.datastructurevisualizer.model;

public class TreeNode {
    private int value;
    private TreeNode left;
    private TreeNode right;
    private boolean visited; // 新增：遍历状态标记

    public TreeNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.visited = false;
    }

    // getter 和 setter 方法
    public boolean isVisited() { return visited; }
    public void setVisited(boolean visited) { this.visited = visited; }
    public void resetVisited() { this.visited = false; }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
    public TreeNode getLeft() { return left; }
    public void setLeft(TreeNode left) { this.left = left; }
    public TreeNode getRight() { return right; }
    public void setRight(TreeNode right) { this.right = right; }
}