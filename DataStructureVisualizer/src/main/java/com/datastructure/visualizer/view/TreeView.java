package com.datastructure.visualizer.view;

import com.datastructure.visualizer.view.components.TreeNodeView;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.geometry.Insets;
import javafx.scene.text.Text;

public class TreeView {
    private VBox root;
    private ComboBox<String> treeType;
    private TextField inputField;
    private Button insertBtn, deleteBtn, searchBtn, clearBtn, buildHuffmanBtn;
    private Pane visualizationPane;
    private Label infoLabel;
    private TextArea operationLog;

    public TreeView() {
        initialize();
    }

    private void initialize() {
        root = new VBox(15);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // 标题
        Label titleLabel = new Label("树形结构可视化");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // 控制面板
        HBox controlPanel = createControlPanel();

        // 信息标签
        infoLabel = new Label("选择树类型并开始操作");
        infoLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #7f8c8d;");

        // 可视化区域
        visualizationPane = new Pane();
        visualizationPane.setPrefSize(800, 400);
        visualizationPane.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-width: 2; -fx-border-radius: 5;");

        // 操作日志
        operationLog = new TextArea();
        operationLog.setPrefHeight(150);
        operationLog.setEditable(false);
        operationLog.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 12;");

        VBox logContainer = new VBox(5);
        logContainer.getChildren().addAll(new Label("操作日志:"), operationLog);

        root.getChildren().addAll(titleLabel, controlPanel, infoLabel, visualizationPane, logContainer);
    }

    private HBox createControlPanel() {
        HBox controlPanel = new HBox(10);
        controlPanel.setPadding(new Insets(10));
        controlPanel.setStyle("-fx-background-color: #ecf0f1; -fx-border-radius: 5;");

        treeType = new ComboBox<>();
        treeType.getItems().addAll("二叉树", "二叉搜索树", "哈夫曼树");
        treeType.setValue("二叉树");
        treeType.setPrefWidth(120);

        inputField = new TextField();
        inputField.setPromptText("输入数值...");
        inputField.setPrefWidth(150);

        insertBtn = createStyledButton("插入", "#27ae60");
        deleteBtn = createStyledButton("删除", "#e74c3c");
        searchBtn = createStyledButton("查找", "#3498db");
        clearBtn = createStyledButton("清空", "#95a5a6");
        buildHuffmanBtn = createStyledButton("构建哈夫曼", "#9b59b6");

        controlPanel.getChildren().addAll(
                new Label("树类型:"), treeType,
                new Label("数值:"), inputField,
                insertBtn, deleteBtn, searchBtn, clearBtn, buildHuffmanBtn
        );

        return controlPanel;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(" + color + ", -20%); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        return button;
    }

    public void updateVisualization(TreeNodeView rootNode) {
        visualizationPane.getChildren().clear();
        if (rootNode != null) {
            layoutTree(rootNode, 400, 50, 300);
            addNodeToPane(rootNode);
        }
    }

    private void layoutTree(TreeNodeView node, double x, double y, double hGap) {
        if (node == null) return;

        node.setLayoutX(x - 20);
        node.setLayoutY(y);

        if (node.getLeft() != null) {
            double childX = x - hGap;
            double childY = y + 80;
            layoutTree(node.getLeft(), childX, childY, hGap / 1.8);

            // 绘制连接线
            Line line = new Line(x, y + 20, childX, childY - 20);
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);
            visualizationPane.getChildren().add(line);
        }

        if (node.getRight() != null) {
            double childX = x + hGap;
            double childY = y + 80;
            layoutTree(node.getRight(), childX, childY, hGap / 1.8);

            // 绘制连接线
            Line line = new Line(x, y + 20, childX, childY - 20);
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);
            visualizationPane.getChildren().add(line);
        }
    }

    private void addNodeToPane(TreeNodeView node) {
        if (node == null) return;
        visualizationPane.getChildren().add(node);
        addNodeToPane(node.getLeft());
        addNodeToPane(node.getRight());
    }

    public void setInfoText(String text) {
        infoLabel.setText(text);
    }

    public void logOperation(String operation) {
        operationLog.appendText(operation + "\n");
    }

    public void clearLog() {
        operationLog.clear();
    }

    // Getters
    public VBox getView() { return root; }
    public ComboBox<String> getTreeType() { return treeType; }
    public TextField getInputField() { return inputField; }
    public Button getInsertBtn() { return insertBtn; }
    public Button getDeleteBtn() { return deleteBtn; }
    public Button getSearchBtn() { return searchBtn; }
    public Button getClearBtn() { return clearBtn; }
    public Button getBuildHuffmanBtn() { return buildHuffmanBtn; }
    public Pane getVisualizationPane() { return visualizationPane; }
}