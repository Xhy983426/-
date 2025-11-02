package com.datastructurevisualizer.view.components;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationUtils {

    public static void highlightNode(Node node) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), node);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);

        scaleTransition.play();
    }

    public static void fadeIn(Node node) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), node);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    public static void slideIn(Node node, double fromX, double toX) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(400), node);
        translateTransition.setFromX(fromX);
        translateTransition.setToX(toX);
        translateTransition.play();
    }
}