package com.datastructurevisualizer.view.components;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class NodeView extends StackPane {
    private static final double NODE_RADIUS = 20;
    private Circle circle;
    private Text text;

    public NodeView(int value, double x, double y) {
        circle = new Circle(NODE_RADIUS);
        circle.setFill(Color.LIGHTBLUE);
        circle.setStroke(Color.BLUE);
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
        circle.setFill(Color.YELLOW);
    }

    public void unhighlight() {
        circle.setFill(Color.LIGHTBLUE);
    }
}