package com.datastructurevisualizer.controller;

import com.datastructurevisualizer.model.ArrayList;
import com.datastructurevisualizer.model.LinkedList;
import com.datastructurevisualizer.model.Stack;
import com.datastructurevisualizer.view.components.LinearStructureView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;

import java.util.List;
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

    // 顺序表步骤导航控件
    @FXML private Button prevArrayStepBtn;
    @FXML private Button nextArrayStepBtn;
    @FXML private Button arrayAutoDemoBtn;
    @FXML private Label arrayStepInfoLabel;

    private LinkedList linkedList;
    private Stack stack;
    private ArrayList arrayList;
    private LinearStructureView listView;
    private LinearStructureView stackView;
    private LinearStructureView arrayView;

    // 顺序表步骤演示相关字段
    private List<ArrayList.OperationStep> currentArraySteps;
    private int currentArrayStepIndex;
    private Timeline arrayAnimation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView = new LinearStructureView(listCanvas);
        stackView = new LinearStructureView(stackCanvas);
        arrayView = new LinearStructureView(arrayCanvas);

        // 初始化顺序表步骤演示
        currentArraySteps = new java.util.ArrayList<ArrayList.OperationStep>();
        currentArrayStepIndex = 0;

        setupEventHandlers();
        updateArrayStepNavigation();
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

        // 顺序表步骤导航事件处理
        prevArrayStepBtn.setOnAction(e -> previousArrayStep());
        nextArrayStepBtn.setOnAction(e -> nextArrayStep());
        arrayAutoDemoBtn.setOnAction(e -> startArrayAutoDemo());
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
    // 在创建顺序表时确保正确初始化
    private void createArrayList() {
        arrayList = new ArrayList();
        arrayOutput.setText("顺序表创建成功！");
        // 确保绘制空数组状态
        arrayView.drawArrayList(arrayList);
        resetArraySteps();
    }

    // 在插入第一个元素时确保正确显示
    private void insertArrayElement() {
        try {
            int value = Integer.parseInt(arrayValueField.getText());
            String indexText = arrayIndexField.getText();
            int index = indexText.isEmpty() ? arrayList.size() : Integer.parseInt(indexText);

            // 使用带步骤的插入方法
            currentArraySteps = arrayList.insertWithSteps(index, value);
            currentArrayStepIndex = 0;

            if (!currentArraySteps.isEmpty()) {
                showArrayStep(currentArrayStepIndex);
                arrayOutput.setText("开始插入演示... 使用导航按钮查看详细步骤");
            } else {
                // 如果没有步骤（可能是直接插入），直接绘制当前状态
                arrayView.drawArrayList(arrayList);
                arrayOutput.setText("插入完成！当前数组大小: " + arrayList.size());
            }

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

            // 使用带步骤的删除方法
            currentArraySteps = arrayList.deleteWithSteps(index);
            currentArrayStepIndex = 0;

            if (!currentArraySteps.isEmpty()) {
                showArrayStep(currentArrayStepIndex);
                arrayOutput.setText("开始删除演示... 使用导航按钮查看详细步骤");
            }

            // 清空输入框
            arrayIndexField.clear();
        } catch (Exception e) {
            arrayOutput.setText("错误: " + e.getMessage());
        }
    }

    // 顺序表步骤导航方法
    private void showArrayStep(int stepIndex) {
        if (currentArraySteps == null || currentArraySteps.isEmpty()) {
            return;
        }

        if (stepIndex < 0) stepIndex = 0;
        if (stepIndex >= currentArraySteps.size()) stepIndex = currentArraySteps.size() - 1;

        currentArrayStepIndex = stepIndex;
        ArrayList.OperationStep step = currentArraySteps.get(stepIndex);

        // 绘制当前步骤
        arrayView.drawArrayListWithSteps(arrayList, step);

        // 更新步骤导航
        updateArrayStepNavigation();

        // 更新输出信息
        arrayOutput.setText("步骤 " + (stepIndex + 1) + "/" + currentArraySteps.size() +
                "\n" + step.description);
    }

    private void previousArrayStep() {
        if (currentArrayStepIndex > 0) {
            showArrayStep(currentArrayStepIndex - 1);
        }
    }

    private void nextArrayStep() {
        if (currentArrayStepIndex < currentArraySteps.size() - 1) {
            showArrayStep(currentArrayStepIndex + 1);
        }
    }

    private void startArrayAutoDemo() {
        if (currentArraySteps == null || currentArraySteps.isEmpty()) {
            arrayOutput.setText("请先执行插入或删除操作");
            return;
        }

        if (arrayAnimation != null) {
            arrayAnimation.stop();
        }

        currentArrayStepIndex = 0;
        arrayAnimation = new Timeline();

        // 为每个步骤创建关键帧
        for (int i = 0; i < currentArraySteps.size(); i++) {
            final int stepIndex = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(i * 1.0), // 每秒一个步骤
                    e -> showArrayStep(stepIndex)
            );
            arrayAnimation.getKeyFrames().add(keyFrame);
        }

        // 添加完成后的延迟
        KeyFrame finalFrame = new KeyFrame(
                Duration.seconds(currentArraySteps.size() * 1.0 + 1),
                e -> {
                    arrayOutput.setText("自动演示完成！使用导航按钮重新查看步骤");
                }
        );
        arrayAnimation.getKeyFrames().add(finalFrame);

        arrayAnimation.setCycleCount(1);
        arrayAnimation.play();

        arrayOutput.setText("自动演示中...");
    }

    private void resetArraySteps() {
        if (arrayAnimation != null) {
            arrayAnimation.stop();
        }
        currentArraySteps.clear();
        currentArrayStepIndex = 0;
        updateArrayStepNavigation();
    }

    private void updateArrayStepNavigation() {
        if (currentArraySteps == null || currentArraySteps.isEmpty()) {
            arrayStepInfoLabel.setText("步骤: 0/0");
            prevArrayStepBtn.setDisable(true);
            nextArrayStepBtn.setDisable(true);
            arrayAutoDemoBtn.setDisable(true);
        } else {
            arrayStepInfoLabel.setText("步骤: " + (currentArrayStepIndex + 1) + "/" + currentArraySteps.size());
            prevArrayStepBtn.setDisable(currentArrayStepIndex == 0);
            nextArrayStepBtn.setDisable(currentArrayStepIndex == currentArraySteps.size() - 1);
            arrayAutoDemoBtn.setDisable(false);
        }
    }

}
