package com.datastructurevisualizer.controller;

import com.datastructurevisualizer.model.*;
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
import java.util.List;
import java.util.ResourceBundle;

public class TreeController implements Initializable {

    // ========== FXML 组件声明 ==========

    // 画布区域
    @FXML
    private Pane binaryTreeCanvas;
    @FXML
    private Pane bstCanvas;
    @FXML
    private Pane huffmanCanvas;
    @FXML
    private Pane avlCanvas;

    // 二叉树控件
    @FXML
    private TextField binaryTreeValueField;
    @FXML
    private Button insertBinaryTreeBtn;
    @FXML
    private ComboBox<String> traversalCombo;
    @FXML
    private Button traverseBtn;
    @FXML
    private TextArea binaryTreeOutput;
    @FXML
    private ComboBox<String> insertMethodCombo;
    @FXML
    private Button clearBinaryTreeBtn;
    @FXML
    private Label binaryTreeInfoLabel;
    @FXML
    private Button traversalStepPrevBtn;
    @FXML
    private Button traversalStepNextBtn;
    @FXML
    private Button traversalAutoDemoBtn;
    @FXML
    private Label traversalStepInfoLabel;
    @FXML
    private Button resetTraversalBtn;

    // BST控件
    @FXML
    private TextField bstValueField;
    @FXML
    private Button insertBstBtn;
    @FXML
    private Button searchBstBtn;
    @FXML
    private Button deleteBstBtn;
    @FXML
    private TextArea bstOutput;
    @FXML
    private Button clearBstBtn;
    @FXML
    private Label bstInfoLabel;
    @FXML
    private Label stepInfoLabel;
    @FXML
    private Button prevStepBtn;
    @FXML
    private Button nextStepBtn;
    @FXML
    private Button autoDemoBtn;
    @FXML
    private Button resetSearchBtn;
    @FXML
    private Button prevDeleteStepBtn;
    @FXML
    private Button nextDeleteStepBtn;
    @FXML
    private Button deleteAutoDemoBtn;
    @FXML
    private Button executeDeleteBtn;
    @FXML
    private Button resetDeleteBtn;
    @FXML
    private Label deleteStepInfoLabel;

    // 哈夫曼树控件
    @FXML
    private TextField huffmanInputField;
    @FXML
    private Button buildHuffmanBtn;
    @FXML
    private TextArea huffmanOutput;
    @FXML
    private Button clearHuffmanBtn;
    @FXML
    private Button prevHuffmanStepBtn;
    @FXML
    private Button nextHuffmanStepBtn;
    @FXML
    private Button huffmanAutoDemoBtn;
    @FXML
    private Label huffmanStepInfoLabel;
    @FXML
    private Button resetHuffmanBtn;

    // AVL树控件
    @FXML
    private TextField avlValueField;
    @FXML
    private Button insertAvlBtn;
    @FXML
    private Button searchAvlBtn;
    @FXML
    private TextArea avlOutput;
    @FXML
    private Button clearAvlBtn;
    @FXML
    private Label avlInfoLabel;
    @FXML
    private Button prevAvlStepBtn;
    @FXML
    private Button nextAvlStepBtn;
    @FXML
    private Button avlAutoDemoBtn;
    @FXML
    private Label avlStepInfoLabel;
    @FXML
    private Button resetAvlBtn;

    // 树存档管理控件
    @FXML
    private TextField treeArchiveNameField;
    @FXML
    private TextField treeArchiveDescriptionField;
    @FXML
    private ComboBox<String> savedTreeFilesCombo;
    @FXML
    private Button refreshTreeSavedBtn;
    @FXML
    private Button deleteTreeSaveBtn;
    @FXML
    private Button saveBinaryTreeBtn;
    @FXML
    private Button loadBinaryTreeBtn;
    @FXML
    private Button saveBstBtn;
    @FXML
    private Button loadBstBtn;
    @FXML
    private Button saveHuffmanBtn;
    @FXML
    private Button loadHuffmanBtn;
    @FXML
    private Button saveAvlBtn;
    @FXML
    private Button loadAvlBtn;

    // ========== 模型和视图对象 ==========
    private BinaryTree binaryTree;
    private BST bst;
    private HuffmanTree huffmanTree;
    private AVLTree avlTree;
    private TreeVisualizer binaryTreeVisualizer;
    private TreeVisualizer bstVisualizer;
    private TreeVisualizer huffmanVisualizer;
    private TreeVisualizer avlVisualizer;

    // ========== 步骤演示相关字段 ==========
    private List<BinaryTree.TraversalStep> currentTraversalSteps;
    private int currentTraversalStepIndex;
    private Timeline traversalAnimation;

    // BST搜索演示
    private List<BST.SearchStep> currentSearchSteps;
    private int currentStepIndex;
    private Timeline searchAnimation;
    private List<BST.DeleteStep> currentDeleteSteps;
    private int currentDeleteStepIndex;
    private Timeline deleteAnimation;

    // 哈夫曼树构建演示
    private List<HuffmanTree.HuffmanStep> currentHuffmanSteps;
    private int currentHuffmanStepIndex;
    private Timeline huffmanAnimation;

    // AVL树构建演示
    private List<AVLTree.AVLStep> currentAvlSteps;
    private int currentAvlStepIndex;
    private Timeline avlAnimation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化可视化组件
        binaryTreeVisualizer = new TreeVisualizer(binaryTreeCanvas);
        bstVisualizer = new TreeVisualizer(bstCanvas);
        huffmanVisualizer = new TreeVisualizer(huffmanCanvas);
        avlVisualizer = new TreeVisualizer(avlCanvas);
        currentTraversalSteps = new ArrayList<>();
        currentTraversalStepIndex = 0;

        // 初始化数据结构
        binaryTree = new BinaryTree();
        bst = new BST();
        huffmanTree = new HuffmanTree();
        avlTree = new AVLTree();

        setupComboBoxes();
        setupEventHandlers();
        setupTreeArchiveHandlers();
        initializeStepDemos();
        setupTraversalEventHandlers();
        updateTraversalStepNavigation();
        updateTreeInfo();
        refreshTreeSavedFiles();
    }

    private void setupTreeArchiveHandlers() {
        // 二叉树存档
        saveBinaryTreeBtn.setOnAction(e -> saveBinaryTree());
        loadBinaryTreeBtn.setOnAction(e -> loadBinaryTree());

        // BST存档
        saveBstBtn.setOnAction(e -> saveBST());
        loadBstBtn.setOnAction(e -> loadBST());

        // 哈夫曼树存档
        saveHuffmanBtn.setOnAction(e -> saveHuffmanTree());
        loadHuffmanBtn.setOnAction(e -> loadHuffmanTree());

        // AVL树存档
        saveAvlBtn.setOnAction(e -> saveAVLTree());
        loadAvlBtn.setOnAction(e -> loadAVLTree());

        // 通用存档操作
        refreshTreeSavedBtn.setOnAction(e -> refreshTreeSavedFiles());
        deleteTreeSaveBtn.setOnAction(e -> deleteTreeSaveFile());
    }

    private void setupTraversalEventHandlers() {
        // 遍历演示事件处理
        traversalStepPrevBtn.setOnAction(e -> previousTraversalStep());
        traversalStepNextBtn.setOnAction(e -> nextTraversalStep());
        traversalAutoDemoBtn.setOnAction(e -> startTraversalAutoDemo());
        resetTraversalBtn.setOnAction(e -> resetTraversalSteps());
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

        // BST搜索演示事件
        prevStepBtn.setOnAction(e -> previousStep());
        nextStepBtn.setOnAction(e -> nextStep());
        autoDemoBtn.setOnAction(e -> startAutoDemo());
        resetSearchBtn.setOnAction(e -> resetSearch());

        // BST删除演示事件
        prevDeleteStepBtn.setOnAction(e -> previousDeleteStep());
        nextDeleteStepBtn.setOnAction(e -> nextDeleteStep());
        deleteAutoDemoBtn.setOnAction(e -> startDeleteAutoDemo());
        executeDeleteBtn.setOnAction(e -> performActualDeletion());
        resetDeleteBtn.setOnAction(e -> resetDeleteSteps());

        // 哈夫曼树事件
        buildHuffmanBtn.setOnAction(e -> buildHuffmanTree());
        clearHuffmanBtn.setOnAction(e -> clearHuffmanTree());

        // 哈夫曼树步骤导航事件
        prevHuffmanStepBtn.setOnAction(e -> previousHuffmanStep());
        nextHuffmanStepBtn.setOnAction(e -> nextHuffmanStep());
        huffmanAutoDemoBtn.setOnAction(e -> startHuffmanAutoDemo());
        resetHuffmanBtn.setOnAction(e -> resetHuffmanSteps());

        // AVL树事件
        insertAvlBtn.setOnAction(e -> insertAVL());
        searchAvlBtn.setOnAction(e -> searchAVL());
        clearAvlBtn.setOnAction(e -> clearAVL());

        // AVL树步骤导航事件
        prevAvlStepBtn.setOnAction(e -> previousAvlStep());
        nextAvlStepBtn.setOnAction(e -> nextAvlStep());
        avlAutoDemoBtn.setOnAction(e -> startAvlAutoDemo());
        resetAvlBtn.setOnAction(e -> resetAvlSteps());

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

        huffmanCanvas.setFocusTraversable(true);
        huffmanCanvas.setOnKeyPressed(event -> {
            if (currentHuffmanSteps == null || currentHuffmanSteps.isEmpty()) return;

            switch (event.getCode()) {
                case LEFT:
                case A:
                    previousHuffmanStep();
                    break;
                case RIGHT:
                case D:
                    nextHuffmanStep();
                    break;
                case HOME:
                    showHuffmanStep(0);
                    break;
                case END:
                    showHuffmanStep(currentHuffmanSteps.size() - 1);
                    break;
                case SPACE:
                    if (huffmanAnimation != null && huffmanAnimation.getStatus() == Timeline.Status.RUNNING) {
                        huffmanAnimation.stop();
                    } else {
                        startHuffmanAutoDemo();
                    }
                    break;
            }
        });

        avlCanvas.setFocusTraversable(true);
        avlCanvas.setOnKeyPressed(event -> {
            if (currentAvlSteps == null || currentAvlSteps.isEmpty()) return;

            switch (event.getCode()) {
                case LEFT:
                case A:
                    previousAvlStep();
                    break;
                case RIGHT:
                case D:
                    nextAvlStep();
                    break;
                case HOME:
                    showAvlStep(0);
                    break;
                case END:
                    showAvlStep(currentAvlSteps.size() - 1);
                    break;
                case SPACE:
                    if (avlAnimation != null && avlAnimation.getStatus() == Timeline.Status.RUNNING) {
                        avlAnimation.stop();
                    } else {
                        startAvlAutoDemo();
                    }
                    break;
            }
        });
    }

    private void initializeStepDemos() {
        // BST搜索演示初始化
        currentSearchSteps = new ArrayList<>();
        currentStepIndex = 0;
        currentDeleteSteps = new ArrayList<>();
        currentDeleteStepIndex = 0;

        // 哈夫曼树步骤演示初始化
        currentHuffmanSteps = new ArrayList<>();
        currentHuffmanStepIndex = 0;

        // AVL树步骤演示初始化
        currentAvlSteps = new ArrayList<>();
        currentAvlStepIndex = 0;

        updateStepNavigation();
        updateHuffmanStepNavigation();
        updateAvlStepNavigation();
        updateDeleteStepNavigation();
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
        resetTreeVisitedState();

        List<BinaryTree.TraversalStep> steps = null;
        switch (traversalType) {
            case "前序遍历":
                steps = binaryTree.preOrderTraversalWithSteps();
                break;
            case "中序遍历":
                steps = binaryTree.inOrderTraversalWithSteps();
                break;
            case "后序遍历":
                steps = binaryTree.postOrderTraversalWithSteps();
                break;
            case "层次遍历":
                steps = binaryTree.levelOrderTraversalWithSteps();
                break;
        }

        if (steps != null && !steps.isEmpty()) {
            currentTraversalSteps = steps;
            currentTraversalStepIndex = 0;
            showTraversalStep(currentTraversalStepIndex);
            binaryTreeOutput.setText("开始" + traversalType + "演示...\n使用导航按钮查看详细步骤");
        } else {
            String result = getTraversalResult(traversalType);
            binaryTreeOutput.setText(traversalType + ":\n" + result);
            binaryTreeVisualizer.drawBinaryTree(binaryTree.getRoot(), "binary");
        }
    }

    private String getTraversalResult(String traversalType) {
        switch (traversalType) {
            case "前序遍历":
                return binaryTree.preOrderTraversal();
            case "中序遍历":
                return binaryTree.inOrderTraversal();
            case "后序遍历":
                return binaryTree.postOrderTraversal();
            case "层次遍历":
                return binaryTree.levelOrderTraversal();
            default:
                return "";
        }
    }

    private void resetTreeVisitedState() {
        if (binaryTree != null && binaryTree.getRoot() != null) {
            resetAllNodesVisited(binaryTree.getRoot());
        }
    }

    private void resetAllNodesVisited(TreeNode node) {
        if (node == null) return;
        node.setVisited(false);
        resetAllNodesVisited(node.getLeft());
        resetAllNodesVisited(node.getRight());
    }

    private void showTraversalStep(int stepIndex) {
        if (currentTraversalSteps == null || currentTraversalSteps.isEmpty()) {
            binaryTreeOutput.setText("错误: 没有遍历步骤数据");
            return;
        }

        if (stepIndex < 0) stepIndex = 0;
        if (stepIndex >= currentTraversalSteps.size()) stepIndex = currentTraversalSteps.size() - 1;

        currentTraversalStepIndex = stepIndex;
        BinaryTree.TraversalStep step = currentTraversalSteps.get(stepIndex);

        binaryTreeVisualizer.drawTraversalStep(binaryTree.getRoot(), step, stepIndex, currentTraversalSteps.size());
        updateTraversalStepNavigation();

        String output = "步骤 " + (stepIndex + 1) + "/" + currentTraversalSteps.size() +
                "\n" + step.description;

        if (step.currentNode != null) {
            output += "\n当前节点: " + step.currentNode.getValue();
        }

        output += "\n已访问节点: " + step.visitedNodes;
        binaryTreeOutput.setText(output);
    }

    private void previousTraversalStep() {
        if (currentTraversalStepIndex > 0) {
            showTraversalStep(currentTraversalStepIndex - 1);
        }
    }

    private void nextTraversalStep() {
        if (currentTraversalStepIndex < currentTraversalSteps.size() - 1) {
            showTraversalStep(currentTraversalStepIndex + 1);
        }
    }

    private void startTraversalAutoDemo() {
        if (currentTraversalSteps == null || currentTraversalSteps.isEmpty()) {
            binaryTreeOutput.setText("请先执行遍历操作");
            return;
        }

        if (traversalAnimation != null) {
            traversalAnimation.stop();
        }

        currentTraversalStepIndex = 0;
        traversalAnimation = new Timeline();

        for (int i = 0; i < currentTraversalSteps.size(); i++) {
            final int stepIndex = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(i * 1.5),
                    e -> showTraversalStep(stepIndex)
            );
            traversalAnimation.getKeyFrames().add(keyFrame);
        }

        KeyFrame finalFrame = new KeyFrame(
                Duration.seconds(currentTraversalSteps.size() * 1.5 + 1),
                e -> {
                    resetTraversalSteps();
                    binaryTreeOutput.setText("自动演示完成！树状态已恢复");
                }
        );
        traversalAnimation.getKeyFrames().add(finalFrame);

        traversalAnimation.setCycleCount(1);
        traversalAnimation.play();
        binaryTreeOutput.setText("自动演示中...\n演示完成后树状态将自动恢复");
    }

    private void resetTraversalSteps() {
        if (traversalAnimation != null) {
            traversalAnimation.stop();
        }

        currentTraversalSteps.clear();
        currentTraversalStepIndex = 0;
        resetTreeVisitedState();

        if (binaryTree != null) {
            binaryTreeVisualizer.drawBinaryTree(binaryTree.getRoot(), "binary");
        }

        updateTraversalStepNavigation();
        binaryTreeOutput.setText("遍历演示已重置");
    }

    private void updateTraversalStepNavigation() {
        if (currentTraversalSteps == null || currentTraversalSteps.isEmpty()) {
            traversalStepInfoLabel.setText("步骤: 0/0");
            traversalStepPrevBtn.setDisable(true);
            traversalStepNextBtn.setDisable(true);
            traversalAutoDemoBtn.setDisable(true);
            resetTraversalBtn.setDisable(true);
        } else {
            traversalStepInfoLabel.setText("步骤: " + (currentTraversalStepIndex + 1) + "/" + currentTraversalSteps.size());
            traversalStepPrevBtn.setDisable(currentTraversalStepIndex == 0);
            traversalStepNextBtn.setDisable(currentTraversalStepIndex == currentTraversalSteps.size() - 1);
            traversalAutoDemoBtn.setDisable(false);
            resetTraversalBtn.setDisable(false);
        }
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
            bstValueField.clear();
            updateTreeInfo();
            resetSearch();

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

            currentSearchSteps = bst.searchWithSteps(value);
            currentStepIndex = 0;

            if (currentSearchSteps.isEmpty()) {
                bstOutput.setText("搜索过程异常");
                return;
            }

            showSearchStep(currentStepIndex);

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

    private void clearBST() {
        bst = new BST();
        bstOutput.setText("BST已清空");
        bstVisualizer.drawBinaryTree(null, "bst");
        updateTreeInfo();
        resetSearch();
    }

    // ========== BST搜索演示操作 ==========

    private void showSearchStep(int stepIndex) {
        if (currentSearchSteps == null || currentSearchSteps.isEmpty()) {
            return;
        }

        if (stepIndex < 0) stepIndex = 0;
        if (stepIndex >= currentSearchSteps.size()) stepIndex = currentSearchSteps.size() - 1;

        currentStepIndex = stepIndex;
        BST.SearchStep step = currentSearchSteps.get(stepIndex);

        bstVisualizer.drawSearchStep(bst.getRoot(), step, stepIndex, currentSearchSteps.size());
        updateStepNavigation();
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

        for (int i = 0; i < currentSearchSteps.size(); i++) {
            final int stepIndex = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(i * 1.5),
                    e -> showSearchStep(stepIndex)
            );
            searchAnimation.getKeyFrames().add(keyFrame);
        }

        KeyFrame finalFrame = new KeyFrame(
                Duration.seconds(currentSearchSteps.size() * 1.5 + 1),
                e -> bstOutput.setText("自动演示完成\n使用左右箭头键重新查看步骤")
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

    // ========== BST删除操作 ==========

    private void deleteBST() {
        try {
            int value = Integer.parseInt(bstValueField.getText());
            if (bst == null || bst.isEmpty()) {
                bstOutput.setText("错误: BST为空");
                return;
            }

            currentDeleteSteps = bst.deleteWithSteps(value);
            currentDeleteStepIndex = 0;

            if (!currentDeleteSteps.isEmpty()) {
                showDeleteStep(currentDeleteStepIndex);
                bstOutput.setText("开始删除步骤演示...\n当前仅展示步骤，树结构未被修改");
            }

            bstValueField.clear();

        } catch (Exception e) {
            bstOutput.setText("错误: " + e.getMessage());
        }
    }

    private void performActualDeletion() {
        try {
            int value = Integer.parseInt(bstValueField.getText());
            if (bst != null) {
                bst.performActualDeletion(value);
                bstVisualizer.drawBinaryTree(bst.getRoot(), "bst");
                updateTreeInfo();
                bstOutput.setText("✅ 删除操作已实际执行\n节点 " + value + " 已被删除");

                currentDeleteSteps.clear();
                currentDeleteStepIndex = 0;
                updateDeleteStepNavigation();
            }
        } catch (Exception e) {
            bstOutput.setText("执行删除时出错: " + e.getMessage());
        }
    }

    private void showDeleteStep(int stepIndex) {
        if (currentDeleteSteps == null || currentDeleteSteps.isEmpty()) {
            return;
        }

        if (stepIndex < 0) stepIndex = 0;
        if (stepIndex >= currentDeleteSteps.size()) stepIndex = currentDeleteSteps.size() - 1;

        currentDeleteStepIndex = stepIndex;
        BST.DeleteStep step = currentDeleteSteps.get(stepIndex);

        bstVisualizer.drawDeleteStep(bst.getRoot(), step, stepIndex, currentDeleteSteps.size());
        updateDeleteStepNavigation();

        String status = "🔍 步骤演示中 (树结构未改变)\n";
        bstOutput.setText(status + "步骤 " + (stepIndex + 1) + "/" + currentDeleteSteps.size() +
                "\n" + step.description);

        bstCanvas.requestFocus();
    }

    private void previousDeleteStep() {
        if (currentDeleteStepIndex > 0) {
            showDeleteStep(currentDeleteStepIndex - 1);
        }
    }

    private void nextDeleteStep() {
        if (currentDeleteStepIndex < currentDeleteSteps.size() - 1) {
            showDeleteStep(currentDeleteStepIndex + 1);
        }
    }

    private void startDeleteAutoDemo() {
        if (currentDeleteSteps == null || currentDeleteSteps.isEmpty()) {
            bstOutput.setText("请先执行删除操作");
            return;
        }

        if (deleteAnimation != null) {
            deleteAnimation.stop();
        }

        currentDeleteStepIndex = 0;
        deleteAnimation = new Timeline();

        for (int i = 0; i < currentDeleteSteps.size(); i++) {
            final int stepIndex = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(i * 2.0),
                    e -> showDeleteStep(stepIndex)
            );
            deleteAnimation.getKeyFrames().add(keyFrame);
        }

        KeyFrame finalFrame = new KeyFrame(
                Duration.seconds(currentDeleteSteps.size() * 2.0 + 1),
                e -> bstOutput.setText("自动演示完成！使用导航按钮重新查看步骤")
        );
        deleteAnimation.getKeyFrames().add(finalFrame);

        deleteAnimation.setCycleCount(1);
        deleteAnimation.play();
        bstOutput.setText("删除自动演示中...\n按空格键停止演示");
    }

    private void resetDeleteSteps() {
        if (deleteAnimation != null) {
            deleteAnimation.stop();
        }
        currentDeleteSteps.clear();
        currentDeleteStepIndex = 0;
        bstVisualizer.drawBinaryTree(bst.getRoot(), "bst");
        updateDeleteStepNavigation();
        bstOutput.setText("删除演示已重置");
    }

    private void updateDeleteStepNavigation() {
        if (currentDeleteSteps == null || currentDeleteSteps.isEmpty()) {
            prevDeleteStepBtn.setDisable(true);
            nextDeleteStepBtn.setDisable(true);
            deleteAutoDemoBtn.setDisable(true);
            resetDeleteBtn.setDisable(true);
            deleteStepInfoLabel.setText("步骤: 0/0");
        } else {
            deleteStepInfoLabel.setText("步骤: " + (currentDeleteStepIndex + 1) + "/" + currentDeleteSteps.size());
            prevDeleteStepBtn.setDisable(currentDeleteStepIndex == 0);
            nextDeleteStepBtn.setDisable(currentDeleteStepIndex == currentDeleteSteps.size() - 1);
            deleteAutoDemoBtn.setDisable(false);
            resetDeleteBtn.setDisable(false);
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
            currentHuffmanSteps = huffmanTree.buildTreeWithSteps(input);
            currentHuffmanStepIndex = 0;

            if (!currentHuffmanSteps.isEmpty()) {
                showHuffmanStep(currentHuffmanStepIndex);
                huffmanOutput.setText("开始哈夫曼树构建演示... 使用导航按钮查看详细步骤");
            }

        } catch (Exception e) {
            huffmanOutput.setText("错误: " + e.getMessage());
        }
    }

    private void clearHuffmanTree() {
        huffmanTree = new HuffmanTree();
        huffmanOutput.setText("哈夫曼树已清空");
        huffmanVisualizer.drawHuffmanTree(null);
        huffmanInputField.clear();
        resetHuffmanSteps();
    }

    // ========== 哈夫曼树步骤演示操作 ==========

    private void showHuffmanStep(int stepIndex) {
        if (currentHuffmanSteps == null || currentHuffmanSteps.isEmpty()) {
            return;
        }

        if (stepIndex < 0) stepIndex = 0;
        if (stepIndex >= currentHuffmanSteps.size()) stepIndex = currentHuffmanSteps.size() - 1;

        currentHuffmanStepIndex = stepIndex;
        HuffmanTree.HuffmanStep step = currentHuffmanSteps.get(stepIndex);

        huffmanVisualizer.drawHuffmanStep(huffmanTree.getRoot(), step,
                stepIndex, currentHuffmanSteps.size());
        updateHuffmanStepNavigation();
        huffmanOutput.setText("步骤 " + (stepIndex + 1) + "/" + currentHuffmanSteps.size() +
                "\n" + step.description);
        huffmanCanvas.requestFocus();
    }

    private void previousHuffmanStep() {
        if (currentHuffmanStepIndex > 0) {
            showHuffmanStep(currentHuffmanStepIndex - 1);
        }
    }

    private void nextHuffmanStep() {
        if (currentHuffmanStepIndex < currentHuffmanSteps.size() - 1) {
            showHuffmanStep(currentHuffmanStepIndex + 1);
        }
    }

    private void startHuffmanAutoDemo() {
        if (currentHuffmanSteps == null || currentHuffmanSteps.isEmpty()) {
            huffmanOutput.setText("请先构建哈夫曼树");
            return;
        }

        if (huffmanAnimation != null) {
            huffmanAnimation.stop();
        }

        currentHuffmanStepIndex = 0;
        huffmanAnimation = new Timeline();

        for (int i = 0; i < currentHuffmanSteps.size(); i++) {
            final int stepIndex = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(i * 2.0),
                    e -> showHuffmanStep(stepIndex)
            );
            huffmanAnimation.getKeyFrames().add(keyFrame);
        }

        KeyFrame finalFrame = new KeyFrame(
                Duration.seconds(currentHuffmanSteps.size() * 2.0 + 1),
                e -> huffmanOutput.setText("自动演示完成！使用导航按钮重新查看步骤")
        );
        huffmanAnimation.getKeyFrames().add(finalFrame);

        huffmanAnimation.setCycleCount(1);
        huffmanAnimation.play();
        huffmanOutput.setText("自动演示中...\n按空格键停止演示");
    }

    private void resetHuffmanSteps() {
        if (huffmanAnimation != null) {
            huffmanAnimation.stop();
        }
        currentHuffmanSteps.clear();
        currentHuffmanStepIndex = 0;
        updateHuffmanStepNavigation();
    }

    private void updateHuffmanStepNavigation() {
        if (currentHuffmanSteps == null || currentHuffmanSteps.isEmpty()) {
            huffmanStepInfoLabel.setText("步骤: 0/0");
            prevHuffmanStepBtn.setDisable(true);
            nextHuffmanStepBtn.setDisable(true);
            huffmanAutoDemoBtn.setDisable(true);
            resetHuffmanBtn.setDisable(true);
        } else {
            huffmanStepInfoLabel.setText("步骤: " + (currentHuffmanStepIndex + 1) + "/" + currentHuffmanSteps.size());
            prevHuffmanStepBtn.setDisable(currentHuffmanStepIndex == 0);
            nextHuffmanStepBtn.setDisable(currentHuffmanStepIndex == currentHuffmanSteps.size() - 1);
            huffmanAutoDemoBtn.setDisable(false);
            resetHuffmanBtn.setDisable(false);
        }
    }

    // ========== AVL树操作 ==========

    private void insertAVL() {
        try {
            int value = Integer.parseInt(avlValueField.getText());
            currentAvlSteps = avlTree.insertWithSteps(value);
            currentAvlStepIndex = 0;

            if (!currentAvlSteps.isEmpty()) {
                showAvlStep(currentAvlStepIndex);
                avlOutput.setText("开始AVL树插入演示... 使用导航按钮查看详细步骤");
            }

            avlValueField.clear();
            updateTreeInfo();

        } catch (Exception e) {
            avlOutput.setText("错误: " + e.getMessage());
        }
    }

    private void searchAVL() {
        try {
            int value = Integer.parseInt(avlValueField.getText());
            if (avlTree == null || avlTree.isEmpty()) {
                avlOutput.setText("错误: AVL树为空");
                return;
            }

            boolean found = avlTree.search(value);
            String result = found ? "✓ 找到节点: " + value : "✗ 未找到节点: " + value;
            avlOutput.setText(result);

            if (found) {
                avlVisualizer.drawAVLTree(avlTree.getRoot());
            }

        } catch (Exception e) {
            avlOutput.setText("错误: " + e.getMessage());
        }
    }

    private void clearAVL() {
        avlTree = new AVLTree();
        avlOutput.setText("AVL树已清空");
        avlVisualizer.drawAVLTree(null);
        updateTreeInfo();
        resetAvlSteps();
    }

    // ========== AVL树步骤演示操作 ==========

    private void showAvlStep(int stepIndex) {
        if (currentAvlSteps == null || currentAvlSteps.isEmpty()) {
            return;
        }

        if (stepIndex < 0) stepIndex = 0;
        if (stepIndex >= currentAvlSteps.size()) stepIndex = currentAvlSteps.size() - 1;

        currentAvlStepIndex = stepIndex;
        AVLTree.AVLStep step = currentAvlSteps.get(stepIndex);

        AVLTree.AVLNode currentTreeState = step.treeState != null ? step.treeState : avlTree.getRoot();
        avlVisualizer.drawAVLTreeWithSteps(currentTreeState, step, stepIndex, currentAvlSteps.size());
        updateAvlStepNavigation();

        String output = "步骤 " + (stepIndex + 1) + "/" + currentAvlSteps.size() +
                "\n" + step.description;

        if (step.balanceFactor != 0) {
            output += "\n平衡因子: " + step.balanceFactor;
            if (step.needsRotation) {
                output += " → 需要旋转";
            } else {
                output += " → 平衡状态";
            }
        }
        if (step.rotationType != null) {
            output += "\n旋转类型: " + step.rotationType;
        }
        if (step.rotationCase != null) {
            output += "\n旋转情况: " + step.rotationCase;
        }

        avlOutput.setText(output);
        avlCanvas.requestFocus();
    }

    private void previousAvlStep() {
        if (currentAvlStepIndex > 0) {
            showAvlStep(currentAvlStepIndex - 1);
        }
    }

    private void nextAvlStep() {
        if (currentAvlStepIndex < currentAvlSteps.size() - 1) {
            showAvlStep(currentAvlStepIndex + 1);
        }
    }

    private void startAvlAutoDemo() {
        if (currentAvlSteps == null || currentAvlSteps.isEmpty()) {
            avlOutput.setText("请先执行插入操作");
            return;
        }

        if (avlAnimation != null) {
            avlAnimation.stop();
        }

        currentAvlStepIndex = 0;
        avlAnimation = new Timeline();

        for (int i = 0; i < currentAvlSteps.size(); i++) {
            final int stepIndex = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(i * 1.5),
                    e -> showAvlStep(stepIndex)
            );
            avlAnimation.getKeyFrames().add(keyFrame);
        }

        KeyFrame finalFrame = new KeyFrame(
                Duration.seconds(currentAvlSteps.size() * 1.5 + 1),
                e -> avlOutput.setText("自动演示完成！使用导航按钮重新查看步骤")
        );
        avlAnimation.getKeyFrames().add(finalFrame);

        avlAnimation.setCycleCount(1);
        avlAnimation.play();
        avlOutput.setText("自动演示中...\n按空格键停止演示");
    }

    private void resetAvlSteps() {
        if (avlAnimation != null) {
            avlAnimation.stop();
        }
        currentAvlSteps.clear();
        currentAvlStepIndex = 0;
        updateAvlStepNavigation();
    }

    private void updateAvlStepNavigation() {
        if (currentAvlSteps == null || currentAvlSteps.isEmpty()) {
            avlStepInfoLabel.setText("步骤: 0/0");
            prevAvlStepBtn.setDisable(true);
            nextAvlStepBtn.setDisable(true);
            avlAutoDemoBtn.setDisable(true);
            resetAvlBtn.setDisable(true);
        } else {
            avlStepInfoLabel.setText("步骤: " + (currentAvlStepIndex + 1) + "/" + currentAvlSteps.size());
            prevAvlStepBtn.setDisable(currentAvlStepIndex == 0);
            nextAvlStepBtn.setDisable(currentAvlStepIndex == currentAvlSteps.size() - 1);
            avlAutoDemoBtn.setDisable(false);
            resetAvlBtn.setDisable(false);
        }
    }

    // ========== 树形结构存档管理方法 ==========

    // 刷新保存文件列表
    private void refreshTreeSavedFiles() {
        List<String> savedFiles = TreeArchiveManager.getSavedTreeFiles();
        savedTreeFilesCombo.getItems().setAll(savedFiles);
        if (!savedFiles.isEmpty()) {
            savedTreeFilesCombo.setValue(savedFiles.get(0));
        }
    }

    // 删除保存文件
    private void deleteTreeSaveFile() {
        String filename = savedTreeFilesCombo.getValue();
        if (filename != null && !filename.isEmpty()) {
            if (TreeArchiveManager.deleteTreeSaveFile(filename)) {
                refreshTreeSavedFiles();
                showTreeAlert("成功", "文件删除成功: " + filename);
            } else {
                showTreeAlert("错误", "文件删除失败: " + filename);
            }
        }
    }

    // 保存二叉树
    private void saveBinaryTree() {
        if (binaryTree == null || binaryTree.isEmpty()) {
            showTreeAlert("错误", "请先创建二叉树");
            return;
        }

        String filename = treeArchiveNameField.getText();
        String description = treeArchiveDescriptionField.getText();

        if (filename.isEmpty()) {
            showTreeAlert("错误", "请输入存档名称");
            return;
        }

        TreeArchiveManager.TreeArchiveData archiveData = binaryTree.saveToArchive(description);
        if (TreeArchiveManager.saveTreeStructure(archiveData, filename)) {
            showTreeAlert("成功", "二叉树保存成功: " + filename);
            refreshTreeSavedFiles();
            treeArchiveNameField.clear();
            treeArchiveDescriptionField.clear();
        } else {
            showTreeAlert("错误", "二叉树保存失败");
        }
    }

    // 加载二叉树
    private void loadBinaryTree() {
        String filename = savedTreeFilesCombo.getValue();
        if (filename == null || filename.isEmpty()) {
            showTreeAlert("错误", "请选择要加载的文件");
            return;
        }

        TreeArchiveManager.TreeArchiveData archiveData = TreeArchiveManager.loadTreeStructure(filename);
        if (archiveData != null && "binary".equals(archiveData.treeType)) {
            binaryTree = BinaryTree.loadFromArchive(archiveData);
            binaryTreeVisualizer.drawBinaryTree(binaryTree.getRoot(), "binary");
            binaryTreeOutput.setText("二叉树加载成功: " + archiveData.description);
            updateTreeInfo();
        } else {
            showTreeAlert("错误", "文件格式不正确或不是二叉树存档");
        }
    }

    // 保存BST
    private void saveBST() {
        if (bst == null || bst.isEmpty()) {
            showTreeAlert("错误", "请先创建BST");
            return;
        }

        String filename = treeArchiveNameField.getText();
        String description = treeArchiveDescriptionField.getText();

        if (filename.isEmpty()) {
            showTreeAlert("错误", "请输入存档名称");
            return;
        }

        TreeArchiveManager.TreeArchiveData archiveData = bst.saveToArchive(description);
        if (TreeArchiveManager.saveTreeStructure(archiveData, filename)) {
            showTreeAlert("成功", "BST保存成功: " + filename);
            refreshTreeSavedFiles();
            treeArchiveNameField.clear();
            treeArchiveDescriptionField.clear();
        } else {
            showTreeAlert("错误", "BST保存失败");
        }
    }

    // 加载BST
    private void loadBST() {
        String filename = savedTreeFilesCombo.getValue();
        if (filename == null || filename.isEmpty()) {
            showTreeAlert("错误", "请选择要加载的文件");
            return;
        }

        TreeArchiveManager.TreeArchiveData archiveData = TreeArchiveManager.loadTreeStructure(filename);
        if (archiveData != null && "bst".equals(archiveData.treeType)) {
            bst = BST.loadFromArchive(archiveData);
            bstVisualizer.drawBinaryTree(bst.getRoot(), "bst");
            bstOutput.setText("BST加载成功: " + archiveData.description);
            updateTreeInfo();
        } else {
            showTreeAlert("错误", "文件格式不正确或不是BST存档");
        }
    }

    // 保存哈夫曼树
    private void saveHuffmanTree() {
        if (huffmanTree == null || huffmanTree.getRoot() == null) {
            showTreeAlert("错误", "请先构建哈夫曼树");
            return;
        }

        String filename = treeArchiveNameField.getText();
        String description = treeArchiveDescriptionField.getText();

        if (filename.isEmpty()) {
            showTreeAlert("错误", "请输入存档名称");
            return;
        }

        TreeArchiveManager.TreeArchiveData archiveData = huffmanTree.saveToArchive(description);
        if (TreeArchiveManager.saveTreeStructure(archiveData, filename)) {
            showTreeAlert("成功", "哈夫曼树保存成功: " + filename);
            refreshTreeSavedFiles();
            treeArchiveNameField.clear();
            treeArchiveDescriptionField.clear();
        } else {
            showTreeAlert("错误", "哈夫曼树保存失败");
        }
    }

    // 加载哈夫曼树
    private void loadHuffmanTree() {
        String filename = savedTreeFilesCombo.getValue();
        if (filename == null || filename.isEmpty()) {
            showTreeAlert("错误", "请选择要加载的文件");
            return;
        }

        TreeArchiveManager.TreeArchiveData archiveData = TreeArchiveManager.loadTreeStructure(filename);
        if (archiveData != null && "huffman".equals(archiveData.treeType)) {
            huffmanTree = HuffmanTree.loadFromArchive(archiveData);
            huffmanVisualizer.drawHuffmanTree(huffmanTree.getRoot());
            huffmanOutput.setText("哈夫曼树加载成功: " + archiveData.description);
        } else {
            showTreeAlert("错误", "文件格式不正确或不是哈夫曼树存档");
        }
    }

    // 保存AVL树
    private void saveAVLTree() {
        if (avlTree == null || avlTree.isEmpty()) {
            showTreeAlert("错误", "请先创建AVL树");
            return;
        }

        String filename = treeArchiveNameField.getText();
        String description = treeArchiveDescriptionField.getText();

        if (filename.isEmpty()) {
            showTreeAlert("错误", "请输入存档名称");
            return;
        }

        TreeArchiveManager.TreeArchiveData archiveData = avlTree.saveToArchive(description);
        if (TreeArchiveManager.saveTreeStructure(archiveData, filename)) {
            showTreeAlert("成功", "AVL树保存成功: " + filename);
            refreshTreeSavedFiles();
            treeArchiveNameField.clear();
            treeArchiveDescriptionField.clear();
        } else {
            showTreeAlert("错误", "AVL树保存失败");
        }
    }

    // 加载AVL树
    private void loadAVLTree() {
        String filename = savedTreeFilesCombo.getValue();
        if (filename == null || filename.isEmpty()) {
            showTreeAlert("错误", "请选择要加载的文件");
            return;
        }

        TreeArchiveManager.TreeArchiveData archiveData = TreeArchiveManager.loadTreeStructure(filename);
        if (archiveData != null && "avl".equals(archiveData.treeType)) {
            avlTree = AVLTree.loadFromArchive(archiveData);
            avlVisualizer.drawAVLTree(avlTree.getRoot());
            avlOutput.setText("AVL树加载成功: " + archiveData.description);
            updateTreeInfo();
        } else {
            showTreeAlert("错误", "文件格式不正确或不是AVL树存档");
        }
    }

    // 辅助方法：显示树形结构提示框
    private void showTreeAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

        if (avlTree != null) {
            avlInfoLabel.setText(String.format("AVL树: 大小=%d, 高度=%d",
                    avlTree.size(), avlTree.height()));
        }
    }
}