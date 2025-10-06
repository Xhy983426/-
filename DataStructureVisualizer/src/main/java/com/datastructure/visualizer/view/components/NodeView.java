package com.datastructure.visualizer.view.components;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

public class NodeView extends StackPane {
    private Circle circle;
    private Text text;
    private String value;

    public NodeView(String value) {
        this(value, 25, Color.LIGHTBLUE);
    }

    public NodeView(String value, double radius, Color color) {
        this.value = value;
        initialize(radius, color);
    }

    private void initialize(double radius, Color color) {
        circle = new Circle(radius);
        circle.setFill(color);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        text = new Text(value);
        text.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        getChildren().addAll(circle, text);

        // 悬停效果
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

    public String getValue() {
        return value;
    }
}