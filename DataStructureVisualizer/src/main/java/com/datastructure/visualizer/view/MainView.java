package com.datastructure.visualizer.view;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class MainView {
    private BorderPane root;
    private TabPane tabPane;
    private LinearStructureView linearStructureView;
    private TreeView treeView;

    public MainView() {
        initialize();
    }

    private void initialize() {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        // 创建菜单栏
        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        // 创建选项卡面板
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        root.setCenter(tabPane);

        // 初始化各个视图
        linearStructureView = new LinearStructureView();
        treeView = new TreeView();

        // 添加选项卡
        createLinearStructuresTab();
        createTreeStructuresTab();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("文件");
        MenuItem newItem = new MenuItem("新建");
        MenuItem exportItem = new MenuItem("导出");
        MenuItem exitItem = new MenuItem("退出");
        fileMenu.getItems().addAll(newItem, new SeparatorMenuItem(), exportItem, new SeparatorMenuItem(), exitItem);

        Menu viewMenu = new Menu("视图");
        CheckMenuItem animationItem = new CheckMenuItem("启用动画");
        animationItem.setSelected(true);
        viewMenu.getItems().addAll(animationItem);

        Menu helpMenu = new Menu("帮助");
        MenuItem aboutItem = new MenuItem("关于");
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);
        return menuBar;
    }

    private void createLinearStructuresTab() {
        Tab linearTab = new Tab("线性结构");
        linearTab.setContent(linearStructureView.getView());
        tabPane.getTabs().add(linearTab);
    }

    private void createTreeStructuresTab() {
        Tab treeTab = new Tab("树形结构");
        treeTab.setContent(treeView.getView());
        tabPane.getTabs().add(treeTab);
    }

    public BorderPane getRoot() {
        return root;
    }

    public LinearStructureView getLinearStructureView() {
        return linearStructureView;
    }

    public TreeView getTreeView() {
        return treeView;
    }
}