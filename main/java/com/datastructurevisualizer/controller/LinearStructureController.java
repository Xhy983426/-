package com.datastructurevisualizer.controller;

import com.datastructurevisualizer.model.ArrayList;
import com.datastructurevisualizer.model.LinkedList;
import com.datastructurevisualizer.model.Stack;
import com.datastructurevisualizer.view.components.LinearStructureView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class LinearStructureController implements Initializable {

    @FXML private Pane listCanvas;
    @FXML private Pane stackCanvas;
    @FXML private Pane arrayCanvas;

    // 链表控件
    @FXML private TextField listValueField;
    @FXML private TextField listIndexField;
    @FXML private Button createListBtn;
    @FXML private Button insertListBtn;
    @FXML private Button deleteListBtn;
    @FXML private TextArea listOutput;

    // 栈控件
    @FXML private TextField stackValueField;
    @FXML private TextField stackCapacityField;
    @FXML private Button createStackBtn;
    @FXML private Button pushStackBtn;
    @FXML private Button popStackBtn;
    @FXML private TextArea stackOutput;

    // 顺序表控件
    @FXML private TextField arrayValueField;
    @FXML private TextField arrayIndexField;
    @FXML private Button createArrayBtn;
    @FXML private Button insertArrayBtn;
    @FXML private Button deleteArrayBtn;
    @FXML private TextArea arrayOutput;

    private LinkedList linkedList;
    private Stack stack;
    private ArrayList arrayList;
    private LinearStructureView listView;
    private LinearStructureView stackView;
    private LinearStructureView arrayView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView = new LinearStructureView(listCanvas);
        stackView = new LinearStructureView(stackCanvas);
        arrayView = new LinearStructureView(arrayCanvas);
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        // 链表事件处理
        createListBtn.setOnAction(e -> createLinkedList());
        insertListBtn.setOnAction(e -> insertListNode());
        deleteListBtn.setOnAction(e -> deleteListNode());

        // 栈事件处理
        createStackBtn.setOnAction(e -> createStack());
        pushStackBtn.setOnAction(e -> pushStack());
        popStackBtn.setOnAction(e -> popStack());

        // 顺序表事件处理
        createArrayBtn.setOnAction(e -> createArrayList());
        insertArrayBtn.setOnAction(e -> insertArrayElement());
        deleteArrayBtn.setOnAction(e -> deleteArrayElement());
    }

    // 链表操作
    private void createLinkedList() {
        linkedList = new LinkedList();
        listOutput.setText("链表创建成功！");
        listView.drawLinkedList(linkedList);
    }

    private void insertListNode() {
        try {
            int value = Integer.parseInt(listValueField.getText());
            String indexText = listIndexField.getText();
            int index = indexText.isEmpty() ? linkedList.getSize() : Integer.parseInt(indexText);

            linkedList.insert(value, index);
            listOutput.setText("在位置 " + index + " 插入节点: " + value + "\n当前链表大小: " + linkedList.getSize());
            listView.drawLinkedList(linkedList);

            // 清空输入框
            listValueField.clear();
            listIndexField.clear();
        } catch (Exception e) {
            listOutput.setText("错误: " + e.getMessage());
        }
    }

    private void deleteListNode() {
        try {
            String indexText = listIndexField.getText();
            if (indexText.isEmpty()) {
                listOutput.setText("错误: 请输入要删除的位置");
                return;
            }
            int index = Integer.parseInt(indexText);
            linkedList.delete(index);
            listOutput.setText("删除位置 " + index + " 的节点\n当前链表大小: " + linkedList.getSize());
            listView.drawLinkedList(linkedList);

            // 清空输入框
            listIndexField.clear();
        } catch (Exception e) {
            listOutput.setText("错误: " + e.getMessage());
        }
    }

    // 栈操作
    private void createStack() {
        try {
            int capacity = stackCapacityField.getText().isEmpty() ? 10 : Integer.parseInt(stackCapacityField.getText());
            stack = new Stack(capacity);
            stackOutput.setText("栈创建成功！容量: " + capacity);
            stackView.drawStack(stack);
        } catch (Exception e) {
            stackOutput.setText("错误: " + e.getMessage());
        }
    }

    private void pushStack() {
        try {
            int value = Integer.parseInt(stackValueField.getText());
            stack.push(value);
            stackOutput.setText("入栈: " + value + "\n当前栈大小: " + stack.size());
            stackView.drawStack(stack);

            // 清空输入框
            stackValueField.clear();
        } catch (Exception e) {
            stackOutput.setText("错误: " + e.getMessage());
        }
    }

    private void popStack() {
        try {
            int value = stack.pop();
            stackOutput.setText("出栈: " + value + "\n当前栈大小: " + stack.size());
            stackView.drawStack(stack);
        } catch (Exception e) {
            stackOutput.setText("错误: " + e.getMessage());
        }
    }

    // 顺序表操作
    private void createArrayList() {
        arrayList = new ArrayList();
        arrayOutput.setText("顺序表创建成功！");
        arrayView.drawArrayList(arrayList);
    }

    private void insertArrayElement() {
        try {
            int value = Integer.parseInt(arrayValueField.getText());
            String indexText = arrayIndexField.getText();
            int index = indexText.isEmpty() ? arrayList.size() : Integer.parseInt(indexText);

            arrayList.insert(index, value);
            arrayOutput.setText("在位置 " + index + " 插入元素: " + value + "\n当前顺序表大小: " + arrayList.size());
            arrayView.drawArrayList(arrayList);

            // 清空输入框
            arrayValueField.clear();
            arrayIndexField.clear();
        } catch (Exception e) {
            arrayOutput.setText("错误: " + e.getMessage());
        }
    }

    private void deleteArrayElement() {
        try {
            String indexText = arrayIndexField.getText();
            if (indexText.isEmpty()) {
                arrayOutput.setText("错误: 请输入要删除的位置");
                return;
            }
            int index = Integer.parseInt(indexText);
            arrayList.remove(index);
            arrayOutput.setText("删除位置 " + index + " 的元素\n当前顺序表大小: " + arrayList.size());
            arrayView.drawArrayList(arrayList);

            // 清空输入框
            arrayIndexField.clear();
        } catch (Exception e) {
            arrayOutput.setText("错误: " + e.getMessage());
        }
    }
}