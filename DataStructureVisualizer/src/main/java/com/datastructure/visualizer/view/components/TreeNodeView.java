package com.datastructure.visualizer.view.components;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javafx.scene.paint.Color;

public class TreeNodeView extends StackPane {
    private Circle circle;
    private Text text;
    private String value;
    private TreeNodeView left;
    private TreeNodeView right;

    public TreeNodeView(String value) {
        this.value = value;
        initialize();
    }

    private void initialize() {
        circle = new Circle(20);
        circle.setFill(Color.LIGHTBLUE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        text = new Text(value);
        text.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");

        getChildren().addAll(circle, text);

        setOnMouseEntered(e -> {
            circle.setStroke(Color.DARKBLUE);
            circle.setStrokeWidth(3);
        });

        setOnMouseExited(e -> {
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(2);
        });
    }

    public void highlight() {
        circle.setFill(Color.YELLOW);
    }

    public void markFound() {
        circle.setFill(Color.LIGHTGREEN);
    }

    public void markDeleted() {
        circle.setFill(Color.RED);
    }

    public void resetColor() {
        circle.setFill(Color.LIGHTBLUE);
    }

    // Getters and setters
    public String getValue() { return value; }
    public TreeNodeView getLeft() { return left; }
    public void setLeft(TreeNodeView left) { this.left = left; }
    public TreeNodeView getRight() { return right; }
    public void setRight(TreeNodeView right) { this.right = right; }
}