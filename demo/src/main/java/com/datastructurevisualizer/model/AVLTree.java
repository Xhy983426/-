package com.datastructurevisualizer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AVLTree {
    private AVLNode root;
    private List<AVLStep> operationSteps;
    private AVLNode currentRoot;

    // 可序列化的AVL树数据
    public static class AVLTreeData implements Serializable {
        private static final long serialVersionUID = 1L;
        public AVLNode.SerializableNode root;
        public int size;
        public int height;

        public AVLTreeData(AVLTree tree) {
            this.root = tree.root != null ? tree.root.toSerializable() : null;
            this.size = tree.size();
            this.height = tree.height();
        }
    }

    public class AVLNode {
        public int value;
        public int height;
        public AVLNode left;
        public AVLNode right;

        // 可序列化的AVL节点
        public static class SerializableNode implements Serializable {
            private static final long serialVersionUID = 1L;
            public int value;
            public int height;
            public SerializableNode left;
            public SerializableNode right;

            public SerializableNode(AVLNode node) {
                if (node != null) {
                    this.value = node.value;
                    this.height = node.height;
                    this.left = node.left != null ? new SerializableNode(node.left) : null;
                    this.right = node.right != null ? new SerializableNode(node.right) : null;
                }
            }
        }

        public AVLNode(int value) {
            this.value = value;
            this.height = 1;
        }
        public SerializableNode toSerializable() {
            return new SerializableNode(this);
        }

        @Override
        public String toString() {
            return value + "(h=" + height + ")";
        }

        // 复制节点（用于保存状态）
        public AVLNode copy() {
            AVLNode newNode = new AVLNode(this.value);
            newNode.height = this.height;
            // 注意：不复制子节点，避免无限递归
            return newNode;
        }
    }

    public class AVLStep {
        public String type; // "insert", "check_balance", "before_rotate", "rotating", "after_rotate", "complete"
        public String description;
        public AVLNode treeState; // 当前树的状态
        public AVLNode currentNode;
        public int balanceFactor;
        public String rotationType;
        public String rotationCase;
        public boolean needsRotation;

        public AVLStep(String type, String description) {
            this.type = type;
            this.description = description;
        }
    }

    private AVLNode fromSerializable(AVLNode.SerializableNode serialNode) {
        if (serialNode == null) return null;

        AVLNode node = new AVLNode(serialNode.value);
        node.height = serialNode.height;
        node.left = fromSerializable(serialNode.left);
        node.right = fromSerializable(serialNode.right);
        return node;
    }

    public AVLTree() {
        root = null;
        currentRoot = null;
        operationSteps = new ArrayList<>();
    }
    public AVLTreeData getSerializableData() {
        return new AVLTreeData(this);
    }

    // 反序列化方法
    public static AVLTree fromSerializableData(AVLTreeData data) {
        AVLTree tree = new AVLTree();
        if (data != null && data.root != null) {
            tree.root = tree.fromSerializable(data.root);
        }
        return tree;
    }

    // 存档管理方法
    public TreeArchiveManager.TreeArchiveData saveToArchive(String description) {
        return new TreeArchiveManager.TreeArchiveData("avl", this.getSerializableData(), description);
    }

    public static AVLTree loadFromArchive(TreeArchiveManager.TreeArchiveData archiveData) {
        if (archiveData != null && archiveData.data instanceof AVLTreeData) {
            return fromSerializableData((AVLTreeData) archiveData.data);
        }
        return new AVLTree();
    }


    // 带步骤演示的插入方法
    public List<AVLStep> insertWithSteps(int value) {
        operationSteps.clear();

        AVLStep startStep = new AVLStep("insert", "开始插入节点: " + value);
        startStep.treeState = copyTree(root);
        operationSteps.add(startStep);

        // 执行BST插入
        root = insertRecursive(root, value);
        currentRoot = root;

        AVLStep afterInsertStep = new AVLStep("insert", "BST插入完成，开始平衡检查");
        afterInsertStep.treeState = copyTree(root);
        operationSteps.add(afterInsertStep);

        // 从新插入的节点开始向上检查平衡
        checkBalanceFromNode(root, value);

        AVLStep completeStep = new AVLStep("complete", "AVL树插入完成");
        completeStep.treeState = copyTree(root);
        operationSteps.add(completeStep);

        return new ArrayList<>(operationSteps);
    }

    // 递归插入（不包含步骤记录）
    private AVLNode insertRecursive(AVLNode node, int value) {
        if (node == null) {
            return new AVLNode(value);
        }

        if (value < node.value) {
            node.left = insertRecursive(node.left, value);
        } else if (value > node.value) {
            node.right = insertRecursive(node.right, value);
        } else {
            return node; // 不允许重复值
        }

        // 更新高度
        node.height = 1 + Math.max(height(node.left), height(node.right));

        return node;
    }

    // 从插入节点向上检查平衡
    private void checkBalanceFromNode(AVLNode startNode, int insertedValue) {
        AVLNode current = startNode;

        while (current != null) {
            // 步骤1: 检查当前节点平衡
            int balance = getBalance(current);

            AVLStep checkStep = new AVLStep("check_balance",
                    "检查节点 " + current.value + " 的平衡因子: " + balance);
            checkStep.treeState = copyTree(root);
            checkStep.currentNode = current;
            checkStep.balanceFactor = balance;
            checkStep.needsRotation = (Math.abs(balance) > 1);
            operationSteps.add(checkStep);

            // 步骤2: 如果需要旋转，执行旋转
            if (Math.abs(balance) > 1) {
                performRotationWithSteps(current, insertedValue);
                break; // 旋转后重新从根节点开始检查
            }

            // 向上移动到父节点（这里简化处理，实际需要知道父节点关系）
            if (insertedValue < current.value) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
    }

    // 执行旋转并记录步骤
    private void performRotationWithSteps(AVLNode unbalancedNode, int insertedValue) {
        int balance = getBalance(unbalancedNode);

        // 步骤1: 旋转前状态
        AVLStep beforeRotateStep = new AVLStep("before_rotate",
                "节点 " + unbalancedNode.value + " 不平衡，需要旋转");
        beforeRotateStep.treeState = copyTree(root);
        beforeRotateStep.currentNode = unbalancedNode;
        beforeRotateStep.balanceFactor = balance;
        operationSteps.add(beforeRotateStep);

        // 确定旋转类型
        String rotationType = "";
        String rotationCase = "";

        if (balance > 1) {
            if (insertedValue < unbalancedNode.left.value) {
                rotationType = "右旋转";
                rotationCase = "LL";
            } else {
                rotationType = "左右旋转";
                rotationCase = "LR";
            }
        } else {
            if (insertedValue > unbalancedNode.right.value) {
                rotationType = "左旋转";
                rotationCase = "RR";
            } else {
                rotationType = "右左旋转";
                rotationCase = "RL";
            }
        }

        AVLStep determineStep = new AVLStep("before_rotate",
                "确定为 " + rotationCase + " 情况，执行" + rotationType);
        determineStep.treeState = copyTree(root);
        determineStep.currentNode = unbalancedNode;
        determineStep.rotationType = rotationType;
        determineStep.rotationCase = rotationCase;
        operationSteps.add(determineStep);

        // 执行旋转
        AVLNode newRoot = performActualRotation(unbalancedNode, rotationType, rotationCase);

        // 更新根节点（如果旋转的是根节点）
        if (unbalancedNode == root) {
            root = newRoot;
        }

        currentRoot = root;

        // 步骤3: 旋转后状态
        AVLStep afterRotateStep = new AVLStep("after_rotate",
                rotationType + "完成，新根节点: " + newRoot.value);
        afterRotateStep.treeState = copyTree(root);
        afterRotateStep.currentNode = newRoot;
        operationSteps.add(afterRotateStep);
    }

    // 执行实际的旋转操作
    private AVLNode performActualRotation(AVLNode node, String rotationType, String rotationCase) {
        switch (rotationCase) {
            case "LL":
                return rightRotate(node);
            case "RR":
                return leftRotate(node);
            case "LR":
                node.left = leftRotate(node.left);
                return rightRotate(node);
            case "RL":
                node.right = rightRotate(node.right);
                return leftRotate(node);
            default:
                return node;
        }
    }

    // 右旋转
    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        // 执行旋转
        x.right = y;
        y.left = T2;

        // 更新高度
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    // 左旋转
    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        // 执行旋转
        y.left = x;
        x.right = T2;

        // 更新高度
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    // 复制整棵树（用于保存状态）
    private AVLNode copyTree(AVLNode node) {
        if (node == null) return null;

        AVLNode newNode = node.copy();
        newNode.left = copyTree(node.left);
        newNode.right = copyTree(node.right);
        return newNode;
    }

    // 辅助方法
    private int height(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(AVLNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    // 搜索方法
    public boolean search(int value) {
        return searchRecursive(root, value);
    }

    private boolean searchRecursive(AVLNode node, int value) {
        if (node == null) return false;
        if (value == node.value) return true;
        return value < node.value ?
                searchRecursive(node.left, value) :
                searchRecursive(node.right, value);
    }

    // 获取树信息
    public int size() {
        return sizeRecursive(root);
    }

    private int sizeRecursive(AVLNode node) {
        if (node == null) return 0;
        return 1 + sizeRecursive(node.left) + sizeRecursive(node.right);
    }

    public int height() {
        return height(root);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public AVLNode getRoot() {
        return root;
    }

    public List<AVLStep> getOperationSteps() {
        return new ArrayList<>(operationSteps);
    }
}