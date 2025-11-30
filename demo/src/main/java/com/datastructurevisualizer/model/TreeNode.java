// TreeNode.java - 添加序列化支持
package com.datastructurevisualizer.model;

import java.io.Serializable;

public class TreeNode implements Serializable {
    private static final long serialVersionUID = 1L;

    private int value;
    private TreeNode left;
    private TreeNode right;
    private boolean visited;

    // 可序列化的树节点数据
    public static class SerializableNode implements Serializable {
        private static final long serialVersionUID = 1L;
        public int value;
        public SerializableNode left;
        public SerializableNode right;
        public boolean visited;

        public SerializableNode(TreeNode node) {
            if (node != null) {
                this.value = node.value;
                this.visited = node.visited;
                this.left = node.left != null ? new SerializableNode(node.left) : null;
                this.right = node.right != null ? new SerializableNode(node.right) : null;
            }
        }
    }

    public TreeNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.visited = false;
    }

    // 序列化方法
    public SerializableNode toSerializable() {
        return new SerializableNode(this);
    }

    // 从序列化数据重建节点
    public static TreeNode fromSerializable(SerializableNode serialNode) {
        if (serialNode == null) return null;

        TreeNode node = new TreeNode(serialNode.value);
        node.visited = serialNode.visited;
        node.left = fromSerializable(serialNode.left);
        node.right = fromSerializable(serialNode.right);
        return node;
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