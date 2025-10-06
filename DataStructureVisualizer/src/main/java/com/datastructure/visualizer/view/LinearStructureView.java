package com.datastructure.visualizer.view;

import com.datastructure.visualizer.view.components.NodeView;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import java.util.List;

public class LinearStructureView {
    private VBox root;
    private ComboBox<String> structureType;
    private TextField inputField;
    private Button insertBtn, deleteBtn, searchBtn, clearBtn;
    private Button pushBtn, popBtn; // 栈专用按钮
    private Pane visualizationPane;
    private Label infoLabel;
    private TextArea operationLog;
    private HBox stackButtons; // 栈按钮容器 - 改为HBox

    public LinearStructureView() {
        initialize();
    }

    private void initialize() {
        root = new VBox(15);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // 标题
        Label titleLabel = new Label("线性结构可视化");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // 控制面板
        HBox controlPanel = createControlPanel();

        // 栈专用按钮容器（初始隐藏）
        stackButtons = createStackButtons();
        stackButtons.setVisible(false);

        // 信息标签
        infoLabel = new Label("选择数据结构类型并开始操作");
        infoLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #7f8c8d;");

        // 可视化区域
        visualizationPane = new Pane();
        visualizationPane.setPrefSize(800, 300);
        visualizationPane.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-width: 2; -fx-border-radius: 5;");

        // 操作日志
        operationLog = new TextArea();
        operationLog.setPrefHeight(150);
        operationLog.setEditable(false);
        operationLog.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 12;");

        VBox logContainer = new VBox(5);
        logContainer.getChildren().addAll(new Label("操作日志:"), operationLog);

        root.getChildren().addAll(titleLabel, controlPanel, stackButtons, infoLabel, visualizationPane, logContainer);
    }

    private HBox createControlPanel() {
        HBox controlPanel = new HBox(10);
        controlPanel.setPadding(new Insets(10));
        controlPanel.setStyle("-fx-background-color: #ecf0f1; -fx-border-radius: 5;");

        structureType = new ComboBox<>();
        structureType.getItems().addAll("链表", "顺序表", "栈");
        structureType.setValue("链表");
        structureType.setPrefWidth(100);

        inputField = new TextField();
        inputField.setPromptText("输入数值...");
        inputField.setPrefWidth(150);

        insertBtn = createStyledButton("插入", "#27ae60");
        deleteBtn = createStyledButton("删除", "#e74c3c");
        searchBtn = createStyledButton("查找", "#3498db");
        clearBtn = createStyledButton("清空", "#95a5a6");

        controlPanel.getChildren().addAll(
                new Label("结构类型:"), structureType,
                new Label("数值:"), inputField,
                insertBtn, deleteBtn, searchBtn, clearBtn
        );

        return controlPanel;
    }

    private HBox createStackButtons() { // 返回类型改为HBox
        HBox stackButtonPanel = new HBox(10); // 改为HBox
        stackButtonPanel.setPadding(new Insets(10));
        stackButtonPanel.setStyle("-fx-background-color: #d5dbdb; -fx-border-radius: 5;");

        pushBtn = createStyledButton("入栈(Push)", "#27ae60");
        popBtn = createStyledButton("出栈(Pop)", "#e74c3c");

        Label stackLabel = new Label("栈操作:");
        stackLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        stackButtonPanel.getChildren().addAll(stackLabel, pushBtn, popBtn);
        return stackButtonPanel;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(" + color + ", -20%); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        return button;
    }

    // 切换按钮显示状态
    public void toggleStackButtons(boolean showStackButtons) {
        if (showStackButtons) {
            // 显示栈按钮，隐藏普通按钮
            stackButtons.setVisible(true);
            insertBtn.setVisible(false);
            deleteBtn.setVisible(false);
            searchBtn.setVisible(false);
        } else {
            // 隐藏栈按钮，显示普通按钮
            stackButtons.setVisible(false);
            insertBtn.setVisible(true);
            deleteBtn.setVisible(true);
            searchBtn.setVisible(true);
        }
    }

    public void updateVisualization(List<NodeView> nodes, String structureType) {
        visualizationPane.getChildren().clear();

        double startX = 50;
        double y = 150;

        switch (structureType) {
            case "链表":
                drawLinkedList(nodes, startX, y);
                break;
            case "顺序表":
                drawArrayList(nodes, startX, y);
                break;
            case "栈":
                drawStack(nodes, startX, y);
                break;
        }
    }

    private void drawLinkedList(List<NodeView> nodes, double startX, double y) {
        double currentX = startX;

        for (int i = 0; i < nodes.size(); i++) {
            NodeView node = nodes.get(i);
            node.setLayoutX(currentX);
            node.setLayoutY(y);
            visualizationPane.getChildren().add(node);

            // 绘制箭头
            if (i < nodes.size() - 1) {
                Line arrow = new Line(currentX + 25, y, currentX + 55, y);
                arrow.setStroke(Color.BLACK);
                arrow.setStrokeWidth(2);

                Line arrowHead1 = new Line(currentX + 55, y, currentX + 50, y - 5);
                Line arrowHead2 = new Line(currentX + 55, y, currentX + 50, y + 5);
                arrowHead1.setStroke(Color.BLACK);
                arrowHead2.setStroke(Color.BLACK);
                arrowHead1.setStrokeWidth(2);
                arrowHead2.setStrokeWidth(2);

                visualizationPane.getChildren().addAll(arrow, arrowHead1, arrowHead2);
            }

            currentX += 80;
        }

        // 绘制null节点
        if (!nodes.isEmpty()) {
            Text nullText = new Text(currentX, y + 5, "null");
            nullText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
            visualizationPane.getChildren().add(nullText);
        }
    }

    private void drawArrayList(List<NodeView> nodes, double startX, double y) {
        double currentX = startX;

        for (int i = 0; i < nodes.size(); i++) {
            NodeView node = nodes.get(i);
            node.setLayoutX(currentX);
            node.setLayoutY(y);
            visualizationPane.getChildren().add(node);

            // 绘制索引
            Text indexText = new Text(currentX + 20, y + 40, String.valueOf(i));
            indexText.setStyle("-fx-font-size: 12; -fx-fill: #7f8c8d;");
            visualizationPane.getChildren().add(indexText);

            currentX += 60;
        }
    }

    private void drawStack(List<NodeView> nodes, double startX, double y) {
        double currentY = y;

        for (int i = nodes.size() - 1; i >= 0; i--) {
            NodeView node = nodes.get(i);
            node.setLayoutX(startX);
            node.setLayoutY(currentY);
            visualizationPane.getChildren().add(node);

            // 如果是栈顶元素，添加标记
            if (i == nodes.size() - 1) {
                Text topText = new Text(startX + 30, currentY - 10, "TOP");
                topText.setStyle("-fx-font-size: 12; -fx-fill: #e74c3c; -fx-font-weight: bold;");
                visualizationPane.getChildren().add(topText);
            }

            currentY -= 60;
        }

        // 绘制栈底
        if (!nodes.isEmpty()) {
            Text bottomText = new Text(startX + 30, y + 40, "BOTTOM");
            bottomText.setStyle("-fx-font-size: 12; -fx-fill: #7f8c8d; -fx-font-weight: bold;");
            visualizationPane.getChildren().add(bottomText);
        }
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
    public ComboBox<String> getStructureType() { return structureType; }
    public TextField getInputField() { return inputField; }
    public Button getInsertBtn() { return insertBtn; }
    public Button getDeleteBtn() { return deleteBtn; }
    public Button getSearchBtn() { return searchBtn; }
    public Button getClearBtn() { return clearBtn; }
    public Button getPushBtn() { return pushBtn; }
    public Button getPopBtn() { return popBtn; }
    public Pane getVisualizationPane() { return visualizationPane; }
}