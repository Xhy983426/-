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
    private static final double NODE_SPACING = 80;

    public LinearStructureView(Pane canvas) {
        this.canvas = canvas;
    }

    public void drawLinkedList(LinkedList list) {
        canvas.getChildren().clear();

        if (list == null || list.isEmpty()) return;

        LinkedList.Node current = list.getHead();
        double startX = 50;
        double startY = 100;
        int position = 0;

        while (current != null) {
            double centerX = startX + position * NODE_SPACING;
            double centerY = startY;

            NodeView nodeView = new NodeView(current.data, centerX, centerY);
            canvas.getChildren().add(nodeView);

            if (current.next != null) {
                double arrowStartX = centerX + 20;
                double arrowEndX = centerX + NODE_SPACING - 20;

                Line arrow = new Line(arrowStartX, centerY, arrowEndX, centerY);
                arrow.setStroke(Color.BLACK);
                arrow.setStrokeWidth(2);

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

    public void drawStack(Stack stack) {
        canvas.getChildren().clear();

        if (stack == null || stack.isEmpty()) return;

        double startX = 100;
        double startY = 400;
        double elementWidth = 60;
        double elementHeight = 40;
        double spacing = 5;

        Line stackBase = new Line(startX - 20, startY, startX + elementWidth + 20, startY);
        stackBase.setStroke(Color.BLACK);
        stackBase.setStrokeWidth(3);
        canvas.getChildren().add(stackBase);

        java.util.List<Integer> elements = stack.getElements();
        for (int i = 0; i < elements.size(); i++) {
            double elementY = startY - (i + 1) * (elementHeight + spacing);

            Rectangle element = new Rectangle(startX, elementY, elementWidth, elementHeight);
            element.setFill(Color.LIGHTGREEN);
            element.setStroke(Color.DARKGREEN);
            element.setStrokeWidth(2);

            Text valueText = new Text(String.valueOf(elements.get(i)));
            valueText.setX(startX + elementWidth / 2 - valueText.getLayoutBounds().getWidth() / 2);
            valueText.setY(elementY + elementHeight / 2 + valueText.getLayoutBounds().getHeight() / 4);

            canvas.getChildren().addAll(element, valueText);
        }

        if (!elements.isEmpty()) {
            double topY = startY - elements.size() * (elementHeight + spacing);
            Text topText = new Text("↑ 栈顶");
            topText.setX(startX + elementWidth + 10);
            topText.setY(topY + elementHeight / 2);
            canvas.getChildren().add(topText);
        }
    }

    public void drawArrayList(ArrayList arrayList) {
        canvas.getChildren().clear();

        if (arrayList == null || arrayList.isEmpty()) return;

        double startX = 50;
        double startY = 200;
        double elementWidth = 50;
        double elementHeight = 40;
        double spacing = 5;

        for (int i = 0; i < arrayList.size(); i++) {
            double elementX = startX + i * (elementWidth + spacing);

            Rectangle element = new Rectangle(elementX, startY, elementWidth, elementHeight);
            element.setFill(Color.LIGHTCORAL);
            element.setStroke(Color.DARKRED);
            element.setStrokeWidth(2);

            Text indexText = new Text(String.valueOf(i));
            indexText.setX(elementX + 5);
            indexText.setY(startY - 5);

            Text valueText = new Text(String.valueOf(arrayList.get(i)));
            valueText.setX(elementX + elementWidth / 2 - valueText.getLayoutBounds().getWidth() / 2);
            valueText.setY(startY + elementHeight / 2 + valueText.getLayoutBounds().getHeight() / 4);

            canvas.getChildren().addAll(element, indexText, valueText);
        }
    }
}