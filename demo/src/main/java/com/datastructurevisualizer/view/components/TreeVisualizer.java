package com.datastructurevisualizer.view.components;

import com.datastructurevisualizer.model.AVLTree;
import com.datastructurevisualizer.model.BST;
import com.datastructurevisualizer.model.BinaryTree;
import com.datastructurevisualizer.model.HuffmanTree;
import com.datastructurevisualizer.model.TreeNode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class TreeVisualizer {
    private Pane canvas;
    private static final double VERTICAL_SPACING = 80;
    private static final double NODE_RADIUS = 20;
    private static final double MIN_HORIZONTAL_GAP = 60; // 最小水平间距

    public TreeVisualizer(Pane canvas) {
        this.canvas = canvas;
    }

    // ========== 树布局信息类 ==========
    private class TreeLayoutInfo {
        int height;
        int maxWidth;
        double requiredWidth;
        double requiredHeight;
    }

    // ========== 通用工具方法 ==========

    /**
     * 检查点是否在画布范围内
     */
    private boolean isPointInCanvas(double x, double y) {
        if (canvas == null) return true;
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        double margin = NODE_RADIUS * 2;

        return x >= margin && x <= canvasWidth - margin &&
                y >= margin && y <= canvasHeight - margin;
    }

    /**
     * 计算二叉树高度
     */
    private int getTreeHeight(TreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(getTreeHeight(node.getLeft()), getTreeHeight(node.getRight()));
    }

    /**
     * 计算AVL树高度
     */
    private int getAVLTreeHeight(AVLTree.AVLNode node) {
        if (node == null) return 0;
        return 1 + Math.max(getAVLTreeHeight(node.left), getAVLTreeHeight(node.right));
    }

    /**
     * 计算哈夫曼树高度
     */
    private int getHuffmanTreeHeight(HuffmanTree.HuffmanNode node) {
        if (node == null) return 0;
        return 1 + Math.max(getHuffmanTreeHeight(node.left), getHuffmanTreeHeight(node.right));
    }

    /**
     * 计算树布局信息
     */
    private TreeLayoutInfo calculateTreeLayout(TreeNode root) {
        TreeLayoutInfo info = new TreeLayoutInfo();
        if (root == null) return info;

        int height = getTreeHeight(root);
        info.height = height;
        info.maxWidth = (int) Math.pow(2, height - 1);
        info.requiredWidth = info.maxWidth * (NODE_RADIUS * 2 + 20);
        info.requiredHeight = height * (VERTICAL_SPACING + NODE_RADIUS * 2);

        return info;
    }

    /**
     * 获取节点颜色
     */
    private Color getNodeColor(String treeType) {
        switch (treeType) {
            case "binary": return Color.LIGHTBLUE;
            case "bst": return Color.LIGHTCORAL;
            case "huffman": return Color.LIGHTGREEN;
            case "avl": return Color.LIGHTBLUE;
            default: return Color.LIGHTGRAY;
        }
    }

    /**
     * 绘制空树提示
     */
    private void drawEmptyTree(String treeType) {
        double canvasWidth = canvas != null ? canvas.getWidth() : 600;
        double canvasHeight = canvas != null ? canvas.getHeight() : 400;

        Text emptyText = new Text(getEmptyTreeText(treeType));
        emptyText.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-fill: #7f8c8d;");
        emptyText.setX(canvasWidth / 2 - emptyText.getLayoutBounds().getWidth() / 2);
        emptyText.setY(canvasHeight / 2);
        canvas.getChildren().add(emptyText);
    }

    private String getEmptyTreeText(String treeType) {
        switch (treeType) {
            case "binary": return "空二叉树";
            case "bst": return "空二叉搜索树";
            case "huffman": return "空哈夫曼树";
            case "avl": return "空AVL树";
            default: return "空树";
        }
    }
    // ========== 增强的树布局信息类 ==========
    private class EnhancedTreeLayoutInfo {
        double x;
        double y;
        double width;
        double height;
        double leftWidth;
        double rightWidth;

        public EnhancedTreeLayoutInfo(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.leftWidth = 0;
            this.rightWidth = 0;
        }
        // 计算哈夫曼树节点布局
        private EnhancedTreeLayoutInfo calculateHuffmanTreeLayout(HuffmanTree.HuffmanNode node, double x, double y, double level) {
            if (node == null) {
                return new EnhancedTreeLayoutInfo(x, y, 0, 0);
            }

            // 动态调整水平间距，避免重叠
            double baseHGap = Math.max(NODE_RADIUS * 5, 200 / (level + 1));

            // 递归计算左右子树布局
            EnhancedTreeLayoutInfo leftLayout = calculateHuffmanTreeLayout(node.left, x, y + VERTICAL_SPACING, level + 1);
            EnhancedTreeLayoutInfo rightLayout = calculateHuffmanTreeLayout(node.right, x, y + VERTICAL_SPACING, level + 1);

            // 计算当前节点的位置
            double currentX = x;
            double leftBound = leftLayout.x - leftLayout.width / 2;
            double rightBound = rightLayout.x + rightLayout.width / 2;

            if (leftLayout.width > 0 && rightLayout.width > 0) {
                currentX = (leftBound + rightBound) / 2;
            } else if (leftLayout.width > 0) {
                currentX = leftBound + baseHGap;
            } else if (rightLayout.width > 0) {
                currentX = rightBound - baseHGap;
            }

            EnhancedTreeLayoutInfo currentLayout = new EnhancedTreeLayoutInfo(currentX, y, 0, 0);
            currentLayout.leftWidth = Math.max(leftLayout.width, baseHGap);
            currentLayout.rightWidth = Math.max(rightLayout.width, baseHGap);
            currentLayout.width = currentLayout.leftWidth + currentLayout.rightWidth;

            return currentLayout;
        }
    }



    // 计算二叉树节点布局
    private EnhancedTreeLayoutInfo calculateBinaryTreeLayout(TreeNode node, double x, double y, double level) {
        if (node == null) {
            return new EnhancedTreeLayoutInfo(x, y, 0, 0);
        }

        // 基础水平间距，根据层级动态调整
        double baseHGap = Math.max(NODE_RADIUS * 4, 150 / (level + 1));

        // 递归计算左右子树布局
        EnhancedTreeLayoutInfo leftLayout = calculateBinaryTreeLayout(node.getLeft(), x, y + VERTICAL_SPACING, level + 1);
        EnhancedTreeLayoutInfo rightLayout = calculateBinaryTreeLayout(node.getRight(), x, y + VERTICAL_SPACING, level + 1);

        // 计算当前节点的位置
        double currentX = x;
        if (leftLayout.width > 0 || rightLayout.width > 0) {
            currentX = (leftLayout.x + leftLayout.width + rightLayout.x) / 2;
        }

        EnhancedTreeLayoutInfo currentLayout = new EnhancedTreeLayoutInfo(currentX, y, 0, 0);
        currentLayout.leftWidth = leftLayout.width + baseHGap;
        currentLayout.rightWidth = rightLayout.width + baseHGap;
        currentLayout.width = currentLayout.leftWidth + currentLayout.rightWidth;

        return currentLayout;
    }
    private void drawTreeWithPositions(TreeNode root, Map<TreeNode, Position> positions,
                                       String treeType, BinaryTree.TraversalStep step) {
        // 首先绘制所有连接线
        for (Map.Entry<TreeNode, Position> entry : positions.entrySet()) {
            TreeNode node = entry.getKey();
            Position pos = entry.getValue();

            // 绘制到左子节点的连接线
            if (node.getLeft() != null) {
                Position leftPos = positions.get(node.getLeft());
                if (leftPos != null && isPositionInCanvas(leftPos.x, leftPos.y)) {
                    Line leftLine = new Line(pos.x, pos.y + NODE_RADIUS,
                            leftPos.x, leftPos.y - NODE_RADIUS);
                    leftLine.setStroke(Color.BLACK);
                    leftLine.setStrokeWidth(2);
                    canvas.getChildren().add(leftLine);
                }
            }

            // 绘制到右子节点的连接线
            if (node.getRight() != null) {
                Position rightPos = positions.get(node.getRight());
                if (rightPos != null && isPositionInCanvas(rightPos.x, rightPos.y)) {
                    Line rightLine = new Line(pos.x, pos.y + NODE_RADIUS,
                            rightPos.x, rightPos.y - NODE_RADIUS);
                    rightLine.setStroke(Color.BLACK);
                    rightLine.setStrokeWidth(2);
                    canvas.getChildren().add(rightLine);
                }
            }
        }


        // 然后绘制所有节点
        for (Map.Entry<TreeNode, Position> entry : positions.entrySet()) {
            TreeNode node = entry.getKey();
            Position pos = entry.getValue();

            if (isPositionInCanvas(pos.x, pos.y)) {
                drawTreeNode(node, pos.x, pos.y, treeType, step);
            }
        }
    }
    private void drawTreeNode(TreeNode node, double x, double y, String treeType, BinaryTree.TraversalStep step) {
        // 创建节点圆圈
        Circle circle = new Circle(x, y, NODE_RADIUS);

        // 根据遍历状态设置颜色
        if (step != null && step.currentNode == node) {
            circle.setFill(Color.GOLD);
            circle.setStroke(Color.RED);
            circle.setStrokeWidth(3);
        } else if (step != null && node.isVisited()) {
            circle.setFill(Color.LIGHTGREEN);
            circle.setStroke(Color.DARKGREEN);
            circle.setStrokeWidth(2);
        } else {
            circle.setFill(getNodeColor(treeType));
            circle.setStroke(Color.DARKBLUE);
            circle.setStrokeWidth(2);
        }

        // 创建节点值文本
        Text valueText = new Text(String.valueOf(node.getValue()));
        valueText.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        // 计算文本居中位置
        double textWidth = valueText.getLayoutBounds().getWidth();
        double textHeight = valueText.getLayoutBounds().getHeight();
        valueText.setX(x - textWidth / 2);
        valueText.setY(y + textHeight / 4);

        // 添加到画布
        canvas.getChildren().addAll(circle, valueText);
    }





    // ========== 二叉树和BST可视化方法 ==========

    public void drawBinaryTree(TreeNode root, String treeType) {
        System.out.println("=== 开始绘制二叉树 ===");
        System.out.println("根节点: " + (root != null ? root.getValue() : "null"));

        if (canvas == null) {
            System.out.println("错误: canvas 为 null");
            return;
        }

        canvas.getChildren().clear();
        System.out.println("画布已清空");

        if (root == null) {
            System.out.println("树为空，绘制空树提示");
            drawEmptyTree(treeType);
            return;
        }

        // 计算树的高度和布局
        int treeHeight = getTreeHeight(root);
        System.out.println("树高度: " + treeHeight);

        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        System.out.println("画布尺寸: " + canvasWidth + " x " + canvasHeight);

        // 如果画布尺寸为0，设置默认值
        if (canvasWidth <= 0) canvasWidth = 600;
        if (canvasHeight <= 0) canvasHeight = 400;

        // 计算起始位置
        double startX = canvasWidth / 2;
        double startY = 80; // 固定起始Y位置

        System.out.println("起始位置: (" + startX + ", " + startY + ")");

        // 动态计算水平间距
        double baseHGap = Math.max(80, canvasWidth * 0.3 / treeHeight);
        double verticalSpacing = Math.min(80, (canvasHeight - startY - 50) / treeHeight);

        System.out.println("水平间距: " + baseHGap + ", 垂直间距: " + verticalSpacing);

        // 绘制树
        drawTreeRecursive(root, startX, startY, baseHGap, verticalSpacing, treeType);
        System.out.println("=== 绘制完成 ===");
    }
    private void drawTreeRecursive(TreeNode node, double x, double y, double hGap,
                                   double verticalSpacing, String treeType) {
        if (node == null) return;

        System.out.println("绘制节点: " + node.getValue() + " 位置: (" + x + ", " + y + ")");

        // 绘制当前节点
        Circle circle = new Circle(x, y, NODE_RADIUS);
        circle.setFill(getNodeColor(treeType));
        circle.setStroke(Color.DARKBLUE);
        circle.setStrokeWidth(2);

        Text valueText = new Text(String.valueOf(node.getValue()));
        valueText.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        // 计算文本位置（居中）
        double textWidth = valueText.getLayoutBounds().getWidth();
        double textHeight = valueText.getLayoutBounds().getHeight();
        valueText.setX(x - textWidth / 2);
        valueText.setY(y + textHeight / 4);

        canvas.getChildren().addAll(circle, valueText);
        System.out.println("节点 " + node.getValue() + " 已添加到画布");

        // 动态调整子节点间距
        double childHGap = Math.max(hGap * 0.6, NODE_RADIUS * 3);

        // 绘制左子树
        if (node.getLeft() != null) {
            double childX = x - childHGap;
            double childY = y + verticalSpacing;

            System.out.println("左子节点 " + node.getLeft().getValue() + " 位置: (" + childX + ", " + childY + ")");

            // 绘制连接线
            Line leftLine = new Line(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
            leftLine.setStroke(Color.BLACK);
            leftLine.setStrokeWidth(2);
            canvas.getChildren().add(leftLine);

            drawTreeRecursive(node.getLeft(), childX, childY, childHGap, verticalSpacing, treeType);
        }

        // 绘制右子树
        if (node.getRight() != null) {
            double childX = x + childHGap;
            double childY = y + verticalSpacing;

            System.out.println("右子节点 " + node.getRight().getValue() + " 位置: (" + childX + ", " + childY + ")");

            // 绘制连接线
            Line rightLine = new Line(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
            rightLine.setStroke(Color.BLACK);
            rightLine.setStrokeWidth(2);
            canvas.getChildren().add(rightLine);

            drawTreeRecursive(node.getRight(), childX, childY, childHGap, verticalSpacing, treeType);
        }
    }
    // 计算树的最大宽度（叶子节点数量）
    private int calculateTreeWidth(TreeNode node) {
        if (node == null) return 0;
        if (node.getLeft() == null && node.getRight() == null) return 1;
        return calculateTreeWidth(node.getLeft()) + calculateTreeWidth(node.getRight());
    }

    // 计算最优水平间距
    private double calculateOptimalHorizontalGap(int treeWidth, double canvasWidth) {
        double maxGap = canvasWidth * 0.8 / Math.max(1, treeWidth);
        double minGap = MIN_HORIZONTAL_GAP;
        return Math.max(minGap, Math.min(maxGap, 150)); // 限制最大间距
    }

    // 计算最优垂直间距
    private double calculateOptimalVerticalSpacing(int treeHeight, double canvasHeight, double startY) {
        double availableHeight = canvasHeight - startY - 50;
        double maxSpacing = availableHeight / Math.max(1, treeHeight);
        return Math.min(VERTICAL_SPACING, maxSpacing);
    }

    // 位置类
    private class Position {
        double x;
        double y;

        Position(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    // 改进的节点位置计算算法
    private void calculateNodePositions(TreeNode node, double x, double y,
                                        double hGap, double vSpacing, int level,
                                        Map<TreeNode, Position> positions) {
        if (node == null) return;

        // 保存当前节点位置
        positions.put(node, new Position(x, y));

        // 计算子节点位置
        double childY = y + vSpacing;

        if (node.getLeft() != null || node.getRight() != null) {
            // 计算子树宽度
            double leftWidth = calculateSubtreeWidth(node.getLeft(), hGap);
            double rightWidth = calculateSubtreeWidth(node.getRight(), hGap);

            // 计算左右子节点的x坐标
            double leftX = x - (rightWidth > 0 ? hGap + rightWidth / 2 : hGap);
            double rightX = x + (leftWidth > 0 ? hGap + leftWidth / 2 : hGap);

            // 递归计算子节点位置
            if (node.getLeft() != null) {
                calculateNodePositions(node.getLeft(), leftX, childY, hGap, vSpacing, level + 1, positions);
            }
            if (node.getRight() != null) {
                calculateNodePositions(node.getRight(), rightX, childY, hGap, vSpacing, level + 1, positions);
            }
        }
    }
    // 计算子树宽度
    private double calculateSubtreeWidth(TreeNode node, double hGap) {
        if (node == null) return 0;

        int leafCount = countLeaves(node);
        if (leafCount == 0) return 0;

        return (leafCount - 1) * hGap;
    }

    // 计算叶子节点数量
    private int countLeaves(TreeNode node) {
        if (node == null) return 0;
        if (node.getLeft() == null && node.getRight() == null) return 1;
        return countLeaves(node.getLeft()) + countLeaves(node.getRight());
    }






    private void addTraversalStepInfo(BinaryTree.TraversalStep step, int stepIndex, int totalSteps) {
        Rectangle infoPanel = new Rectangle(400, 120);
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

        Text typeText = new Text("遍历类型: " + step.traversalType);
        typeText.setStyle("-fx-font-size: 12;");
        typeText.setX(20);
        typeText.setY(50);

        Text descText = new Text(step.description);
        descText.setStyle("-fx-font-size: 12;");
        descText.setX(20);
        descText.setY(70);

        // 显示已访问节点
        String visitedNodesStr = "已访问: " + step.visitedNodes.toString();
        Text visitedText = new Text(visitedNodesStr);
        visitedText.setStyle("-fx-font-size: 11; -fx-fill: #27ae60;");
        visitedText.setX(20);
        visitedText.setY(90);

        // 显示当前路径
        if (!step.currentPath.isEmpty()) {
            String pathStr = "当前路径: " + step.currentPath.toString();
            Text pathText = new Text(pathStr);
            pathText.setStyle("-fx-font-size: 11; -fx-fill: #e67e22;");
            pathText.setX(20);
            pathText.setY(110);
            canvas.getChildren().add(pathText);
        }

        canvas.getChildren().addAll(infoPanel, stepText, typeText, descText, visitedText);
    }


    // ========== BST搜索步骤演示 ==========

    // 逐步演示搜索过程
    public void drawSearchStep(TreeNode root, BST.SearchStep step, int stepIndex, int totalSteps) {
        canvas.getChildren().clear();
        if (root != null) {
            TreeLayoutInfo layoutInfo = calculateTreeLayout(root);
            double canvasWidth = canvas.getWidth();
            double canvasHeight = canvas.getHeight();

            double baseHGap = Math.min(200, canvasWidth * 0.4 / Math.max(1, layoutInfo.maxWidth * 0.5));
            double startX = canvasWidth / 2;
            double startY = Math.min(80, canvasHeight * 0.1);
            double verticalSpacing = Math.min(VERTICAL_SPACING,
                    (canvasHeight - startY - 50) / Math.max(1, layoutInfo.height));

            drawTreeRecursiveWithStepOptimized(root, startX, startY, baseHGap, verticalSpacing,
                    "bst", step, stepIndex, totalSteps);
        } else {
            drawEmptyTree("bst");
        }

        // 添加步骤信息
        if (step != null) {
            addStepInfo(step, stepIndex, totalSteps);
        }
    }

    // 优化的步骤绘制方法
    private void drawTreeRecursiveWithStepOptimized(TreeNode node, double x, double y, double hGap,
                                                    double verticalSpacing, String treeType,
                                                    BST.SearchStep step, int stepIndex, int totalSteps) {
        if (node == null) return;

        if (!isPointInCanvas(x, y)) {
            return;
        }

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
        } else {
            valueText.setStyle("-fx-font-weight: bold;");
        }

        canvas.getChildren().addAll(circle, valueText);

        // 动态调整水平间距
        double childHGap = Math.max(hGap * 0.6, NODE_RADIUS * 3);

        if (node.getLeft() != null) {
            double childX = x - childHGap;
            double childY = y + verticalSpacing;

            if (isPointInCanvas(childX, childY)) {
                Line leftLine = new Line(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
                leftLine.setStroke(Color.BLACK);
                leftLine.setStrokeWidth(2);
                canvas.getChildren().add(leftLine);

                drawTreeRecursiveWithStepOptimized(node.getLeft(), childX, childY, childHGap,
                        verticalSpacing, treeType, step, stepIndex, totalSteps);
            }
        }

        if (node.getRight() != null) {
            double childX = x + childHGap;
            double childY = y + verticalSpacing;

            if (isPointInCanvas(childX, childY)) {
                Line rightLine = new Line(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
                rightLine.setStroke(Color.BLACK);
                rightLine.setStrokeWidth(2);
                canvas.getChildren().add(rightLine);

                drawTreeRecursiveWithStepOptimized(node.getRight(), childX, childY, childHGap,
                        verticalSpacing, treeType, step, stepIndex, totalSteps);
            }
        }
    }

    // 添加步骤信息面板
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
    // BST删除步骤演示
    public void drawDeleteStep(TreeNode root, BST.DeleteStep step, int stepIndex, int totalSteps) {
        canvas.getChildren().clear();
        if (root != null) {
            TreeLayoutInfo layoutInfo = calculateTreeLayout(root);
            double canvasWidth = canvas.getWidth();
            double canvasHeight = canvas.getHeight();

            double baseHGap = Math.min(200, canvasWidth * 0.4 / Math.max(1, layoutInfo.maxWidth * 0.5));
            double startX = canvasWidth / 2;
            double startY = Math.min(80, canvasHeight * 0.1);
            double verticalSpacing = Math.min(VERTICAL_SPACING,
                    (canvasHeight - startY - 50) / Math.max(1, layoutInfo.height));

            drawTreeWithDeleteVisualization(root, startX, startY, baseHGap, verticalSpacing, step);
        } else {
            drawEmptyTree("bst");
        }

        // 添加简化的步骤信息
        addDeleteVisualInfo(step, stepIndex, totalSteps);
    }
    private void drawTreeWithDeleteVisualization(TreeNode node, double x, double y, double hGap,
                                                 double verticalSpacing, BST.DeleteStep step) {
        if (node == null) return;

        if (!isPointInCanvas(x, y)) {
            return;
        }

        // 判断节点状态
        boolean isCurrent = step.currentNode == node;
        boolean isToDelete = step.nodeToDelete == node;
        boolean isMarkedForDeletion = (step.stepType.equals("found") ||
                step.stepType.equals("leaf_case") ||
                step.stepType.equals("one_child_case") ||
                step.stepType.equals("two_children_case")) &&
                isToDelete;

        Circle circle = new Circle(x, y, NODE_RADIUS);

        // 根据步骤类型设置不同的视觉效果
        if (isMarkedForDeletion) {
            // 标记为要删除的节点 - 橙色警告色
            circle.setFill(Color.ORANGE);
            circle.setStroke(Color.RED);
            circle.setStrokeWidth(3);
        } else if (isCurrent && step.stepType.equals("compare")) {
            // 当前比较的节点 - 蓝色
            circle.setFill(Color.LIGHTBLUE);
            circle.setStroke(Color.BLUE);
            circle.setStrokeWidth(2);
        } else if (isCurrent) {
            // 当前操作的节点 - 金色
            circle.setFill(Color.GOLD);
            circle.setStroke(Color.ORANGE);
            circle.setStrokeWidth(2);
        } else {
            // 普通节点
            circle.setFill(Color.LIGHTCORAL);
            circle.setStroke(Color.DARKBLUE);
            circle.setStrokeWidth(2);
        }

        Text valueText = new Text(String.valueOf(node.getValue()));
        if (isMarkedForDeletion) {
            valueText.setStyle("-fx-font-weight: bold; -fx-fill: #c0392b;");
        } else if (isCurrent) {
            valueText.setStyle("-fx-font-weight: bold; -fx-fill: #2980b9;");
        } else {
            valueText.setStyle("-fx-font-weight: bold;");
        }
        valueText.setX(x - valueText.getLayoutBounds().getWidth() / 2);
        valueText.setY(y + valueText.getLayoutBounds().getHeight() / 4);

        canvas.getChildren().addAll(circle, valueText);

        // 根据步骤类型添加可视化元素
        addStepVisualization(node, x, y, step);

        // 动态调整水平间距
        double childHGap = Math.max(hGap * 0.6, NODE_RADIUS * 3);

        if (node.getLeft() != null) {
            double childX = x - childHGap;
            double childY = y + verticalSpacing;

            if (isPointInCanvas(childX, childY)) {
                Line leftLine = new Line(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);

                // 如果是比较步骤且向左查找，高亮左边线
                if (isCurrent && step.stepType.equals("compare") && step.description.contains("向左")) {
                    leftLine.setStroke(Color.RED);
                    leftLine.setStrokeWidth(3);
                } else {
                    leftLine.setStroke(Color.BLACK);
                    leftLine.setStrokeWidth(2);
                }

                canvas.getChildren().add(leftLine);

                drawTreeWithDeleteVisualization(node.getLeft(), childX, childY, childHGap,
                        verticalSpacing, step);
            }
        }

        if (node.getRight() != null) {
            double childX = x + childHGap;
            double childY = y + verticalSpacing;

            if (isPointInCanvas(childX, childY)) {
                Line rightLine = new Line(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);

                // 如果是比较步骤且向右查找，高亮右边线
                if (isCurrent && step.stepType.equals("compare") && step.description.contains("向右")) {
                    rightLine.setStroke(Color.RED);
                    rightLine.setStrokeWidth(3);
                } else {
                    rightLine.setStroke(Color.BLACK);
                    rightLine.setStrokeWidth(2);
                }

                canvas.getChildren().add(rightLine);

                drawTreeWithDeleteVisualization(node.getRight(), childX, childY, childHGap,
                        verticalSpacing, step);
            }
        }
    }

    // 根据步骤类型添加可视化元素
    private void addStepVisualization(TreeNode node, double x, double y, BST.DeleteStep step) {
        boolean isCurrent = step.currentNode == node;
        boolean isToDelete = step.nodeToDelete == node;

        if (isToDelete) {
            // 要删除的节点 - 根据步骤类型显示不同状态
            switch (step.stepType) {
                case "found":
                    Text foundMark = new Text("🎯 找到!");
                    foundMark.setStyle("-fx-font-size: 10; -fx-fill: #e74c3c; -fx-font-weight: bold;");
                    foundMark.setX(x - 12);
                    foundMark.setY(y - 25);
                    canvas.getChildren().add(foundMark);
                    break;

                case "analyze":
                    Text analyzeMark = new Text("📊 分析中...");
                    analyzeMark.setStyle("-fx-font-size: 9; -fx-fill: #e67e22; -fx-font-weight: bold;");
                    analyzeMark.setX(x - 18);
                    analyzeMark.setY(y - 25);
                    canvas.getChildren().add(analyzeMark);
                    break;

                case "leaf_identified":
                case "one_child_identified":
                case "two_children_identified":
                    String typeText = step.stepType.equals("leaf_identified") ? "叶子节点" :
                            step.stepType.equals("one_child_identified") ? "单子节点" : "双子节点";
                    Text typeMark = new Text("📝 " + typeText);
                    typeMark.setStyle("-fx-font-size: 9; -fx-fill: #e67e22; -fx-font-weight: bold;");
                    typeMark.setX(x - 15);
                    typeMark.setY(y - 35);
                    canvas.getChildren().add(typeMark);
                    break;

                case "ready_to_delete":
                case "ready_to_replace":
                    Text readyMark = new Text("⏳ 准备删除");
                    readyMark.setStyle("-fx-font-size: 9; -fx-fill: #c0392b; -fx-font-weight: bold;");
                    readyMark.setX(x - 20);
                    readyMark.setY(y - 25);
                    canvas.getChildren().add(readyMark);
                    break;
            }
        }

        if (isCurrent && (step.stepType.equals("compare") ||
                step.stepType.equals("traverse_left") ||
                step.stepType.equals("traverse_right"))) {
            // 查找步骤 - 显示比较信息
            String info = "";
            if (step.stepType.equals("compare")) {
                info = "比较: " + step.description.split(": ")[1];
            } else if (step.stepType.equals("traverse_left")) {
                info = "← 向左查找";
            } else {
                info = "→ 向右查找";
            }

            Text infoText = new Text(info);
            infoText.setStyle("-fx-font-size: 9; -fx-fill: #3498db; -fx-font-weight: bold;");
            infoText.setX(x + 15);
            infoText.setY(y - 15);
            canvas.getChildren().add(infoText);
        }
    }



    // 简化的步骤信息显示
    private void addDeleteVisualInfo(BST.DeleteStep step, int stepIndex, int totalSteps) {
        // 小型信息面板
        Rectangle infoPanel = new Rectangle(300, 40);
        infoPanel.setFill(Color.rgb(255, 255, 255, 0.9));
        infoPanel.setStroke(Color.DARKGRAY);
        infoPanel.setStrokeWidth(1);
        infoPanel.setLayoutX(10);
        infoPanel.setLayoutY(10);
        infoPanel.setArcWidth(10);
        infoPanel.setArcHeight(10);

        Text stepText = new Text("步骤 " + (stepIndex + 1) + "/" + totalSteps + " - " + getDeleteStepVisualName(step.stepType));
        stepText.setStyle("-fx-font-weight: bold; -fx-font-size: 12; -fx-fill: #2c3e50;");
        stepText.setX(20);
        stepText.setY(30);

        canvas.getChildren().addAll(infoPanel, stepText);

        // 在画布底部添加当前操作说明
        Text actionText = new Text(getDeleteActionDescription(step));
        actionText.setStyle("-fx-font-size: 14; -fx-fill: #e74c3c; -fx-font-weight: bold;");
        actionText.setX(canvas.getWidth() / 2 - actionText.getLayoutBounds().getWidth() / 2);
        actionText.setY(canvas.getHeight() - 20);
        canvas.getChildren().add(actionText);
    }

    // 获取步骤的可视化名称
    private String getDeleteStepVisualName(String stepType) {
        switch (stepType) {
            case "start": return "🚀 开始";
            case "compare": return "🔍 比较";
            case "found": return "🎯 找到目标";
            case "traverse_left": return "← 向左查找";
            case "traverse_right": return "→ 向右查找";
            case "analyze": return "📊 分析节点";
            case "leaf_identified": return "🍃 叶子节点";
            case "one_child_identified": return "🌿 单子节点";
            case "two_children_identified": return "🌳 双子节点";
            case "ready_to_delete": return "⏳ 准备删除";
            case "ready_to_replace": return "🔄 准备替换";
            case "need_successor": return "📋 需要后继";
            case "traverse_min": return "🔎 找最小值";
            case "min_found": return "✅ 找到最小值";
            case "successor_found": return "📋 找到后继";
            case "copy_value": return "🔄 复制值";
            case "delete_successor": return "🗑️ 删后继";
            case "delete_executed": return "✅ 删除执行";
            case "node_deleted": return "🎉 节点已删";
            case "not_found": return "❌ 未找到";
            case "complete": return "🏁 完成";
            default: return stepType;
        }
    }

    // 获取操作描述
    private String getDeleteActionDescription(BST.DeleteStep step) {
        switch (step.stepType) {
            case "find":
                return step.description;
            case "mark":
                return "🎯 标记要删除的节点: " + step.nodeToDelete.getValue();
            case "find_replacement":
                if (step.replacementNode != null) {
                    return "📋 找到后继节点: " + step.replacementNode.getValue();
                }
                return "📋 正在查找后继节点...";
            case "replace":
                return "🔄 将节点值替换为后继节点的值";
            case "delete":
                return "🗑️ 删除节点完成";
            case "complete":
                return "✅ 删除操作完成";
            default:
                return step.description;
        }
    }




    // ========== AVL树可视化方法 ==========
    public void drawAVLTree(AVLTree.AVLNode root) {
        drawAVLTreeWithSteps(root, null, 0, 0);
    }


    /**
     * 绘制AVL树真实步骤 - 显示实际树状态
     */
    public void drawAVLTreeWithSteps(AVLTree.AVLNode currentTree, AVLTree.AVLStep step,
                                     int stepIndex, int totalSteps) {
        canvas.getChildren().clear();

        if (currentTree == null) {
            drawEmptyTree("avl");
            addAVLStepInfo(step, stepIndex, totalSteps);
            return;
        }

        double canvasWidth = getCanvasWidth();
        double canvasHeight = getCanvasHeight();

        // 计算树的高度
        int treeHeight = getAVLTreeHeight(currentTree);

        // 动态计算布局参数
        double baseHGap = Math.min(200, canvasWidth * 0.4 / Math.max(1, treeHeight));
        double startX = canvasWidth / 2;
        double startY = Math.min(100, canvasHeight * 0.2);
        double verticalSpacing = Math.min(VERTICAL_SPACING,
                (canvasHeight - startY - 100) / Math.max(1, treeHeight));

        // 绘制当前树状态
        drawAVLTreeStructure(currentTree, startX, startY, baseHGap, verticalSpacing, step);

        // 添加步骤信息
        addAVLStepInfo(step, stepIndex, totalSteps);

        // 添加状态说明
        if (step != null) {
            addStepStateInfo(step, startX, startY + treeHeight * verticalSpacing + 30);
        }
    }

    /**
     * 绘制AVL树结构
     */
    private void drawAVLTreeStructure(AVLTree.AVLNode node, double x, double y,
                                      double hGap, double verticalSpacing,
                                      AVLTree.AVLStep step) {
        if (node == null) return;

        if (!isPointInCanvas(x, y)) {
            return;
        }

        // 绘制当前节点
        drawAVLTreeNode(node, x, y, step);

        // 动态调整水平间距
        double childHGap = Math.max(hGap * 0.6, NODE_RADIUS * 3);

        // 绘制左子树
        if (node.left != null) {
            double childX = x - childHGap;
            double childY = y + verticalSpacing;

            if (isPointInCanvas(childX, childY)) {
                drawAVLConnection(x, y, childX, childY, step, "left");
                drawAVLTreeStructure(node.left, childX, childY, childHGap, verticalSpacing, step);
            }
        }

        // 绘制右子树
        if (node.right != null) {
            double childX = x + childHGap;
            double childY = y + verticalSpacing;

            if (isPointInCanvas(childX, childY)) {
                drawAVLConnection(x, y, childX, childY, step, "right");
                drawAVLTreeStructure(node.right, childX, childY, childHGap, verticalSpacing, step);
            }
        }
    }

    /**
     * 绘制AVL树节点
     */
    private void drawAVLTreeNode(AVLTree.AVLNode node, double x, double y, AVLTree.AVLStep step) {
        Circle circle = new Circle(x, y, NODE_RADIUS);

        // 根据步骤类型设置节点颜色
        if (step != null && step.currentNode != null && step.currentNode.value == node.value) {
            // 当前操作的节点
            switch (step.type) {
                case "check_balance":
                    circle.setFill(step.needsRotation ? Color.LIGHTCORAL : Color.LIGHTGREEN);
                    circle.setStroke(step.needsRotation ? Color.RED : Color.GREEN);
                    break;
                case "before_rotate":
                    circle.setFill(Color.LIGHTYELLOW);
                    circle.setStroke(Color.ORANGE);
                    break;
                case "after_rotate":
                    circle.setFill(Color.LIGHTGREEN);
                    circle.setStroke(Color.DARKGREEN);
                    break;
                default:
                    circle.setFill(Color.LIGHTBLUE);
                    circle.setStroke(Color.DARKBLUE);
            }
            circle.setStrokeWidth(3);
        } else {
            circle.setFill(Color.LIGHTBLUE);
            circle.setStroke(Color.DARKBLUE);
            circle.setStrokeWidth(2);
        }

        // 节点文本（值和高度）
        String nodeText = node.value + "\n(h=" + node.height + ")";
        Text text = new Text(nodeText);
        text.setStyle("-fx-font-weight: bold; -fx-font-size: 10; -fx-text-alignment: center;");
        text.setX(x - text.getLayoutBounds().getWidth() / 2);
        text.setY(y + text.getLayoutBounds().getHeight() / 4);

        canvas.getChildren().addAll(circle, text);

        // 添加操作标记
        if (step != null && step.currentNode != null && step.currentNode.value == node.value) {
            addNodeOperationMark(x, y, step);
        }
    }

    /**
     * 绘制连接线
     */
    private void drawAVLConnection(double fromX, double fromY, double toX, double toY,
                                   AVLTree.AVLStep step, String direction) {
        Line line = new Line(fromX, fromY + NODE_RADIUS, toX, toY - NODE_RADIUS);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(2);
        canvas.getChildren().add(line);
    }

    /**
     * 添加节点操作标记
     */
    private void addNodeOperationMark(double x, double y, AVLTree.AVLStep step) {
        String markText = "";
        Color markColor = Color.BLACK;

        switch (step.type) {
            case "insert":
                markText = "📥 插入";
                markColor = Color.BLUE;
                break;
            case "check_balance":
                if (step.needsRotation) {
                    markText = "⚖️ 不平衡";
                    markColor = Color.RED;
                } else {
                    markText = "⚖️ 平衡";
                    markColor = Color.GREEN;
                }
                break;
            case "before_rotate":
                markText = "🔄 准备旋转";
                markColor = Color.ORANGE;
                break;
            case "after_rotate":
                markText = "✅ 旋转完成";
                markColor = Color.GREEN;
                break;
        }

        if (!markText.isEmpty()) {
            Text mark = new Text(markText);
            mark.setStyle("-fx-font-weight: bold; -fx-font-size: 9;");
            mark.setFill(markColor);
            mark.setX(x - mark.getLayoutBounds().getWidth() / 2);
            mark.setY(y - 20);
            canvas.getChildren().add(mark);
        }
    }

    /**
     * 添加步骤状态信息
     */
    private void addStepStateInfo(AVLTree.AVLStep step, double x, double y) {
        if (step == null) return;

        String stateInfo = step.description;
        Color color = Color.BLACK;

        switch (step.type) {
            case "check_balance":
                color = step.needsRotation ? Color.RED : Color.GREEN;
                if (step.balanceFactor != 0) {
                    stateInfo += " (平衡因子: " + step.balanceFactor + ")";
                }
                break;
            case "before_rotate":
                color = Color.ORANGE;
                if (step.rotationType != null) {
                    stateInfo += " → " + step.rotationType;
                }
                break;
            case "after_rotate":
                color = Color.GREEN;
                break;
            case "insert":
                color = Color.BLUE;
                break;
        }

        Text stateText = new Text(stateInfo);
        stateText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        stateText.setFill(color);
        stateText.setX(x - stateText.getLayoutBounds().getWidth() / 2);
        stateText.setY(y);
        canvas.getChildren().add(stateText);
    }

    /**
     * 增强的步骤信息面板
     */
    private void addAVLStepInfo(AVLTree.AVLStep step, int stepIndex, int totalSteps) {
        Rectangle infoPanel = new Rectangle(450, 100);
        infoPanel.setFill(Color.rgb(255, 255, 255, 0.95));
        infoPanel.setStroke(Color.DARKGRAY);
        infoPanel.setStrokeWidth(1);
        infoPanel.setLayoutX(10);
        infoPanel.setLayoutY(10);
        infoPanel.setArcWidth(10);
        infoPanel.setArcHeight(10);

        Text stepText = new Text("步骤 " + (stepIndex + 1) + "/" + totalSteps);
        stepText.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-fill: #2c3e50;");
        stepText.setX(20);
        stepText.setY(30);

        Text typeText = new Text("操作: " + getAVLStepTypeChinese(step.type));
        typeText.setStyle("-fx-font-size: 12; -fx-fill: #34495e;");
        typeText.setX(20);
        typeText.setY(50);

        Text descText = new Text(step.description);
        descText.setStyle("-fx-font-size: 11; -fx-fill: #7f8c8d;");
        descText.setX(20);
        descText.setY(70);

        canvas.getChildren().addAll(infoPanel, stepText, typeText, descText);

        // 显示平衡因子
        if (step.balanceFactor != 0) {
            Text balanceText = new Text("平衡因子: " + step.balanceFactor);
            balanceText.setStyle("-fx-font-size: 11; -fx-fill: #e74c3c; -fx-font-weight: bold;");
            balanceText.setX(250);
            balanceText.setY(50);
            canvas.getChildren().add(balanceText);
        }

        // 显示旋转信息
        if (step.rotationType != null) {
            Text rotationText = new Text("旋转: " + step.rotationType);
            rotationText.setStyle("-fx-font-size: 11; -fx-fill: #9b59b6; -fx-font-weight: bold;");
            rotationText.setX(250);
            rotationText.setY(70);
            canvas.getChildren().add(rotationText);
        }

        if (step.rotationCase != null) {
            Text caseText = new Text("情况: " + step.rotationCase);
            caseText.setStyle("-fx-font-size: 11; -fx-fill: #e67e22; -fx-font-weight: bold;");
            caseText.setX(250);
            caseText.setY(90);
            canvas.getChildren().add(caseText);
        }
    }
    private String getAVLStepTypeChinese(String stepType) {
        switch (stepType) {
            case "insert": return "插入节点";
            case "start_rotate": return "开始旋转";
            case "rotate": return "旋转操作";
            case "balance": return "平衡调整";
            case "check_balance": return "检查平衡";
            case "end_rotate": return "旋转完成";
            case "complete": return "完成插入";
            default: return stepType;
        }
    }

    // ========== 哈夫曼树可视化方法 ==========


    public void drawHuffmanTree(HuffmanTree.HuffmanNode root) {
        canvas.getChildren().clear();
        if (root != null) {
            double canvasWidth = canvas.getWidth();
            double canvasHeight = canvas.getHeight();

            // 计算树的高度
            int height = getHuffmanTreeHeight(root);

            // 动态计算布局参数
            double startX = canvasWidth / 2;
            double startY = Math.min(80, canvasHeight * 0.15);
            double baseHGap = Math.min(300, canvasWidth * 0.8 / Math.max(1, height));
            double verticalSpacing = Math.min(VERTICAL_SPACING,
                    (canvasHeight - startY - 50) / Math.max(1, height));

            // 使用改进的绘制方法
            drawHuffmanTreeRecursiveImproved(root, startX, startY, baseHGap, verticalSpacing, null, 0);
        } else {
            drawEmptyTree("huffman");
        }
    }

    // 哈夫曼树步骤演示
    public void drawHuffmanStep(HuffmanTree.HuffmanNode root, HuffmanTree.HuffmanStep step,
                                int stepIndex, int totalSteps) {
        canvas.getChildren().clear();

        if (step == null) return;

        System.out.println("绘制哈夫曼步骤 " + (stepIndex + 1) + "/" + totalSteps + " - 类型: " + step.type);

        switch (step.type) {
            case "frequency":
                drawFrequencyStep(step);
                break;
            case "create_node":
                drawCreateNodeStep(step);
                break;
            case "forest":
                drawForestStep(step, stepIndex, totalSteps);
                break;
            case "combine":
                drawCombineStepWithTree(step, stepIndex, totalSteps);
                break;

            case "build_tree":
                drawCompleteTreeStep(step, stepIndex, totalSteps);
                break;
            case "generate_code":
                drawGenerateCodeStep(step, totalSteps);
                break;
            case "result":
                drawResultStep(step, totalSteps);
                break;
            default:
                // 默认显示当前树结构
                if (root != null) {
                    drawHuffmanTreeStructure(root, step, stepIndex, totalSteps);
                }
                break;
        }
    }

    private void drawForestStep(HuffmanTree.HuffmanStep step, int stepIndex, int totalSteps) {
        // 先绘制步骤信息
        addHuffmanStepInfo(step, stepIndex, totalSteps);

        Text title = new Text("当前森林状态");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-fill: #2c3e50;");
        title.setX(canvas.getWidth() / 2 - title.getLayoutBounds().getWidth() / 2);
        title.setY(120);
        canvas.getChildren().add(title);

        // 绘制森林中的所有树
        if (step.combinedNodes != null && !step.combinedNodes.isEmpty()) {
            double startX = 100;
            double startY = 180;
            double treeSpacing = 200;

            for (int i = 0; i < step.combinedNodes.size(); i++) {
                HuffmanTree.HuffmanNode treeRoot = step.combinedNodes.get(i);
                double treeX = startX + i * treeSpacing;

                // 绘制单棵树
                drawSingleHuffmanTree(treeRoot, treeX, startY, 80, 60, "森林中的树 " + (i + 1));
            }
        }

        // 显示队列状态
        drawQueueStatus(step, 400);
    }
    private void drawCombineStepWithTree(HuffmanTree.HuffmanStep step, int stepIndex, int totalSteps) {
        addHuffmanStepInfo(step, stepIndex, totalSteps);

        Text title = new Text("合并节点");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-fill: #2c3e50;");
        title.setX(canvas.getWidth() / 2 - title.getLayoutBounds().getWidth() / 2);
        title.setY(120);
        canvas.getChildren().add(title);

        // 显示被合并的节点和新建的树
        if (step.combinedNodes.size() >= 3) {
            HuffmanTree.HuffmanNode left = step.combinedNodes.get(0);
            HuffmanTree.HuffmanNode right = step.combinedNodes.get(1);
            HuffmanTree.HuffmanNode parent = step.combinedNodes.get(2);

            double centerX = canvas.getWidth() / 2;
            double baseY = 200;

            // 绘制左子树
            drawSingleHuffmanTree(left, centerX - 200, baseY, 60, 50, "左节点");

            // 绘制右子树
            drawSingleHuffmanTree(right, centerX + 200, baseY, 60, 50, "右节点");

            // 绘制合并后的树
            drawSingleHuffmanTree(parent, centerX, baseY + 150, 80, 60, "合并后的树");

            // 绘制合并箭头
            drawMergeArrows(centerX - 200, baseY + 80, centerX + 200, baseY + 80, centerX, baseY + 150);
        }

        drawQueueStatus(step, 500);
    }



    // 绘制完整树步骤
    private void drawCompleteTreeStep(HuffmanTree.HuffmanStep step, int stepIndex, int totalSteps) {
        addHuffmanStepInfo(step, stepIndex, totalSteps);

        Text title = new Text("哈夫曼树构建完成");
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-fill: #27ae60;");
        title.setX(canvas.getWidth() / 2 - title.getLayoutBounds().getWidth() / 2);
        title.setY(80);
        canvas.getChildren().add(title);

        // 绘制最终的完整哈夫曼树
        if (step.currentNode != null) {
            drawHuffmanTreeStructure(step.currentNode, step, stepIndex, totalSteps);
        }
    }

    // 绘制单棵哈夫曼树
    private void drawSingleHuffmanTree(HuffmanTree.HuffmanNode root, double startX, double startY,
                                       double hGap, double verticalSpacing, String title) {
        if (root == null) return;

        // 绘制标题
        Text treeTitle = new Text(title);
        treeTitle.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-fill: #34495e;");
        treeTitle.setX(startX - treeTitle.getLayoutBounds().getWidth() / 2);
        treeTitle.setY(startY - 20);
        canvas.getChildren().add(treeTitle);

        // 递归绘制树结构
        drawHuffmanTreeRecursive(root, startX, startY, hGap, verticalSpacing,0);
    }
    private double getCanvasWidth() {
        if (canvas == null || canvas.getWidth() <= 0) {
            return 800; // 默认宽度
        }
        return canvas.getWidth();
    }

    // 获取安全的画布高度
    private double getCanvasHeight() {
        if (canvas == null || canvas.getHeight() <= 0) {
            return 500; // 默认高度
        }
        return canvas.getHeight();
    }
    private boolean isPositionInCanvas(double x, double y) {
        double canvasWidth = getCanvasWidth();
        double canvasHeight = getCanvasHeight();
        double margin = NODE_RADIUS * 2;

        return x >= margin && x <= canvasWidth - margin &&
                y >= margin && y <= canvasHeight - margin;
    }

    // 修复遍历步骤绘制方法
    public void drawTraversalStep(TreeNode root, BinaryTree.TraversalStep step,
                                  int stepIndex, int totalSteps) {
        System.out.println("=== 开始绘制遍历步骤 " + (stepIndex + 1) + "/" + totalSteps + " ===");

        if (canvas == null) {
            System.out.println("错误: canvas 为 null");
            return;
        }

        canvas.getChildren().clear();

        if (root == null) {
            drawEmptyTree("binary");
            addTraversalStepInfo(step, stepIndex, totalSteps);
            return;
        }

        // 获取画布尺寸
        double canvasWidth = getCanvasWidth();
        double canvasHeight = getCanvasHeight();

        // 计算树的高度和宽度
        int treeHeight = getTreeHeight(root);
        int treeWidth = calculateTreeWidth(root);

        // 计算起始位置
        double startY = Math.max(80, canvasHeight * 0.1);
        double startX = canvasWidth / 2;

        // 动态计算间距
        double horizontalGap = calculateOptimalHorizontalGap(treeWidth, canvasWidth);
        double verticalSpacing = calculateOptimalVerticalSpacing(treeHeight, canvasHeight, startY);

        // 计算节点位置
        Map<TreeNode, Position> nodePositions = new HashMap<TreeNode, Position>();
        calculateNodePositions(root, startX, startY, horizontalGap, verticalSpacing, 0, nodePositions);

        // 绘制树
        drawTreeWithPositions(root, nodePositions, "binary", step);

        // 添加步骤信息
        addTraversalStepInfo(step, stepIndex, totalSteps);

        System.out.println("=== 遍历步骤绘制完成 ===");
    }


    // 递归绘制哈夫曼树
    private void drawHuffmanTreeRecursive(HuffmanTree.HuffmanNode node, double x, double y,
                                          double hGap, double verticalSpacing, int depth) {
        if (node == null) return;

        // 检查位置是否在画布内
        if (!isPositionInCanvas(x, y)) {
            System.out.println("哈夫曼树节点位置超出画布: (" + x + ", " + y + ")");
            return;
        }

        // 动态调整间距，避免深度越大间距越小
        double adjustedHGap = hGap / (depth * 0.3 + 1);
        double adjustedVGap = Math.min(verticalSpacing, 80); // 限制垂直间距

        // 绘制当前节点
        Circle circle = new Circle(x, y, 15);
        if (node.isLeaf()) {
            circle.setFill(Color.LIGHTGREEN);
            circle.setStroke(Color.DARKGREEN);
        } else {
            circle.setFill(Color.LIGHTCORAL);
            circle.setStroke(Color.DARKRED);
        }
        circle.setStrokeWidth(2);

        // 节点文本
        String nodeText = node.isLeaf() ?
                "'" + node.character + "'\n" + node.frequency :
                String.valueOf(node.frequency);
        Text text = new Text(nodeText);
        text.setStyle("-fx-font-weight: bold; -fx-font-size: 10; -fx-text-alignment: center;");
        text.setX(x - text.getLayoutBounds().getWidth() / 2);
        text.setY(y + text.getLayoutBounds().getHeight() / 4);

        canvas.getChildren().addAll(circle, text);

        // 绘制子节点
        double childHGap = Math.max(adjustedHGap * 0.6, 40); // 最小水平间距

        if (node.left != null) {
            double childX = x - childHGap;
            double childY = y + adjustedVGap;

            if (isPositionInCanvas(childX, childY)) {
                // 绘制连接线和编码
                Line leftLine = new Line(x, y + 15, childX, childY - 15);
                leftLine.setStroke(Color.BLACK);
                leftLine.setStrokeWidth(2);
                canvas.getChildren().add(leftLine);

                Text zeroText = new Text("0");
                zeroText.setStyle("-fx-font-weight: bold; -fx-fill: #3498db;");
                zeroText.setX((x + childX) / 2 - 5);
                zeroText.setY((y + childY) / 2);
                canvas.getChildren().add(zeroText);

                drawHuffmanTreeRecursive(node.left, childX, childY, childHGap, adjustedVGap, depth + 1);
            }
        }

        if (node.right != null) {
            double childX = x + childHGap;
            double childY = y + adjustedVGap;

            if (isPositionInCanvas(childX, childY)) {
                // 绘制连接线和编码
                Line rightLine = new Line(x, y + 15, childX, childY - 15);
                rightLine.setStroke(Color.BLACK);
                rightLine.setStrokeWidth(2);
                canvas.getChildren().add(rightLine);

                Text oneText = new Text("1");
                oneText.setStyle("-fx-font-weight: bold; -fx-fill: #e74c3c;");
                oneText.setX((x + childX) / 2 - 5);
                oneText.setY((y + childY) / 2);
                canvas.getChildren().add(oneText);

                drawHuffmanTreeRecursive(node.right, childX, childY, childHGap, adjustedVGap, depth + 1);
            }
        }
    }

    // 绘制完整的哈夫曼树结构
   private void drawHuffmanTreeStructure(HuffmanTree.HuffmanNode root, HuffmanTree.HuffmanStep step,
                                          int stepIndex, int totalSteps) {
        addHuffmanStepInfo(step, stepIndex, totalSteps);

        double canvasWidth = getCanvasWidth();
        double canvasHeight = getCanvasHeight();

        // 计算树的高度
        int treeHeight = getHuffmanTreeHeight(root);

        // 计算起始位置
        double startX = canvasWidth / 2;
        double startY = 120;

        // 动态计算间距
        double baseHGap = Math.min(200, canvasWidth * 0.6 / Math.max(1, treeHeight));
        double verticalSpacing = Math.min(80, (canvasHeight - startY - 100) / Math.max(1, treeHeight));

        // 绘制完整的树
        drawHuffmanTreeRecursive(root, startX, startY, baseHGap, verticalSpacing, 0);
    }

    // 绘制合并箭头
    private void drawMergeArrows(double leftX, double leftY, double rightX, double rightY,
                                 double parentX, double parentY) {
        // 从左节点到父节点的箭头
        Line leftArrow = new Line(leftX, leftY, parentX - 30, parentY - 40);
        leftArrow.setStroke(Color.PURPLE);
        leftArrow.setStrokeWidth(2);
        leftArrow.getStrokeDashArray().addAll(5.0, 5.0);

        // 从右节点到父节点的箭头
        Line rightArrow = new Line(rightX, rightY, parentX + 30, parentY - 40);
        rightArrow.setStroke(Color.PURPLE);
        rightArrow.setStrokeWidth(2);
        rightArrow.getStrokeDashArray().addAll(5.0, 5.0);

        canvas.getChildren().addAll(leftArrow, rightArrow);

        // 合并说明文本
        Text mergeText = new Text("合并 ↑");
        mergeText.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-fill: #9b59b6;");
        mergeText.setX((leftX + rightX) / 2 - mergeText.getLayoutBounds().getWidth() / 2);
        mergeText.setY((leftY + parentY) / 2);
        canvas.getChildren().add(mergeText);
    }

    // 绘制队列状态
    private void drawQueueStatus(HuffmanTree.HuffmanStep step, double yPos) {
        Text queueTitle = new Text("优先队列状态:");
        queueTitle.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-fill: #2c3e50;");
        queueTitle.setX(50);
        queueTitle.setY(yPos);
        canvas.getChildren().add(queueTitle);

        if (step.currentQueue != null) {
            PriorityQueue<HuffmanTree.HuffmanNode> queueCopy =
                    new PriorityQueue<HuffmanTree.HuffmanNode>(step.currentQueue);
            double queueY = yPos + 25;
            int count = 0;

            while (!queueCopy.isEmpty() && count < 10) {
                HuffmanTree.HuffmanNode node = queueCopy.poll();
                Text nodeInfo = new Text(node.toString());
                nodeInfo.setStyle("-fx-font-size: 12; -fx-fill: #34495e;");
                nodeInfo.setX(70);
                nodeInfo.setY(queueY);
                canvas.getChildren().add(nodeInfo);
                queueY += 20;
                count++;
            }
        }
    }
    // 绘制频率统计步骤
    private void drawFrequencyStep(HuffmanTree.HuffmanStep step) {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        // 先绘制步骤信息
        addHuffmanStepInfo(step, 0, Integer.MAX_VALUE);

        Text title = new Text("字符频率统计");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-fill: #2c3e50;");
        title.setX(canvasWidth / 2 - title.getLayoutBounds().getWidth() / 2);
        title.setY(120);
        canvas.getChildren().add(title);

        // 创建频率统计的容器
        Rectangle statsPanel = new Rectangle(canvasWidth * 0.6, 200);
        statsPanel.setFill(Color.rgb(248, 249, 250, 0.9));
        statsPanel.setStroke(Color.LIGHTGRAY);
        statsPanel.setStrokeWidth(1);
        statsPanel.setArcWidth(10);
        statsPanel.setArcHeight(10);
        statsPanel.setX(canvasWidth * 0.2);
        statsPanel.setY(150);
        canvas.getChildren().add(statsPanel);

        int y = 180;
        int column = 0;
        double columnWidth = canvasWidth * 0.6 / 2;

        for (Map.Entry<Character, Integer> entry : step.frequencyMap.entrySet()) {
            double x = canvasWidth * 0.2 + column * columnWidth + 20;

            Text charText = new Text("'" + entry.getKey() + "': " + entry.getValue() + " 次");
            charText.setStyle("-fx-font-size: 14; -fx-fill: #34495e;");
            charText.setX(x);
            charText.setY(y);
            canvas.getChildren().add(charText);

            y += 25;

            // 如果一列满了，换到下一列
            if (y > 150 + 180) {
                y = 180;
                column++;

            }
        }
    }


    // 绘制创建节点步骤
    private void drawCreateNodeStep(HuffmanTree.HuffmanStep step) {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        // 先绘制步骤信息
        addHuffmanStepInfo(step, 0, Integer.MAX_VALUE);

        Text title = new Text("初始叶子节点");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-fill: #2c3e50;");
        title.setX(canvasWidth / 2 - title.getLayoutBounds().getWidth() / 2);
        title.setY(120);
        canvas.getChildren().add(title);

        double startX = canvasWidth * 0.1;
        double startY = 160; // 下移起始位置
        int nodesPerRow = 6; // 增加每行节点数
        int count = 0;

        // 复制队列以避免修改原队列
        PriorityQueue<HuffmanTree.HuffmanNode> queueCopy =
                new PriorityQueue<>(step.currentQueue);

        while (!queueCopy.isEmpty()) {
            HuffmanTree.HuffmanNode node = queueCopy.poll();

            // 绘制节点
            Circle circle = new Circle(startX, startY, 18); // 稍微减小节点半径
            circle.setFill(Color.LIGHTGREEN);
            circle.setStroke(Color.DARKGREEN);
            circle.setStrokeWidth(2);

            Text nodeText = new Text(node.toString());
            nodeText.setStyle("-fx-font-size: 9; -fx-font-weight: bold;"); // 减小字体
            nodeText.setX(startX - nodeText.getLayoutBounds().getWidth() / 2);
            nodeText.setY(startY + 5);

            canvas.getChildren().addAll(circle, nodeText);

            count++;
            startX += 70; // 减小水平间距

            if (count % nodesPerRow == 0) {
                startX = canvasWidth * 0.1;
                startY += 50; // 减小垂直间距


            }
        }
    }



    // 绘制编码生成步骤
    private void drawGenerateCodeStep(HuffmanTree.HuffmanStep step,int totalSteps) {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        // 先绘制步骤信息
        addHuffmanStepInfo(step, 0, totalSteps);

        Text title = new Text("哈夫曼编码");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-fill: #2c3e50;");
        title.setX(canvasWidth / 2 - title.getLayoutBounds().getWidth() / 2);
        title.setY(120);
        canvas.getChildren().add(title);

        // 创建编码表容器
        Rectangle codePanel = new Rectangle(canvasWidth * 0.6, 250);
        codePanel.setFill(Color.rgb(248, 249, 250, 0.9));
        codePanel.setStroke(Color.LIGHTGRAY);
        codePanel.setStrokeWidth(1);
        codePanel.setArcWidth(10);
        codePanel.setArcHeight(10);
        codePanel.setX(canvasWidth * 0.2);
        codePanel.setY(140);
        canvas.getChildren().add(codePanel);

        int y = 180;
        int column = 0;
        double columnWidth = canvasWidth * 0.6 / 2;
        int count = 0;

        for (Map.Entry<Character, String> entry : step.huffmanCodes.entrySet()) {
            double x = canvasWidth * 0.2 + column * columnWidth + 20;

            Text codeText = new Text("'" + entry.getKey() + "' → " + entry.getValue());
            codeText.setStyle("-fx-font-size: 14; -fx-fill: #34495e;");
            codeText.setX(x);
            codeText.setY(y);
            canvas.getChildren().add(codeText);

            y += 25;
            count++;

            // 如果一列满了，换到下一列
            if (y > 150 + 220) {
                y = 180;
                column++;

                // 如果列也满了，停止显示
                if (column >= 2) {
                    Text moreText = new Text("... 还有 " + (step.huffmanCodes.size() - count) + " 个编码");
                    moreText.setStyle("-fx-font-size: 12; -fx-fill: #7f8c8d; -fx-font-style: italic;");
                    moreText.setX(x);
                    moreText.setY(y);
                    canvas.getChildren().add(moreText);
                    break;
                }
            }
        }
    }


    // 绘制结果步骤
    private void drawResultStep(HuffmanTree.HuffmanStep step,int totalSteps) {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        // 先绘制步骤信息
        addHuffmanStepInfo(step, 0, totalSteps);

        Text title = new Text("哈夫曼编码结果");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-fill: #2c3e50;");
        title.setX(canvasWidth / 2 - title.getLayoutBounds().getWidth() / 2);
        title.setY(120);
        canvas.getChildren().add(title);

        // 创建结果容器
        Rectangle resultPanel = new Rectangle(canvasWidth * 0.7, 300);
        resultPanel.setFill(Color.rgb(248, 249, 250, 0.9));
        resultPanel.setStroke(Color.LIGHTGRAY);
        resultPanel.setStrokeWidth(1);
        resultPanel.setArcWidth(10);
        resultPanel.setArcHeight(10);
        resultPanel.setX(canvasWidth * 0.15);
        resultPanel.setY(140);
        canvas.getChildren().add(resultPanel);

        // 显示编码表
        Text codesTitle = new Text("编码表:");
        codesTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-fill: #34495e;");
        codesTitle.setX(canvasWidth * 0.2);
        codesTitle.setY(180);
        canvas.getChildren().add(codesTitle);

        int y = 210;
        int column = 0;
        double columnWidth = canvasWidth * 0.7 / 3; // 三列布局
        int count = 0;

        for (Map.Entry<Character, String> entry : step.huffmanCodes.entrySet()) {
            double x = canvasWidth * 0.15 + column * columnWidth + 20;

            Text codeText = new Text("'" + entry.getKey() + "' : " + entry.getValue());
            codeText.setStyle("-fx-font-size: 14; -fx-fill: #2c3e50;");
            codeText.setX(x);
            codeText.setY(y);
            canvas.getChildren().add(codeText);

            y += 25;
            count++;

            // 如果一列满了，换到下一列
            if (y > 150 + 250) {
                y = 210;
                column++;

                // 如果列也满了，停止显示
                if (column >= 3) {
                    Text moreText = new Text("... 还有 " + (step.huffmanCodes.size() - count) + " 个编码");
                    moreText.setStyle("-fx-font-size: 12; -fx-fill: #7f8c8d; -fx-font-style: italic;");
                    moreText.setX(x);
                    moreText.setY(y);
                    canvas.getChildren().add(moreText);
                    break;
                }
            }
        }

        // 显示压缩信息 - 放在底部
        Text infoText = new Text(step.description);
        infoText.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-fill: #27ae60;");
        infoText.setX(canvasWidth / 2 - infoText.getLayoutBounds().getWidth() / 2);
        infoText.setY(canvasHeight - 80);
        canvas.getChildren().add(infoText);
    }

    // 修改原有的哈夫曼树绘制方法以支持步骤高亮
    private void drawHuffmanTreeRecursiveImproved(HuffmanTree.HuffmanNode node, double x, double y,
                                                  double hGap, double verticalSpacing,
                                                  HuffmanTree.HuffmanStep step, int depth) {
        if (node == null) return;

        // 动态调整水平间距，避免深度越大间距越小
        double dynamicHGap = Math.max(hGap * 0.7, NODE_RADIUS * 4);
        double childVerticalSpacing = Math.min(verticalSpacing, 100); // 限制最小垂直间距

        // 检查节点是否在画布范围内
        if (!isPointInCanvas(x, y)) {
            return;
        }

        // 绘制当前节点
        Circle circle = new Circle(x, y, NODE_RADIUS);

        // 根据步骤类型设置颜色
        if (step != null && step.currentNode == node) {
            circle.setFill(Color.GOLD);
            circle.setStroke(Color.RED);
            circle.setStrokeWidth(3);
        } else {
            circle.setFill(node.isLeaf() ? Color.LIGHTGREEN : Color.LIGHTCORAL);
            circle.setStroke(node.isLeaf() ? Color.DARKGREEN : Color.DARKRED);
            circle.setStrokeWidth(2);
        }

        String nodeText = node.isLeaf() ?
                "'" + node.character + "':" + node.frequency :
                String.valueOf(node.frequency);
        Text text = new Text(nodeText);
        text.setStyle("-fx-font-weight: bold; -fx-font-size: " + (node.isLeaf() ? "10" : "12") + ";");
        text.setX(x - text.getLayoutBounds().getWidth() / 2);
        text.setY(y + text.getLayoutBounds().getHeight() / 4);

        canvas.getChildren().addAll(circle, text);

        // 绘制子节点连接
        if (node.left != null) {
            double childX = x - dynamicHGap;
            double childY = y + childVerticalSpacing;

            // 检查子节点位置是否合理，避免重叠
            if (isPointInCanvas(childX, childY) && Math.abs(childX - x) > NODE_RADIUS * 2) {
                Line leftLine = new Line(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
                leftLine.setStroke(Color.BLACK);
                leftLine.setStrokeWidth(2);
                canvas.getChildren().add(leftLine);

                Text zeroText = new Text("0");
                zeroText.setStyle("-fx-font-weight: bold;");
                zeroText.setX((x + childX) / 2 - 5);
                zeroText.setY((y + childY) / 2);
                canvas.getChildren().add(zeroText);

                drawHuffmanTreeRecursiveImproved(node.left, childX, childY,
                        dynamicHGap, childVerticalSpacing, step, depth + 1);
            }
        }

        if (node.right != null) {
            double childX = x + dynamicHGap;
            double childY = y + childVerticalSpacing;

            // 检查子节点位置是否合理，避免重叠
            if (isPointInCanvas(childX, childY) && Math.abs(childX - x) > NODE_RADIUS * 2) {
                Line rightLine = new Line(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
                rightLine.setStroke(Color.BLACK);
                rightLine.setStrokeWidth(2);
                canvas.getChildren().add(rightLine);

                Text oneText = new Text("1");
                oneText.setStyle("-fx-font-weight: bold;");
                oneText.setX((x + childX) / 2 - 5);
                oneText.setY((y + childY) / 2);
                canvas.getChildren().add(oneText);

                drawHuffmanTreeRecursiveImproved(node.right, childX, childY,
                        dynamicHGap, childVerticalSpacing, step, depth + 1);
            }
        }
    }

    // 添加哈夫曼步骤信息面板
    private void addHuffmanStepInfo(HuffmanTree.HuffmanStep step, int stepIndex, int totalSteps) {
        // 使用固定位置，避免与其他元素重叠
        double panelY = 10;

        Rectangle infoPanel = new Rectangle(400, 70); // 减小高度
        infoPanel.setFill(Color.rgb(255, 255, 255, 0.95));
        infoPanel.setStroke(Color.DARKGRAY);
        infoPanel.setStrokeWidth(1);
        infoPanel.setLayoutX(10);
        infoPanel.setLayoutY(panelY);
        infoPanel.setArcWidth(10);
        infoPanel.setArcHeight(10);

        Text stepText = new Text("步骤 " + (stepIndex + 1) + "/" + totalSteps);
        stepText.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-fill: #2c3e50;");
        stepText.setX(20);
        stepText.setY(panelY + 25);

        Text typeText = new Text("类型: " + getHuffmanStepTypeChinese(step.type));
        typeText.setStyle("-fx-font-size: 12; -fx-fill: #34495e;");
        typeText.setX(20);
        typeText.setY(panelY + 45);

        Text descText = new Text(step.description);
        descText.setStyle("-fx-font-size: 11; -fx-fill: #7f8c8d;");
        descText.setX(20);
        descText.setY(panelY + 65);

        // 限制描述文本长度
        if (descText.getLayoutBounds().getWidth() > 350) {
            String shortDesc = step.description.length() > 50 ?
                    step.description.substring(0, 50) + "..." : step.description;
            descText.setText(shortDesc);
        }

        canvas.getChildren().addAll(infoPanel, stepText, typeText, descText);
    }

    // 获取哈夫曼步骤类型的中文描述
    private String getHuffmanStepTypeChinese(String stepType) {
        switch (stepType) {
            case "frequency": return "频率统计";
            case "create_node": return "创建节点";
            case "combine": return "合并节点";
            case "build_tree": return "构建树";
            case "generate_code": return "生成编码";
            case "result": return "最终结果";
            default: return stepType;
        }
    }

}