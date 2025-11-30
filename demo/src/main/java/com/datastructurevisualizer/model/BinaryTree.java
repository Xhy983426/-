package com.datastructurevisualizer.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.util.List;

public class BinaryTree {
    private TreeNode root;
    private List<TraversalStep> traversalSteps;

    // 可序列化的二叉树数据 - 只保存树结构，不保存步骤数据
    public static class BinaryTreeData implements Serializable {
        private static final long serialVersionUID = 1L;
        public TreeNode.SerializableNode root;
        public int size;
        public int height;

        public BinaryTreeData(BinaryTree tree) {
            this.root = tree.root != null ? tree.root.toSerializable() : null;
            this.size = tree.getSize();
            this.height = tree.getHeight();
        }
    }

    public BinaryTree() {
        root = null;
        traversalSteps = new ArrayList<>();
    }

    // 序列化方法
    public BinaryTreeData getSerializableData() {
        return new BinaryTreeData(this);
    }

    // 反序列化方法
    public static BinaryTree fromSerializableData(BinaryTreeData data) {
        BinaryTree tree = new BinaryTree();
        if (data != null && data.root != null) {
            tree.root = TreeNode.fromSerializable(data.root);
        }
        return tree;
    }

    // 存档管理方法
    public TreeArchiveManager.TreeArchiveData saveToArchive(String description) {
        return new TreeArchiveManager.TreeArchiveData("binary", this.getSerializableData(), description);
    }

    public static BinaryTree loadFromArchive(TreeArchiveManager.TreeArchiveData archiveData) {
        if (archiveData != null && archiveData.data instanceof BinaryTreeData) {
            return fromSerializableData((BinaryTreeData) archiveData.data);
        }
        return new BinaryTree();
    }

    public class TraversalStep {
        public String traversalType;
        public TreeNode currentNode;
        public String description;
        public List<Integer> visitedNodes;
        public List<Integer> currentPath;

        public TraversalStep(String traversalType, TreeNode currentNode, String description) {
            this.traversalType = traversalType;
            this.currentNode = currentNode;
            this.description = description;
            this.visitedNodes = new ArrayList<Integer>();
            this.currentPath = new ArrayList<Integer>();
        }
    }


    // 重置所有节点的访问状态
    private void resetVisited(TreeNode node) {
        if (node != null) {
            node.resetVisited();
            resetVisited(node.getLeft());
            resetVisited(node.getRight());
        }
    }
    // 收集已访问节点
    private void collectVisitedNodes(TreeNode node, List<Integer> visitedNodes) {
        if (node != null) {
            if (node.isVisited()) {
                visitedNodes.add(node.getValue());
            }
            collectVisitedNodes(node.getLeft(), visitedNodes);
            collectVisitedNodes(node.getRight(), visitedNodes);
        }
    }
    // 带步骤的前序遍历
    public List<TraversalStep> preOrderTraversalWithSteps() {
        traversalSteps.clear();
        resetVisited(root);  // 重置访问状态

        TraversalStep startStep = new TraversalStep("前序遍历", null, "开始前序遍历");
        traversalSteps.add(startStep);

        // 确保传递根节点信息
        preOrderRecursiveWithSteps(root, new ArrayList<Integer>());

        TraversalStep completeStep = new TraversalStep("前序遍历", null, "前序遍历完成");
        List<Integer> allVisited = new ArrayList<Integer>();
        collectVisitedNodes(root, allVisited);
        completeStep.visitedNodes = new ArrayList<Integer>(allVisited);
        traversalSteps.add(completeStep);

        return new ArrayList<TraversalStep>(traversalSteps);
    }


    private void preOrderRecursiveWithSteps(TreeNode node, List<Integer> currentPath) {
        if (node == null) return;

        // 访问当前节点
        node.setVisited(true);
        currentPath.add(node.getValue());

        TraversalStep step = new TraversalStep("前序遍历", node,
                "访问节点: " + node.getValue());

        // 确保收集正确的已访问节点
        collectVisitedNodes(root, step.visitedNodes);
        step.currentPath = new ArrayList<Integer>(currentPath);
        traversalSteps.add(step);

        // 递归左子树
        if (node.getLeft() != null) {
            TraversalStep leftStep = new TraversalStep("前序遍历", node,
                    "转向左子树: " + node.getLeft().getValue());
            collectVisitedNodes(root, leftStep.visitedNodes);
            leftStep.currentPath = new ArrayList<Integer>(currentPath);
            traversalSteps.add(leftStep);
        }
        preOrderRecursiveWithSteps(node.getLeft(), currentPath);

        // 递归右子树
        if (node.getRight() != null) {
            TraversalStep rightStep = new TraversalStep("前序遍历", node,
                    "转向右子树: " + node.getRight().getValue());
            collectVisitedNodes(root, rightStep.visitedNodes);
            rightStep.currentPath = new ArrayList<Integer>(currentPath);
            traversalSteps.add(rightStep);
        }
        preOrderRecursiveWithSteps(node.getRight(), currentPath);

        currentPath.remove(currentPath.size() - 1);
    }

    // 带步骤的中序遍历
    public List<TraversalStep> inOrderTraversalWithSteps() {
        traversalSteps.clear();
        resetVisited(root);

        TraversalStep startStep = new TraversalStep("中序遍历", null, "开始中序遍历");
        traversalSteps.add(startStep);

        inOrderRecursiveWithSteps(root, new ArrayList<Integer>());

        TraversalStep completeStep = new TraversalStep("中序遍历", null, "中序遍历完成");
        List<Integer> allVisited = new ArrayList<Integer>();
        collectVisitedNodes(root, allVisited);
        completeStep.visitedNodes = new ArrayList<Integer>(allVisited);
        traversalSteps.add(completeStep);

        resetVisited(root);
        return new ArrayList<TraversalStep>(traversalSteps);
    }

    private void inOrderRecursiveWithSteps(TreeNode node, List<Integer> currentPath) {
        if (node == null) return;

        // 递归左子树
        if (node.getLeft() != null) {
            TraversalStep leftStep = new TraversalStep("中序遍历", node,
                    "转向左子树: " + node.getLeft().getValue());
            collectVisitedNodes(root, leftStep.visitedNodes);
            leftStep.currentPath = new ArrayList<>(currentPath);
            traversalSteps.add(leftStep);
        }
        inOrderRecursiveWithSteps(node.getLeft(), currentPath);

        // 访问当前节点
        node.setVisited(true);
        currentPath.add(node.getValue());

        TraversalStep step = new TraversalStep("中序遍历", node,
                "访问节点: " + node.getValue());
        collectVisitedNodes(root, step.visitedNodes);
        step.currentPath = new ArrayList<>(currentPath);
        traversalSteps.add(step);

        // 递归右子树
        if (node.getRight() != null) {
            TraversalStep rightStep = new TraversalStep("中序遍历", node,
                    "转向右子树: " + node.getRight().getValue());
            collectVisitedNodes(root, rightStep.visitedNodes);
            rightStep.currentPath = new ArrayList<>(currentPath);
            traversalSteps.add(rightStep);
        }
        inOrderRecursiveWithSteps(node.getRight(), currentPath);

        currentPath.remove(currentPath.size() - 1);
    }

    // 带步骤的后序遍历
    public List<TraversalStep> postOrderTraversalWithSteps() {
        traversalSteps.clear();
        resetVisited(root);

        TraversalStep startStep = new TraversalStep("后序遍历", null, "开始后序遍历");
        traversalSteps.add(startStep);

        postOrderRecursiveWithSteps(root, new ArrayList<Integer>());

        TraversalStep completeStep = new TraversalStep("后序遍历", null, "后序遍历完成");
        List<Integer> allVisited = new ArrayList<Integer>();
        collectVisitedNodes(root, allVisited);
        completeStep.visitedNodes = new ArrayList<Integer>(allVisited);
        traversalSteps.add(completeStep);

        resetVisited(root);
        return new ArrayList<TraversalStep>(traversalSteps);
    }


    private void postOrderRecursiveWithSteps(TreeNode node, List<Integer> currentPath) {
        if (node == null) return;

        // 递归左子树
        if (node.getLeft() != null) {
            TraversalStep leftStep = new TraversalStep("后序遍历", node,
                    "转向左子树: " + node.getLeft().getValue());
            collectVisitedNodes(root, leftStep.visitedNodes);
            leftStep.currentPath = new ArrayList<>(currentPath);
            traversalSteps.add(leftStep);
        }
        postOrderRecursiveWithSteps(node.getLeft(), currentPath);

        // 递归右子树
        if (node.getRight() != null) {
            TraversalStep rightStep = new TraversalStep("后序遍历", node,
                    "转向右子树: " + node.getRight().getValue());
            collectVisitedNodes(root, rightStep.visitedNodes);
            rightStep.currentPath = new ArrayList<>(currentPath);
            traversalSteps.add(rightStep);
        }
        postOrderRecursiveWithSteps(node.getRight(), currentPath);

        // 访问当前节点
        node.setVisited(true);
        currentPath.add(node.getValue());

        TraversalStep step = new TraversalStep("后序遍历", node,
                "访问节点: " + node.getValue());
        collectVisitedNodes(root, step.visitedNodes);
        step.currentPath = new ArrayList<>(currentPath);
        traversalSteps.add(step);

        currentPath.remove(currentPath.size() - 1);
    }

    // 带步骤的层次遍历
    public List<TraversalStep> levelOrderTraversalWithSteps() {
        traversalSteps.clear();
        resetVisited(root);

        TraversalStep startStep = new TraversalStep("层次遍历", null, "开始层次遍历");
        traversalSteps.add(startStep);

        if (root == null) {
            return new ArrayList<TraversalStep>(traversalSteps);
        }

        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        int level = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            TraversalStep levelStep = new TraversalStep("层次遍历", null,
                    "遍历第 " + level + " 层，节点数: " + levelSize);
            collectVisitedNodes(root, levelStep.visitedNodes);
            traversalSteps.add(levelStep);

            for (int i = 0; i < levelSize; i++) {
                TreeNode current = queue.poll();
                current.setVisited(true);

                TraversalStep visitStep = new TraversalStep("层次遍历", current,
                        "访问节点: " + current.getValue() + " (第 " + level + " 层)");
                collectVisitedNodes(root, visitStep.visitedNodes);
                traversalSteps.add(visitStep);

                if (current.getLeft() != null) {
                    queue.offer(current.getLeft());
                }
                if (current.getRight() != null) {
                    queue.offer(current.getRight());
                }
            }
            level++;
        }

        TraversalStep completeStep = new TraversalStep("层次遍历", null, "层次遍历完成");
        List<Integer> allVisited = new ArrayList<Integer>();
        collectVisitedNodes(root, allVisited);
        completeStep.visitedNodes = new ArrayList<Integer>(allVisited);
        traversalSteps.add(completeStep);

        resetVisited(root);
        return new ArrayList<TraversalStep>(traversalSteps);
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