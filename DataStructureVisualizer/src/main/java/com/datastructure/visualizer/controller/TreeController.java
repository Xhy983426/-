package com.datastructure.visualizer.controller;

import com.datastructure.visualizer.model.*;
import com.datastructure.visualizer.view.TreeView;
import com.datastructure.visualizer.view.components.TreeNodeView;
import com.datastructure.visualizer.view.components.AnimationUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class TreeController {
    private TreeView view;
    private DataStructure<String> currentTree;
    private HuffmanTree huffmanTree;

    public TreeController(TreeView view) {
        this.view = view;
        this.currentTree = new BinaryTree<>();
        this.huffmanTree = new HuffmanTree();
        setupEventHandlers();
        updateView();
    }

    private void setupEventHandlers() {
        // 插入操作
        view.getInsertBtn().setOnAction(e -> {
            String value = view.getInputField().getText().trim();
            if (!value.isEmpty()) {
                insert(value);
                view.getInputField().clear();
            }
        });

        // 删除操作
        view.getDeleteBtn().setOnAction(e -> {
            String value = view.getInputField().getText().trim();
            if (!value.isEmpty()) {
                delete(value);
                view.getInputField().clear();
            }
        });

        // 查找操作
        view.getSearchBtn().setOnAction(e -> {
            String value = view.getInputField().getText().trim();
            if (!value.isEmpty()) {
                search(value);
                view.getInputField().clear();
            }
        });

        // 清空操作
        view.getClearBtn().setOnAction(e -> clear());

        // 构建哈夫曼树
        view.getBuildHuffmanBtn().setOnAction(e -> buildHuffmanTree());

        // 树类型切换
        view.getTreeType().setOnAction(e -> changeTreeType(view.getTreeType().getValue()));

        // 回车键插入
        view.getInputField().setOnAction(e -> {
            String value = view.getInputField().getText().trim();
            if (!value.isEmpty()) {
                insert(value);
                view.getInputField().clear();
            }
        });
    }

    private void insert(String value) {
        currentTree.insert(value);
        updateView();
        view.logOperation("插入: " + value + " -> " + currentTree.visualize());
    }

    private void delete(String value) {
        boolean existed = currentTree.search(value);
        currentTree.delete(value);
        updateView();

        if (existed) {
            view.logOperation("删除: " + value + " -> " + currentTree.visualize());
        } else {
            view.logOperation("删除失败: " + value + " 不存在");
        }
    }

    private void search(String value) {
        boolean found = currentTree.search(value);

        // 高亮找到的节点
        TreeNodeView rootView = createTreeView();
        highlightNode(rootView, value, found);
        view.updateVisualization(rootView);

        view.logOperation("查找: " + value + " -> " + (found ? "找到" : "未找到"));

        Alert alert = new Alert(found ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING);
        alert.setTitle("查找结果");
        alert.setHeaderText(null);
        alert.setContentText("数值 " + value + " " + (found ? "找到!" : "未找到!"));
        alert.show();
    }

    private void clear() {
        currentTree.clear();
        huffmanTree = new HuffmanTree();
        updateView();
        view.clearLog();
        view.logOperation("清空树结构");
    }

    private void buildHuffmanTree() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("构建哈夫曼树");
        dialog.setHeaderText("请输入要编码的文本:");
        dialog.setContentText("文本:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String text = result.get().trim();
            huffmanTree.buildTree(text);
            updateView();

            // 显示哈夫曼编码
            StringBuilder codes = new StringBuilder("哈夫曼编码:\n");
            for (var entry : huffmanTree.getHuffmanCodes().entrySet()) {
                codes.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            codes.append("编码结果: ").append(huffmanTree.getEncodedText());

            view.logOperation("构建哈夫曼树: " + text);
            view.logOperation(codes.toString());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("哈夫曼编码");
            alert.setHeaderText("编码结果");
            alert.setContentText(codes.toString());
            alert.show();
        }
    }

    private void changeTreeType(String treeType) {
        switch (treeType) {
            case "二叉树":
                currentTree = new BinaryTree<>();
                break;
            case "二叉搜索树":
                currentTree = new BST<>();
                break;
            case "哈夫曼树":
                // 哈夫曼树需要单独构建
                currentTree = new BinaryTree<>();
                break;
        }
        updateView();
        view.clearLog();
        view.logOperation("切换到: " + treeType);
    }

    private void updateView() {
        TreeNodeView rootView = createTreeView();
        view.updateVisualization(rootView);

        String treeInfo;
        if (view.getTreeType().getValue().equals("哈夫曼树") && huffmanTree.getRoot() != null) {
            treeInfo = "哈夫曼树 | 叶子节点: " + countLeaves(huffmanTree.getRoot());
        } else {
            treeInfo = view.getTreeType().getValue() + " | 节点数量: " + currentTree.size();
        }

        view.setInfoText("当前树: " + treeInfo + " | 状态: " +
                (currentTree.isEmpty() && huffmanTree.getRoot() == null ? "空" : "非空"));
    }

    private TreeNodeView createTreeView() {
        if (view.getTreeType().getValue().equals("哈夫曼树")) {
            return buildHuffmanTreeView(huffmanTree.getRoot());
        } else if (currentTree instanceof BinaryTree) {
            BinaryTree<String> binaryTree = (BinaryTree<String>) currentTree;
            return buildTreeView(binaryTree.getRoot());
        }
        return null;
    }

    private TreeNodeView buildTreeView(com.datastructure.visualizer.model.TreeNode<String> node) {
        if (node == null) return null;

        TreeNodeView viewNode = new TreeNodeView(node.data);
        viewNode.setLeft(buildTreeView(node.left));
        viewNode.setRight(buildTreeView(node.right));

        return viewNode;
    }

    private TreeNodeView buildHuffmanTreeView(HuffmanTree.HuffmanNode node) {
        if (node == null) return null;

        String displayValue = node.isLeaf() ?
                "'" + node.character + "'\\n" + node.frequency :
                "Freq:\\n" + node.frequency;

        TreeNodeView viewNode = new TreeNodeView(displayValue);
        viewNode.setLeft(buildHuffmanTreeView(node.left));
        viewNode.setRight(buildHuffmanTreeView(node.right));

        return viewNode;
    }

    private void highlightNode(TreeNodeView node, String value, boolean found) {
        if (node == null) return;

        if (node.getValue().contains(value)) {
            if (found) {
                node.markFound();
                AnimationUtils.pulseNode(node);
            } else {
                node.markDeleted();
            }
        } else {
            node.resetColor();
        }

        highlightNode(node.getLeft(), value, found);
        highlightNode(node.getRight(), value, found);
    }

    private int countLeaves(HuffmanTree.HuffmanNode node) {
        if (node == null) return 0;
        if (node.isLeaf()) return 1;
        return countLeaves(node.left) + countLeaves(node.right);
    }
}