package com.datastructure.visualizer.view.components;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationUtils {

    public static void animateInsert(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(300), node);
        st.setFromX(0.1);
        st.setFromY(0.1);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    public static void animateDelete(Node node, Runnable onFinished) {
        ScaleTransition st = new ScaleTransition(Duration.millis(300), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(0.1);
        st.setToY(0.1);
        st.setOnFinished(e -> onFinished.run());
        st.play();
    }

    public static void pulseNode(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
    }
}