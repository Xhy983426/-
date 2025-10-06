package com.datastructure.visualizer.controller;


import com.datastructure.visualizer.model.*;
import com.datastructure.visualizer.view.LinearStructureView;
import com.datastructure.visualizer.view.components.NodeView;
import com.datastructure.visualizer.view.components.AnimationUtils;
import javafx.scene.control.Alert;
import java.util.List;

public class LinearStructureController {
    private LinearStructureView view;
    private LinearStructure<String> currentStructure;

    public LinearStructureController(LinearStructureView view) {
        this.view = view;
        this.currentStructure = new LinkedList<>();
        setupEventHandlers();
        updateView();
    }

    private void setupEventHandlers() {
        // 插入操作
        view.getInsertBtn().setOnAction(e -> {
            String value = view.getInputField().getText().trim();
            if (!value.isEmpty()) {
                insert(value);
                view.getInputField().clear();
            }
        });

        // 删除操作
        view.getDeleteBtn().setOnAction(e -> {
            String value = view.getInputField().getText().trim();
            if (!value.isEmpty()) {
                delete(value);
                view.getInputField().clear();
            }
        });

        // 查找操作
        view.getSearchBtn().setOnAction(e -> {
            String value = view.getInputField().getText().trim();
            if (!value.isEmpty()) {
                search(value);
                view.getInputField().clear();
            }
        });

        // 清空操作
        view.getClearBtn().setOnAction(e -> clear());

        // 入栈操作
        view.getPushBtn().setOnAction(e -> {
            String value = view.getInputField().getText().trim();
            if (!value.isEmpty()) {
                push(value);
                view.getInputField().clear();
            }
        });

        // 出栈操作
        view.getPopBtn().setOnAction(e -> {
            pop();
        });

        // 结构类型切换
        view.getStructureType().setOnAction(e -> changeStructure(view.getStructureType().getValue()));

        // 回车键插入/入栈
        view.getInputField().setOnAction(e -> {
            String value = view.getInputField().getText().trim();
            if (!value.isEmpty()) {
                if (view.getStructureType().getValue().equals("栈")) {
                    push(value);
                } else {
                    insert(value);
                }
                view.getInputField().clear();
            }
        });
    }

    private void insert(String value) {
        currentStructure.insert(value);
        updateView();
        view.logOperation("插入: " + value + " -> " + currentStructure.visualize());

        // 高亮新插入的节点
        List<NodeView> nodes = createNodeViews();
        if (!nodes.isEmpty()) {
            NodeView lastNode = nodes.get(nodes.size() - 1);
            AnimationUtils.animateInsert(lastNode);
            AnimationUtils.pulseNode(lastNode);
        }
    }

    private void push(String value) {
        if (currentStructure instanceof Stack) {
            Stack<String> stack = (Stack<String>) currentStructure;
            stack.push(value);
            updateView();
            view.logOperation("入栈: " + value + " -> " + currentStructure.visualize());

            // 高亮新入栈的节点
            List<NodeView> nodes = createNodeViews();
            if (!nodes.isEmpty()) {
                NodeView topNode = nodes.get(nodes.size() - 1);
                AnimationUtils.animateInsert(topNode);
                AnimationUtils.pulseNode(topNode);
            }
        }
    }

    private void pop() {
        if (currentStructure instanceof Stack) {
            Stack<String> stack = (Stack<String>) currentStructure;
            if (stack.isEmpty()) {
                view.logOperation("出栈失败: 栈为空");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("出栈操作");
                alert.setHeaderText(null);
                alert.setContentText("栈为空，无法执行出栈操作!");
                alert.show();
                return;
            }

            String poppedValue = stack.pop();
            updateView();
            view.logOperation("出栈: " + poppedValue + " -> " + currentStructure.visualize());

            // 显示出栈的值
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("出栈操作");
            alert.setHeaderText(null);
            alert.setContentText("出栈元素: " + poppedValue);
            alert.show();
        }
    }

    private void delete(String value) {
        boolean existed = currentStructure.search(value);
        currentStructure.delete(value);
        updateView();

        if (existed) {
            view.logOperation("删除: " + value + " -> " + currentStructure.visualize());
        } else {
            view.logOperation("删除失败: " + value + " 不存在");
        }
    }

    private void search(String value) {
        boolean found = currentStructure.search(value);
        List<NodeView> nodes = createNodeViews();

        // 高亮找到的节点
        for (NodeView node : nodes) {
            if (node.getValue().equals(value)) {
                node.markFound();
                AnimationUtils.pulseNode(node);
                break;
            }
        }

        view.logOperation("查找: " + value + " -> " + (found ? "找到" : "未找到"));

        Alert alert = new Alert(found ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING);
        alert.setTitle("查找结果");
        alert.setHeaderText(null);
        alert.setContentText("数值 " + value + " " + (found ? "找到!" : "未找到!"));
        alert.show();
    }

    private void clear() {
        currentStructure.clear();
        updateView();
        view.clearLog();
        view.logOperation("清空数据结构");
    }

    private void changeStructure(String structureType) {
        switch (structureType) {
            case "链表":
                currentStructure = new LinkedList<>();
                view.toggleStackButtons(false);
                break;
            case "顺序表":
                currentStructure = new ArrayList<>();
                view.toggleStackButtons(false);
                break;
            case "栈":
                currentStructure = new Stack<>();
                view.toggleStackButtons(true);
                break;
        }
        updateView();
        view.clearLog();
        view.logOperation("切换到: " + structureType);
    }

    private void updateView() {
        List<NodeView> nodes = createNodeViews();
        view.updateVisualization(nodes, view.getStructureType().getValue());

        String statusText = "当前结构: " + view.getStructureType().getValue() +
                " | 元素数量: " + currentStructure.size() +
                " | 状态: " + (currentStructure.isEmpty() ? "空" : "非空");

        if (currentStructure instanceof Stack) {
            Stack<String> stack = (Stack<String>) currentStructure;
            String topValue = stack.peek();
            if (topValue != null) {
                statusText += " | 栈顶: " + topValue;
            }
        }

        view.setInfoText(statusText);
    }

    private List<NodeView> createNodeViews() {
        java.util.List<NodeView> nodes = new java.util.ArrayList<>();
        for (String element : currentStructure.getElements()) {
            nodes.add(new NodeView(element));
        }
        return nodes;
    }
}