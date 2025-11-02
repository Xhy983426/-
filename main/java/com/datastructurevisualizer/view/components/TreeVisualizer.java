package com.datastructurevisualizer.view.components;

import com.datastructurevisualizer.model.BST;
import com.datastructurevisualizer.model.HuffmanTree;
import com.datastructurevisualizer.model.TreeNode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.List;

public class TreeVisualizer {
    private Pane canvas;
    private static final double VERTICAL_SPACING = 80;
    private static final double NODE_RADIUS = 18;

    public TreeVisualizer(Pane canvas) {
        this.canvas = canvas;
    }

    public void drawBinaryTree(TreeNode root, String treeType) {
        canvas.getChildren().clear();
        if (root != null) {
            drawTreeRecursive(root, 400, 50, 200, treeType, false, -1);
        }
    }

    // 增强的绘制方法，支持高亮特定节点
    public void drawBinaryTreeWithHighlight(TreeNode root, String treeType, int highlightValue) {
        canvas.getChildren().clear();
        if (root != null) {
            drawTreeRecursive(root, 400, 50, 200, treeType, true, highlightValue);
        }
    }

    // 逐步演示搜索过程
    public void drawSearchStep(TreeNode root, BST.SearchStep step, int stepIndex, int totalSteps) {
        canvas.getChildren().clear();
        if (root != null) {
            drawTreeRecursiveWithStep(root, 400, 50, 200, "bst", step, stepIndex, totalSteps);
        }

        // 添加步骤信息
        if (step != null) {
            addStepInfo(step, stepIndex, totalSteps);
        }
    }

    private void drawTreeRecursive(TreeNode node, double x, double y, double hGap,
                                   String treeType, boolean highlight, int highlightValue) {
        if (node == null) return;

        // 判断是否高亮当前节点
        boolean isHighlighted = highlight && node.getValue() == highlightValue;

        Circle circle = new Circle(x, y, NODE_RADIUS);
        circle.setFill(isHighlighted ? Color.GOLD : getNodeColor(treeType));
        circle.setStroke(Color.DARKBLUE);
        circle.setStrokeWidth(2);

        Text valueText = new Text(String.valueOf(node.getValue()));
        valueText.setX(x - valueText.getLayoutBounds().getWidth() / 2);
        valueText.setY(y + valueText.getLayoutBounds().getHeight() / 4);

        if (isHighlighted) {
            valueText.setStyle("-fx-font-weight: bold; -fx-fill: #c0392b;");
        }

        canvas.getChildren().addAll(circle, valueText);

        if (node.getLeft() != null) {
            double childX = x - hGap;
            double childY = y + VERTICAL_SPACING;

            Line leftLine = new Line(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
            leftLine.setStroke(Color.BLACK);
            leftLine.setStrokeWidth(2);
            canvas.getChildren().add(leftLine);

            drawTreeRecursive(node.getLeft(), childX, childY, hGap / 1.5,
                    treeType, highlight, highlightValue);
        }

        if (node.getRight() != null) {
            double childX = x + hGap;
            double childY = y + VERTICAL_SPACING;

            Line rightLine = new Line(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
            rightLine.setStroke(Color.BLACK);
            rightLine.setStrokeWidth(2);
            canvas.getChildren().add(rightLine);

            drawTreeRecursive(node.getRight(), childX, childY, hGap / 1.5,
                    treeType, highlight, highlightValue);
        }
    }

    private void drawTreeRecursiveWithStep(TreeNode node, double x, double y, double hGap,
                                           String treeType, BST.SearchStep step,
                                           int stepIndex, int totalSteps) {
        if (node == null) return;

        // 判断是否高亮当前节点（搜索路径上的节点）
        boolean isCurrentStep = step != null && step.currentNode != null &&
                step.currentNode.getValue() == node.getValue();
        boolean isHighlighted = isCurrentStep;

        Circle circle = new Circle(x, y, NODE_RADIUS);

        if (isHighlighted) {
            circle.setFill(Color.GOLD);
            circle.setStroke(Color.RED);
            circle.setStrokeWidth(3);
        } else {
            circle.setFill(getNodeColor(treeType));
            circle.setStroke(Color.DARKBLUE);
            circle.setStrokeWidth(2);
        }

        Text valueText = new Text(String.valueOf(node.getValue()));
        valueText.setX(x - valueText.getLayoutBounds().getWidth() / 2);
        valueText.setY(y + valueText.getLayoutBounds().getHeight() / 4);

        if (isHighlighted) {
            valueText.setStyle("-fx-font-weight: bold; -fx-fill: #c0392b;");
        }

        canvas.getChildren().addAll(circle, valueText);

        // 绘制连接线
        if (node.getLeft() != null) {
            double childX = x - hGap;
            double childY = y + VERTICAL_SPACING;

            Line leftLine = new Line(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
            leftLine.setStroke(Color.BLACK);
            leftLine.setStrokeWidth(2);
            canvas.getChildren().add(leftLine);

            drawTreeRecursiveWithStep(node.getLeft(), childX, childY, hGap / 1.5,
                    treeType, step, stepIndex, totalSteps);
        }

        if (node.getRight() != null) {
            double childX = x + hGap;
            double childY = y + VERTICAL_SPACING;

            Line rightLine = new Line(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
            rightLine.setStroke(Color.BLACK);
            rightLine.setStrokeWidth(2);
            canvas.getChildren().add(rightLine);

            drawTreeRecursiveWithStep(node.getRight(), childX, childY, hGap / 1.5,
                    treeType, step, stepIndex, totalSteps);
        }
    }

    private void addStepInfo(BST.SearchStep step, int stepIndex, int totalSteps) {
        // 添加步骤信息面板
        Rectangle infoPanel = new Rectangle(350, 60);
        infoPanel.setFill(Color.rgb(255, 255, 255, 0.95));
        infoPanel.setStroke(Color.DARKGRAY);
        infoPanel.setStrokeWidth(1);
        infoPanel.setLayoutX(10);
        infoPanel.setLayoutY(10);
        infoPanel.setArcWidth(10);
        infoPanel.setArcHeight(10);

        Text stepText = new Text("步骤 " + (stepIndex + 1) + "/" + totalSteps);
        stepText.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        stepText.setX(20);
        stepText.setY(30);

        Text descText = new Text(step.description);
        descText.setStyle("-fx-font-size: 12;");
        descText.setX(20);
        descText.setY(50);

        // 根据搜索结果设置颜色
        if (step.found) {
            descText.setFill(Color.GREEN);
            stepText.setFill(Color.GREEN);
        } else if (step.currentNode == null) {
            descText.setFill(Color.RED);
            stepText.setFill(Color.RED);
        }

        // 添加导航提示
        Text navText = new Text("使用 ← → 箭头键导航步骤");
        navText.setStyle("-fx-font-size: 10; -fx-fill: #7f8c8d;");
        navText.setX(20);
        navText.setY(70);

        canvas.getChildren().addAll(infoPanel, stepText, descText, navText);
    }

    public void drawHuffmanTree(HuffmanTree.HuffmanNode root) {
        canvas.getChildren().clear();
        if (root != null) {
            drawHuffmanTreeRecursive(root, 400, 50, 300);
        }
    }

    private void drawHuffmanTreeRecursive(HuffmanTree.HuffmanNode node, double x, double y, double hGap) {
        if (node == null) return;

        Circle circle = new Circle(x, y, NODE_RADIUS);
        circle.setFill(Color.LIGHTGREEN);
        circle.setStroke(Color.DARKGREEN);
        circle.setStrokeWidth(2);

        String nodeText = node.isLeaf() ?
                "'" + node.character + "':" + node.frequency :
                String.valueOf(node.frequency);
        Text text = new Text(nodeText);
        text.setX(x - text.getLayoutBounds().getWidth() / 2);
        text.setY(y + text.getLayoutBounds().getHeight() / 4);

        canvas.getChildren().addAll(circle, text);

        if (node.left != null) {
            double childX = x - hGap;
            double childY = y + VERTICAL_SPACING;

            Line leftLine = new Line(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
            leftLine.setStroke(Color.BLACK);
            leftLine.setStrokeWidth(2);
            canvas.getChildren().add(leftLine);

            Text zeroText = new Text("0");
            zeroText.setStyle("-fx-font-weight: bold;");
            zeroText.setX((x + childX) / 2 - 5);
            zeroText.setY((y + childY) / 2);
            canvas.getChildren().add(zeroText);

            drawHuffmanTreeRecursive(node.left, childX, childY, hGap / 1.5);
        }

        if (node.right != null) {
            double childX = x + hGap;
            double childY = y + VERTICAL_SPACING;

            Line rightLine = new Line(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
            rightLine.setStroke(Color.BLACK);
            rightLine.setStrokeWidth(2);
            canvas.getChildren().add(rightLine);

            Text oneText = new Text("1");
            oneText.setStyle("-fx-font-weight: bold;");
            oneText.setX((x + childX) / 2 - 5);
            oneText.setY((y + childY) / 2);
            canvas.getChildren().add(oneText);

            drawHuffmanTreeRecursive(node.right, childX, childY, hGap / 1.5);
        }
    }

    // 保持向后兼容的方法
    public void highlightNode(TreeNode root, int value) {
        drawBinaryTreeWithHighlight(root, "bst", value);
    }

    private Color getNodeColor(String treeType) {
        switch (treeType) {
            case "binary": return Color.LIGHTBLUE;
            case "bst": return Color.LIGHTCORAL;
            case "huffman": return Color.LIGHTGREEN;
            default: return Color.LIGHTGRAY;
        }
    }

    // 清空画布
    public void clear() {
        canvas.getChildren().clear();
    }
}