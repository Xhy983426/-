package com.datastructurevisualizer.model;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

public class BST {
    private TreeNode root;
    private List<SearchStep> searchSteps;

    // 可序列化的BST数据
    public static class BSTData implements Serializable {
        private static final long serialVersionUID = 1L;
        public TreeNode.SerializableNode root;
        public int size;
        public int height;

        public BSTData(BST bst) {
            this.root = bst.root != null ? bst.root.toSerializable() : null;
            this.size = bst.size();
            this.height = bst.height();
        }
    }

    public BST() {
        root = null;
        searchSteps = new ArrayList<>();
    }

    // 序列化方法
    public BSTData getSerializableData() {
        return new BSTData(this);
    }

    // 反序列化方法
    public static BST fromSerializableData(BSTData data) {
        BST bst = new BST();
        if (data != null && data.root != null) {
            bst.root = TreeNode.fromSerializable(data.root);
        }
        return bst;
    }

    // 存档管理方法
    public TreeArchiveManager.TreeArchiveData saveToArchive(String description) {
        return new TreeArchiveManager.TreeArchiveData("bst", this.getSerializableData(), description);
    }

    public static BST loadFromArchive(TreeArchiveManager.TreeArchiveData archiveData) {
        if (archiveData != null && archiveData.data instanceof BSTData) {
            return fromSerializableData((BSTData) archiveData.data);
        }
        return new BST();
    }
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
    // 添加删除步骤类
    public class DeleteStep {
        public TreeNode currentNode;
        public TreeNode nodeToDelete;
        public TreeNode replacementNode;
        public String description;
        public String stepType; // "find", "mark", "find_replacement", "replace", "delete", "complete"

        public DeleteStep(TreeNode currentNode, TreeNode nodeToDelete, String description, String stepType) {
            this.currentNode = currentNode;
            this.nodeToDelete = nodeToDelete;
            this.description = description;
            this.stepType = stepType;
        }
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
        root = deleteNodeRecursive(root, value);
    }

    public List<DeleteStep> deleteWithSteps(int value) {
        List<DeleteStep> deleteSteps = new ArrayList<>();

        // 步骤1: 开始删除
        deleteSteps.add(new DeleteStep(null, null, "开始删除值为 " + value + " 的节点", "start"));

        // 阶段1: 查找目标节点（只读查找）
        TreeNode nodeToDelete = findNodeReadOnly(root, value, deleteSteps);

        if (nodeToDelete == null) {
            deleteSteps.add(new DeleteStep(null, null, "未找到值为 " + value + " 的节点", "not_found"));
            return deleteSteps;
        }

        // 阶段2: 分析节点类型（只读分析）
        analyzeNodeTypeReadOnly(nodeToDelete, deleteSteps);

        // 阶段3: 制定删除策略（只读策略）
        planDeletionStrategyReadOnly(nodeToDelete, deleteSteps);

        deleteSteps.add(new DeleteStep(null, null, "删除策略演示完成", "demo_complete"));
        return deleteSteps;
    }

    // 只读查找节点（不修改树结构）
    private TreeNode findNodeReadOnly(TreeNode current, int value, List<DeleteStep> steps) {
        if (current == null) {
            return null;
        }

        // 记录比较步骤
        steps.add(new DeleteStep(current, null,
                "比较: " + value + " 与 " + current.getValue(), "compare"));

        if (value == current.getValue()) {
            steps.add(new DeleteStep(current, current,
                    "🎯 找到要删除的节点: " + value, "found"));
            return current;
        } else if (value < current.getValue()) {
            steps.add(new DeleteStep(current, null,
                    value + " < " + current.getValue() + "，向左子树查找", "traverse_left"));
            return findNodeReadOnly(current.getLeft(), value, steps);
        } else {
            steps.add(new DeleteStep(current, null,
                    value + " > " + current.getValue() + "，向右子树查找", "traverse_right"));
            return findNodeReadOnly(current.getRight(), value, steps);
        }
    }

    // 只读分析节点类型
    private void analyzeNodeTypeReadOnly(TreeNode node, List<DeleteStep> steps) {
        steps.add(new DeleteStep(node, node, "正在分析节点类型...", "analyze"));

        if (node.getLeft() == null && node.getRight() == null) {
            steps.add(new DeleteStep(node, node, "该节点是叶子节点（没有子节点）", "leaf_identified"));
            steps.add(new DeleteStep(node, node, "删除策略: 直接移除该节点", "leaf_strategy"));
        } else if (node.getLeft() == null) {
            steps.add(new DeleteStep(node, node, "该节点只有右子节点: " + node.getRight().getValue(), "one_child_identified"));
            steps.add(new DeleteStep(node.getRight(), node, "删除策略: 用右子节点替换当前节点", "one_child_strategy"));
        } else if (node.getRight() == null) {
            steps.add(new DeleteStep(node, node, "该节点只有左子节点: " + node.getLeft().getValue(), "one_child_identified"));
            steps.add(new DeleteStep(node.getLeft(), node, "删除策略: 用左子节点替换当前节点", "one_child_strategy"));
        } else {
            steps.add(new DeleteStep(node, node, "该节点有两个子节点", "two_children_identified"));

            // 查找后继节点（只读）
            TreeNode successor = findMinReadOnly(node.getRight(), steps, node);
            steps.add(new DeleteStep(successor, node, "找到后继节点: " + successor.getValue(), "successor_found"));

            steps.add(new DeleteStep(node, node,
                    "删除策略: 将节点值替换为 " + successor.getValue() + "，然后删除原后继节点", "two_children_strategy"));
        }
    }

    // 只读查找最小值
    private TreeNode findMinReadOnly(TreeNode node, List<DeleteStep> steps, TreeNode nodeToDelete) {
        if (node.getLeft() == null) {
            steps.add(new DeleteStep(node, nodeToDelete, "找到最小值节点: " + node.getValue(), "min_found"));
            return node;
        }
        steps.add(new DeleteStep(node, nodeToDelete, "继续在左子树中查找最小值", "traverse_min"));
        return findMinReadOnly(node.getLeft(), steps, nodeToDelete);
    }

    // 制定删除策略（只读）
    private void planDeletionStrategyReadOnly(TreeNode nodeToDelete, List<DeleteStep> steps) {
        steps.add(new DeleteStep(nodeToDelete, nodeToDelete, "总结删除执行步骤...", "execution_plan"));

        if (nodeToDelete.getLeft() == null && nodeToDelete.getRight() == null) {
            steps.add(new DeleteStep(null, nodeToDelete,
                    "执行步骤: 找到父节点，将其对应指针设为null", "leaf_execution"));
        } else if (nodeToDelete.getLeft() == null || nodeToDelete.getRight() == null) {
            TreeNode child = (nodeToDelete.getLeft() != null) ? nodeToDelete.getLeft() : nodeToDelete.getRight();
            steps.add(new DeleteStep(child, nodeToDelete,
                    "执行步骤: 将父节点的指针指向子节点 " + child.getValue(), "one_child_execution"));
        } else {
            TreeNode successor = findMinReadOnly(nodeToDelete.getRight(), new ArrayList<>(), nodeToDelete);
            steps.add(new DeleteStep(nodeToDelete, nodeToDelete,
                    "执行步骤1: 将节点值改为 " + successor.getValue(), "copy_value_step"));
            steps.add(new DeleteStep(nodeToDelete.getRight(), successor,
                    "执行步骤2: 删除原来的后继节点 " + successor.getValue(), "delete_successor_step"));
        }
    }


    // 实际执行删除的方法（在演示完成后调用）
    public void performActualDeletion(int value) {
        root = deleteRecursive(root, value);
    }

    // 原有的删除方法
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




    // 后继节点查找结果
    private class SuccessorSearchResult {
        TreeNode node;
        TreeNode parent;

        SuccessorSearchResult(TreeNode node, TreeNode parent) {
            this.node = node;
            this.parent = parent;
        }
    }

    // 查找后继节点
    private SuccessorSearchResult findSuccessorWithSteps(TreeNode node, List<DeleteStep> steps, TreeNode nodeToDelete) {
        return findSuccessorHelper(node, null, steps, nodeToDelete);
    }

    private SuccessorSearchResult findSuccessorHelper(TreeNode node, TreeNode parent, List<DeleteStep> steps, TreeNode nodeToDelete) {
        if (node.getLeft() == null) {
            steps.add(new DeleteStep(node, nodeToDelete,
                    "找到最小值节点: " + node.getValue(), "min_found"));
            return new SuccessorSearchResult(node, parent);
        }
        steps.add(new DeleteStep(node, nodeToDelete,
                "继续在左子树中查找最小值，当前节点: " + node.getValue(), "traverse_min"));
        return findSuccessorHelper(node.getLeft(), node, steps, nodeToDelete);
    }




    private TreeNode deleteNodeRecursive(TreeNode current, int value) {
        if (current == null) return null;

        if (value == current.getValue()) {
            if (current.getLeft() == null) return current.getRight();
            if (current.getRight() == null) return current.getLeft();

            int smallestValue = findSmallestValue(current.getRight());
            current.setValue(smallestValue);
            current.setRight(deleteNodeRecursive(current.getRight(), smallestValue));
            return current;
        }

        if (value < current.getValue()) {
            current.setLeft(deleteNodeRecursive(current.getLeft(), value));
        } else {
            current.setRight(deleteNodeRecursive(current.getRight(), value));
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