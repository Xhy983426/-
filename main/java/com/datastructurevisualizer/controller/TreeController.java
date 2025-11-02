package com.datastructurevisualizer.controller;

import com.datastructurevisualizer.model.BST;
import com.datastructurevisualizer.model.BinaryTree;
import com.datastructurevisualizer.model.HuffmanTree;
import com.datastructurevisualizer.model.TreeNode;
import com.datastructurevisualizer.view.components.TreeVisualizer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;

public class TreeController implements Initializable {

    @FXML private Pane binaryTreeCanvas;
    @FXML private Pane bstCanvas;
    @FXML private Pane huffmanCanvas;

    // 二叉树控件
    @FXML private TextField binaryTreeValueField;
    @FXML private Button insertBinaryTreeBtn;
    @FXML private ComboBox<String> traversalCombo;
    @FXML private Button traverseBtn;
    @FXML private TextArea binaryTreeOutput;
    @FXML private ComboBox<String> insertMethodCombo;
    @FXML private Button clearBinaryTreeBtn;
    @FXML private Label binaryTreeInfoLabel;

    // BST控件
    @FXML private TextField bstValueField;
    @FXML private Button insertBstBtn;
    @FXML private Button searchBstBtn;
    @FXML private Button deleteBstBtn;
    @FXML private TextArea bstOutput;
    @FXML private Button clearBstBtn;
    @FXML private Label bstInfoLabel;
    @FXML private Label stepInfoLabel;
    @FXML private Button prevStepBtn;
    @FXML private Button nextStepBtn;
    @FXML private Button autoDemoBtn;
    @FXML private Button resetSearchBtn;

    // 哈夫曼树控件
    @FXML private TextField huffmanInputField;
    @FXML private Button buildHuffmanBtn;
    @FXML private TextArea huffmanOutput;
    @FXML private Button clearHuffmanBtn;

    private BinaryTree binaryTree;
    private BST bst;
    private HuffmanTree huffmanTree;
    private TreeVisualizer binaryTreeVisualizer;
    private TreeVisualizer bstVisualizer;
    private TreeVisualizer huffmanVisualizer;

    // 搜索演示相关字段
    private List<BST.SearchStep> currentSearchSteps;
    private int currentStepIndex;
    private Timeline searchAnimation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化可视化组件
        binaryTreeVisualizer = new TreeVisualizer(binaryTreeCanvas);
        bstVisualizer = new TreeVisualizer(bstCanvas);
        huffmanVisualizer = new TreeVisualizer(huffmanCanvas);

        // 初始化数据结构
        binaryTree = new BinaryTree();
        bst = new BST();

        setupComboBoxes();
        setupEventHandlers();
        initializeSearchDemo();

        updateTreeInfo();
    }

    private void setupComboBoxes() {
        // 遍历方式
        traversalCombo.getItems().addAll("前序遍历", "中序遍历", "后序遍历", "层次遍历");
        traversalCombo.setValue("前序遍历");

        // 插入方式
        insertMethodCombo.getItems().addAll("层次插入", "平衡插入", "随机插入");
        insertMethodCombo.setValue("层次插入");
    }

    private void setupEventHandlers() {
        // 二叉树事件
        insertBinaryTreeBtn.setOnAction(e -> insertBinaryTree());
        traverseBtn.setOnAction(e -> traverseTree());
        clearBinaryTreeBtn.setOnAction(e -> clearBinaryTree());

        // BST事件
        insertBstBtn.setOnAction(e -> insertBST());
        searchBstBtn.setOnAction(e -> searchBST());
        deleteBstBtn.setOnAction(e -> deleteBST());
        clearBstBtn.setOnAction(e -> clearBST());

        // 搜索演示事件
        prevStepBtn.setOnAction(e -> previousStep());
        nextStepBtn.setOnAction(e -> nextStep());
        autoDemoBtn.setOnAction(e -> startAutoDemo());
        resetSearchBtn.setOnAction(e -> resetSearch());

        // 哈夫曼树事件
        buildHuffmanBtn.setOnAction(e -> buildHuffmanTree());
        clearHuffmanBtn.setOnAction(e -> clearHuffmanTree());

        // 键盘事件
        setupKeyboardHandlers();
    }

    private void setupKeyboardHandlers() {
        bstCanvas.setFocusTraversable(true);
        bstCanvas.setOnKeyPressed(event -> {
            if (currentSearchSteps == null || currentSearchSteps.isEmpty()) return;

            switch (event.getCode()) {
                case LEFT:
                case A:
                    previousStep();
                    break;
                case RIGHT:
                case D:
                    nextStep();
                    break;
                case HOME:
                    showSearchStep(0);
                    break;
                case END:
                    showSearchStep(currentSearchSteps.size() - 1);
                    break;
                case SPACE:
                    if (searchAnimation != null && searchAnimation.getStatus() == Timeline.Status.RUNNING) {
                        searchAnimation.stop();
                    } else {
                        startAutoDemo();
                    }
                    break;
            }
        });
    }

    private void initializeSearchDemo() {
        currentSearchSteps = new ArrayList<>();
        currentStepIndex = 0;
        updateStepNavigation();
    }

    // ========== 二叉树操作 ==========
    private void insertBinaryTree() {
        try {
            int value = Integer.parseInt(binaryTreeValueField.getText());
            if (binaryTree == null) {
                binaryTree = new BinaryTree();
            }

            String insertMethod = insertMethodCombo.getValue();
            switch (insertMethod) {
                case "层次插入":
                    binaryTree.insert(value);
                    break;
                case "平衡插入":
                    binaryTree.insertBalanced(value);
                    break;
                case "随机插入":
                    binaryTree.insertRandom(value);
                    break;
            }

            binaryTreeOutput.setText("插入节点: " + value + " (方式: " + insertMethod + ")");
            binaryTreeVisualizer.drawBinaryTree(binaryTree.getRoot(), "binary");

            // 清空输入框
            binaryTreeValueField.clear();
            updateTreeInfo();

        } catch (Exception e) {
            binaryTreeOutput.setText("错误: " + e.getMessage());
        }
    }

    private void traverseTree() {
        if (binaryTree == null || binaryTree.isEmpty()) {
            binaryTreeOutput.setText("错误: 二叉树为空");
            return;
        }

        String traversalType = traversalCombo.getValue();
        String result = "";

        switch (traversalType) {
            case "前序遍历":
                result = binaryTree.preOrderTraversal();
                break;
            case "中序遍历":
                result = binaryTree.inOrderTraversal();
                break;
            case "后序遍历":
                result = binaryTree.postOrderTraversal();
                break;
            case "层次遍历":
                result = binaryTree.levelOrderTraversal();
                break;
        }

        binaryTreeOutput.setText(traversalType + ":\n" + result);
    }

    private void clearBinaryTree() {
        binaryTree = new BinaryTree();
        binaryTreeOutput.setText("二叉树已清空");
        binaryTreeVisualizer.drawBinaryTree(null, "binary");
        updateTreeInfo();
    }

    // ========== BST操作 ==========
    private void insertBST() {
        try {
            int value = Integer.parseInt(bstValueField.getText());
            if (bst == null) {
                bst = new BST();
            }
            bst.insert(value);
            bstOutput.setText("插入BST节点: " + value);
            bstVisualizer.drawBinaryTree(bst.getRoot(), "bst");

            // 清空输入框
            bstValueField.clear();
            updateTreeInfo();
            resetSearch(); // 插入新节点后重置搜索状态

        } catch (Exception e) {
            bstOutput.setText("错误: " + e.getMessage());
        }
    }

    private void searchBST() {
        try {
            int value = Integer.parseInt(bstValueField.getText());
            if (bst == null || bst.isEmpty()) {
                bstOutput.setText("错误: BST为空");
                return;
            }

            // 获取搜索步骤
            currentSearchSteps = bst.searchWithSteps(value);
            currentStepIndex = 0;

            if (currentSearchSteps.isEmpty()) {
                bstOutput.setText("搜索过程异常");
                return;
            }

            // 显示第一步
            showSearchStep(currentStepIndex);

            // 更新输出信息
            BST.SearchStep finalStep = currentSearchSteps.get(currentSearchSteps.size() - 1);
            String result = finalStep.found ?
                    "✓ 找到节点: " + value :
                    "✗ 未找到节点: " + value;

            bstOutput.setText(result + "\n搜索步骤数: " + currentSearchSteps.size() +
                    "\n使用左右箭头键或按钮查看详细搜索过程");

        } catch (Exception e) {
            bstOutput.setText("错误: " + e.getMessage());
        }
    }

    private void deleteBST() {
        try {
            int value = Integer.parseInt(bstValueField.getText());
            if (bst == null || bst.isEmpty()) {
                bstOutput.setText("错误: BST为空");
                return;
            }
            bst.delete(value);
            bstOutput.setText("删除BST节点: " + value);
            bstVisualizer.drawBinaryTree(bst.getRoot(), "bst");

            // 清空输入框
            bstValueField.clear();
            updateTreeInfo();
            resetSearch(); // 删除节点后重置搜索状态

        } catch (Exception e) {
            bstOutput.setText("错误: " + e.getMessage());
        }
    }

    private void clearBST() {
        bst = new BST();
        bstOutput.setText("BST已清空");
        bstVisualizer.drawBinaryTree(null, "bst");
        updateTreeInfo();
        resetSearch();
    }

    // ========== 搜索演示操作 ==========
    private void showSearchStep(int stepIndex) {
        if (currentSearchSteps == null || currentSearchSteps.isEmpty()) {
            return;
        }

        if (stepIndex < 0) stepIndex = 0;
        if (stepIndex >= currentSearchSteps.size()) stepIndex = currentSearchSteps.size() - 1;

        currentStepIndex = stepIndex;
        BST.SearchStep step = currentSearchSteps.get(stepIndex);

        // 绘制当前步骤
        bstVisualizer.drawSearchStep(bst.getRoot(), step, stepIndex, currentSearchSteps.size());

        // 更新步骤导航
        updateStepNavigation();

        // 请求焦点以便接收键盘事件
        bstCanvas.requestFocus();
    }

    private void previousStep() {
        if (currentStepIndex > 0) {
            showSearchStep(currentStepIndex - 1);
        }
    }

    private void nextStep() {
        if (currentStepIndex < currentSearchSteps.size() - 1) {
            showSearchStep(currentStepIndex + 1);
        }
    }

    private void startAutoDemo() {
        if (currentSearchSteps == null || currentSearchSteps.isEmpty()) {
            bstOutput.setText("请先执行搜索操作");
            return;
        }

        if (searchAnimation != null) {
            searchAnimation.stop();
        }

        currentStepIndex = 0;
        searchAnimation = new Timeline();

        // 为每个步骤创建关键帧
        for (int i = 0; i < currentSearchSteps.size(); i++) {
            final int stepIndex = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(i * 1.5), // 每1.5秒一个步骤
                    e -> showSearchStep(stepIndex)
            );
            searchAnimation.getKeyFrames().add(keyFrame);
        }

        // 添加完成后的延迟
        KeyFrame finalFrame = new KeyFrame(
                Duration.seconds(currentSearchSteps.size() * 1.5 + 1),
                e -> {
                    bstOutput.setText("自动演示完成\n使用左右箭头键重新查看步骤");
                }
        );
        searchAnimation.getKeyFrames().add(finalFrame);

        searchAnimation.setCycleCount(1);
        searchAnimation.play();

        bstOutput.setText("自动演示中...\n按空格键停止演示");
    }

    private void resetSearch() {
        if (searchAnimation != null) {
            searchAnimation.stop();
        }
        currentSearchSteps.clear();
        currentStepIndex = 0;
        bstVisualizer.drawBinaryTree(bst.getRoot(), "bst");
        updateStepNavigation();
    }

    private void updateStepNavigation() {
        if (currentSearchSteps == null || currentSearchSteps.isEmpty()) {
            stepInfoLabel.setText("步骤: 0/0");
            prevStepBtn.setDisable(true);
            nextStepBtn.setDisable(true);
            autoDemoBtn.setDisable(true);
            resetSearchBtn.setDisable(true);
        } else {
            stepInfoLabel.setText("步骤: " + (currentStepIndex + 1) + "/" + currentSearchSteps.size());
            prevStepBtn.setDisable(currentStepIndex == 0);
            nextStepBtn.setDisable(currentStepIndex == currentSearchSteps.size() - 1);
            autoDemoBtn.setDisable(false);
            resetSearchBtn.setDisable(false);
        }
    }

    // ========== 哈夫曼树操作 ==========
    private void buildHuffmanTree() {
        try {
            String input = huffmanInputField.getText();
            if (input.isEmpty()) {
                huffmanOutput.setText("错误: 请输入文本");
                return;
            }

            huffmanTree = new HuffmanTree();
            var huffmanCodes = huffmanTree.buildTree(input);

            StringBuilder result = new StringBuilder("输入文本: " + input + "\n\n");
            result.append("字符频率统计:\n");

            // 统计字符频率
            java.util.Map<Character, Integer> frequencyMap = new java.util.HashMap<>();
            for (char c : input.toCharArray()) {
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
            }

            for (var entry : frequencyMap.entrySet()) {
                result.append("'").append(entry.getKey()).append("': ").append(entry.getValue()).append("次\n");
            }

            result.append("\n哈夫曼编码:\n");
            for (var entry : huffmanCodes.entrySet()) {
                result.append("'").append(entry.getKey()).append("': ").append(entry.getValue()).append("\n");
            }

            // 计算压缩率
            int originalBits = input.length() * 8; // 假设原始是8位ASCII
            int compressedBits = 0;
            for (char c : input.toCharArray()) {
                compressedBits += huffmanCodes.get(c).length();
            }
            double compressionRatio = (1 - (double)compressedBits / originalBits) * 100;

            result.append(String.format("\n压缩信息:\n"));
            result.append(String.format("原始大小: %d 位\n", originalBits));
            result.append(String.format("压缩后: %d 位\n", compressedBits));
            result.append(String.format("压缩率: %.2f%%\n", compressionRatio));

            huffmanOutput.setText(result.toString());
            huffmanVisualizer.drawHuffmanTree(huffmanTree.getRoot());

        } catch (Exception e) {
            huffmanOutput.setText("错误: " + e.getMessage());
        }
    }

    private void clearHuffmanTree() {
        huffmanTree = null;
        huffmanOutput.setText("哈夫曼树已清空");
        huffmanVisualizer.drawHuffmanTree(null);
        huffmanInputField.clear();
    }

    // ========== 辅助方法 ==========
    private void updateTreeInfo() {
        if (binaryTree != null) {
            binaryTreeInfoLabel.setText(String.format("二叉树: 大小=%d, 高度=%d",
                    binaryTree.getSize(), binaryTree.getHeight()));
        }

        if (bst != null) {
            bstInfoLabel.setText(String.format("BST: 大小=%d, 高度=%d",
                    bst.size(), bst.height()));
        }
    }
}