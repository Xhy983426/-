package com.datastructurevisualizer.view.components;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class TreeNodeView extends StackPane {
    private static final double NODE_RADIUS = 18;
    private Circle circle;
    private Text text;

    public TreeNodeView(int value, double x, double y, Color color) {
        circle = new Circle(NODE_RADIUS);
        circle.setFill(color);
        circle.setStroke(Color.DARKBLUE);
        circle.setStrokeWidth(2);

        text = new Text(String.valueOf(value));
        text.setStyle("-fx-font-weight: bold;");

        getChildren().addAll(circle, text);
        setLayoutX(x - NODE_RADIUS);
        setLayoutY(y - NODE_RADIUS);
    }

    public void setPosition(double x, double y) {
        setLayoutX(x - NODE_RADIUS);
        setLayoutY(y - NODE_RADIUS);
    }

    public void highlight() {
        circle.setFill(Color.GOLD);
    }

    public void unhighlight() {
        circle.setFill(Color.LIGHTGREEN);
    }
}