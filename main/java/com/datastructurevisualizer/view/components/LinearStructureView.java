package com.datastructurevisualizer.view.components;

import com.datastructurevisualizer.model.ArrayList;
import com.datastructurevisualizer.model.LinkedList;
import com.datastructurevisualizer.model.Stack;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LinearStructureView {
    private Pane canvas;
    private static final double ELEMENT_WIDTH = 50;
    private static final double ELEMENT_HEIGHT = 40;
    private static final double SPACING = 5;
    private static final double START_X = 50;
    private static final double START_Y = 200;

    public LinearStructureView(Pane canvas) {
        this.canvas = canvas;
    }

    // 新的方法：绘制顺序表步骤演示
    public void drawArrayListWithSteps(ArrayList arrayList, ArrayList.OperationStep step) {
        canvas.getChildren().clear();

        if (arrayList == null || step == null) return;

        int[] arrayState = step.arrayState;

        // 如果数组状态为空，显示空数组提示
        if (arrayState.length == 0) {
            drawEmptyArray();
            addStepDescription(step);
            return;
        }

        for (int i = 0; i < arrayState.length; i++) {
            double elementX = START_X + i * (ELEMENT_WIDTH + SPACING);

            Rectangle element = new Rectangle(elementX, START_Y, ELEMENT_WIDTH, ELEMENT_HEIGHT);

            // 设置元素颜色基于状态
            if (i == step.highlightedIndex) {
                element.setFill(getStepColor(step.type));
            } else if (isInMovingIndexes(i, step.movingIndexes)) {
                element.setFill(Color.LIGHTYELLOW);
            } else {
                element.setFill(Color.LIGHTCORAL);
            }

            element.setStroke(Color.DARKRED);
            element.setStrokeWidth(2);

            // 索引文本
            Text indexText = new Text(String.valueOf(i));
            indexText.setStyle("-fx-font-size: 12; -fx-fill: #2c3e50;");
            indexText.setX(elementX + 5);
            indexText.setY(START_Y - 5);

            // 值文本
            Text valueText = new Text(String.valueOf(arrayState[i]));
            valueText.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            valueText.setX(elementX + ELEMENT_WIDTH / 2 - valueText.getLayoutBounds().getWidth() / 2);
            valueText.setY(START_Y + ELEMENT_HEIGHT / 2 + valueText.getLayoutBounds().getHeight() / 4);

            canvas.getChildren().addAll(element, indexText, valueText);

            // 如果是移动步骤，添加箭头
            if (step.type.equals("move") && step.movingIndexes.length == 2) {
                drawMovementArrow(step.movingIndexes[0], step.movingIndexes[1]);
            }
        }

        // 添加步骤描述
        addStepDescription(step);
    }

    // 原有的绘制方法（保持兼容性）
    public void drawArrayList(ArrayList arrayList) {
        canvas.getChildren().clear();

        if (arrayList == null || arrayList.isEmpty()) {
            drawEmptyArray();
            return;
        }

        for (int i = 0; i < arrayList.size(); i++) {
            double elementX = START_X + i * (ELEMENT_WIDTH + SPACING);

            Rectangle element = new Rectangle(elementX, START_Y, ELEMENT_WIDTH, ELEMENT_HEIGHT);
            element.setFill(Color.LIGHTCORAL);
            element.setStroke(Color.DARKRED);
            element.setStrokeWidth(2);

            // 索引文本
            Text indexText = new Text(String.valueOf(i));
            indexText.setStyle("-fx-font-size: 12; -fx-fill: #2c3e50;");
            indexText.setX(elementX + 5);
            indexText.setY(START_Y - 5);

            // 值文本
            Text valueText = new Text(String.valueOf(arrayList.get(i)));
            valueText.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            valueText.setX(elementX + ELEMENT_WIDTH / 2 - valueText.getLayoutBounds().getWidth() / 2);
            valueText.setY(START_Y + ELEMENT_HEIGHT / 2 + valueText.getLayoutBounds().getHeight() / 4);

            canvas.getChildren().addAll(element, indexText, valueText);
        }
    }

    // 绘制空数组提示
    private void drawEmptyArray() {
        Text emptyText = new Text("空数组");
        emptyText.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-fill: #7f8c8d;");
        emptyText.setX(START_X);
        emptyText.setY(START_Y + ELEMENT_HEIGHT / 2);
        canvas.getChildren().add(emptyText);
    }

    // 绘制链表
    public void drawLinkedList(LinkedList list) {
        canvas.getChildren().clear();

        if (list == null || list.isEmpty()) {
            drawEmptyLinkedList();
            return;
        }

        LinkedList.Node current = list.getHead();
        double startX = 50;
        double startY = 100;
        int position = 0;
        double NODE_SPACING = 80;

        while (current != null) {
            double centerX = startX + position * NODE_SPACING;
            double centerY = startY;

            // 创建节点
            Rectangle nodeRect = new Rectangle(centerX - 20, centerY - 20, 40, 40);
            nodeRect.setFill(Color.LIGHTBLUE);
            nodeRect.setStroke(Color.BLUE);
            nodeRect.setStrokeWidth(2);

            // 节点值
            Text valueText = new Text(String.valueOf(current.data));
            valueText.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            valueText.setX(centerX - valueText.getLayoutBounds().getWidth() / 2);
            valueText.setY(centerY + valueText.getLayoutBounds().getHeight() / 4);

            // 位置索引
            Text indexText = new Text(String.valueOf(position));
            indexText.setStyle("-fx-font-size: 12; -fx-fill: #2c3e50;");
            indexText.setX(centerX - 20);
            indexText.setY(startY - 25);

            canvas.getChildren().addAll(nodeRect, valueText, indexText);

            // 绘制箭头（如果不是最后一个节点）
            if (current.next != null) {
                double arrowStartX = centerX + 20;
                double arrowEndX = centerX + NODE_SPACING - 20;

                Line arrow = new Line(arrowStartX, centerY, arrowEndX, centerY);
                arrow.setStroke(Color.BLACK);
                arrow.setStrokeWidth(2);

                // 箭头头部
                Line arrowHead1 = new Line(arrowEndX, centerY, arrowEndX - 10, centerY - 5);
                Line arrowHead2 = new Line(arrowEndX, centerY, arrowEndX - 10, centerY + 5);
                arrowHead1.setStroke(Color.BLACK);
                arrowHead2.setStroke(Color.BLACK);

                canvas.getChildren().addAll(arrow, arrowHead1, arrowHead2);
            }

            current = current.next;
            position++;
        }
    }

    // 绘制空链表提示
    private void drawEmptyLinkedList() {
        Text emptyText = new Text("空链表");
        emptyText.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-fill: #7f8c8d;");
        emptyText.setX(50);
        emptyText.setY(100);
        canvas.getChildren().add(emptyText);
    }

    // 绘制栈
    public void drawStack(Stack stack) {
        canvas.getChildren().clear();

        if (stack == null || stack.isEmpty()) {
            drawEmptyStack();
            return;
        }

        double startX = 100;
        double startY = 400;
        double elementWidth = 60;
        double elementHeight = 40;
        double spacing = 5;

        // 绘制栈底
        Line stackBase = new Line(startX - 20, startY, startX + elementWidth + 20, startY);
        stackBase.setStroke(Color.BLACK);
        stackBase.setStrokeWidth(3);
        canvas.getChildren().add(stackBase);

        // 绘制栈元素
        java.util.List<Integer> elements = stack.getElements();
        for (int i = 0; i < elements.size(); i++) {
            double elementY = startY - (i + 1) * (elementHeight + spacing);

            Rectangle element = new Rectangle(startX, elementY, elementWidth, elementHeight);
            element.setFill(Color.LIGHTGREEN);
            element.setStroke(Color.DARKGREEN);
            element.setStrokeWidth(2);

            Text valueText = new Text(String.valueOf(elements.get(i)));
            valueText.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            valueText.setX(startX + elementWidth / 2 - valueText.getLayoutBounds().getWidth() / 2);
            valueText.setY(elementY + elementHeight / 2 + valueText.getLayoutBounds().getHeight() / 4);

            // 位置索引
            Text indexText = new Text(String.valueOf(i));
            indexText.setStyle("-fx-font-size: 12; -fx-fill: #2c3e50;");
            indexText.setX(startX - 25);
            indexText.setY(elementY + elementHeight / 2);

            canvas.getChildren().addAll(element, valueText, indexText);
        }

        // 标记栈顶
        if (!elements.isEmpty()) {
            double topY = startY - elements.size() * (elementHeight + spacing);
            Text topText = new Text("↑ 栈顶");
            topText.setStyle("-fx-font-size: 12; -fx-fill: #e74c3c;");
            topText.setX(startX + elementWidth + 10);
            topText.setY(topY + elementHeight / 2);
            canvas.getChildren().add(topText);
        }

        // 显示栈信息
        Text infoText = new Text("栈大小: " + stack.size() + " / " + stack.getCapacity());
        infoText.setStyle("-fx-font-size: 12; -fx-fill: #2c3e50;");
        infoText.setX(startX);
        infoText.setY(startY + 30);
        canvas.getChildren().add(infoText);
    }

    // 绘制空栈提示
    private void drawEmptyStack() {
        double startX = 100;
        double startY = 400;

        // 绘制栈底
        Line stackBase = new Line(startX - 20, startY, startX + 60 + 20, startY);
        stackBase.setStroke(Color.BLACK);
        stackBase.setStrokeWidth(3);
        canvas.getChildren().add(stackBase);

        Text emptyText = new Text("空栈");
        emptyText.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-fill: #7f8c8d;");
        emptyText.setX(startX + 10);
        emptyText.setY(startY - 30);
        canvas.getChildren().add(emptyText);
    }

    // 绘制移动箭头
    private void drawMovementArrow(int fromIndex, int toIndex) {
        double fromX = START_X + fromIndex * (ELEMENT_WIDTH + SPACING) + ELEMENT_WIDTH / 2;
        double toX = START_X + toIndex * (ELEMENT_WIDTH + SPACING) + ELEMENT_WIDTH / 2;
        double y = START_Y - 20;

        Line arrowLine = new Line(fromX, y, toX, y);
        arrowLine.setStroke(Color.RED);
        arrowLine.setStrokeWidth(2);

        // 箭头头部
        double arrowSize = 8;
        if (fromIndex < toIndex) {
            // 向右箭头
            Line arrowHead1 = new Line(toX, y, toX - arrowSize, y - arrowSize);
            Line arrowHead2 = new Line(toX, y, toX - arrowSize, y + arrowSize);
            arrowHead1.setStroke(Color.RED);
            arrowHead2.setStroke(Color.RED);
            canvas.getChildren().addAll(arrowHead1, arrowHead2);
        } else {
            // 向左箭头
            Line arrowHead1 = new Line(toX, y, toX + arrowSize, y - arrowSize);
            Line arrowHead2 = new Line(toX, y, toX + arrowSize, y + arrowSize);
            arrowHead1.setStroke(Color.RED);
            arrowHead2.setStroke(Color.RED);
            canvas.getChildren().addAll(arrowHead1, arrowHead2);
        }

        canvas.getChildren().add(arrowLine);
    }

    // 添加步骤描述面板
    private void addStepDescription(ArrayList.OperationStep step) {
        Rectangle infoPanel = new Rectangle(400, 80);
        infoPanel.setFill(Color.rgb(255, 255, 255, 0.95));
        infoPanel.setStroke(Color.DARKGRAY);
        infoPanel.setStrokeWidth(1);
        infoPanel.setLayoutX(10);
        infoPanel.setLayoutY(10);
        infoPanel.setArcWidth(10);
        infoPanel.setArcHeight(10);

        Text stepTypeText = new Text("步骤类型: " + getStepTypeChinese(step.type));
        stepTypeText.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-fill: #2c3e50;");
        stepTypeText.setX(20);
        stepTypeText.setY(30);

        Text descText = new Text(step.description);
        descText.setStyle("-fx-font-size: 12; -fx-fill: #34495e;");
        descText.setX(20);
        descText.setY(50);

        // 添加颜色说明
        Text colorInfoText = new Text("颜色说明: 绿色-插入, 蓝色-移动, 黄色-准备, 橙色-标记, 灰色-检查");
        colorInfoText.setStyle("-fx-font-size: 10; -fx-fill: #7f8c8d;");
        colorInfoText.setX(20);
        colorInfoText.setY(70);

        canvas.getChildren().addAll(infoPanel, stepTypeText, descText, colorInfoText);
    }

    // 检查索引是否在移动索引数组中
    private boolean isInMovingIndexes(int index, int[] movingIndexes) {
        if (movingIndexes == null) return false;
        for (int movingIndex : movingIndexes) {
            if (index == movingIndex) return true;
        }
        return false;
    }

    // 根据步骤类型获取颜色
    private Color getStepColor(String stepType) {
        switch (stepType) {
            case "insert": return Color.LIGHTGREEN;
            case "delete": return Color.LIGHTCORAL;
            case "move": return Color.LIGHTBLUE;
            case "mark": return Color.ORANGE;
            case "check": return Color.LIGHTGRAY;
            case "prepare": return Color.LIGHTYELLOW;
            case "complete": return Color.LIGHTGREEN;
            default: return Color.LIGHTCORAL;
        }
    }

    // 获取步骤类型的中文描述
    private String getStepTypeChinese(String stepType) {
        switch (stepType) {
            case "insert": return "插入";
            case "delete": return "删除";
            case "move": return "移动";
            case "mark": return "标记";
            case "check": return "检查";
            case "prepare": return "准备";
            case "complete": return "完成";
            default: return stepType;
        }
    }


}