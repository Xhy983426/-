package com.datastructurevisualizer.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private BorderPane mainBorderPane;
    @FXML private TabPane mainTabPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 主控制器主要负责布局管理
        // 具体的功能由各个子控制器处理
    }
}