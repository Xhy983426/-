package com.datastructure.visualizer;

import com.datastructure.visualizer.view.MainView;
import javafx.scene.layout.BorderPane;

public class MainController {
    private BorderPane root;
    private MainView view;

    public MainController() {
        this.view = new MainView();
        this.root = view.getRoot();
    }

    public BorderPane getView() {
        return root;
    }
}