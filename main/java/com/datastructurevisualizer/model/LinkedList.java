package com.datastructurevisualizer.model;

import java.util.ArrayList;
import java.util.List;

public class LinkedList {
    private Node head;
    private int size;
    private List<OperationStep> operationSteps;

    public class Node {
        public int data;
        public Node next;

        public Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    public class OperationStep {
        public String type; // "insert", "delete", "create"
        public int value;
        public int position;
        public String description;
        public List<NodeState> nodeStates;
        public Node highlightedNode;

        public OperationStep(String type, int value, int position, String description) {
            this.type = type;
            this.value = value;
            this.position = position;
            this.description = description;
            this.nodeStates = new ArrayList<>();
        }
    }

    public class NodeState {
        public int data;
        public int index;
        public String state; // "normal", "highlight", "new", "removed"

        public NodeState(int data, int index, String state) {
            this.data = data;
            this.index = index;
            this.state = state;
        }
    }

    public LinkedList() {
        head = null;
        size = 0;
        operationSteps = new ArrayList<>();
    }

    public OperationStep createList() {
        head = null;
        size = 0;
        OperationStep step = new OperationStep("create", 0, -1, "创建空链表");
        recordNodeStates(step);
        operationSteps.add(step);
        return step;
    }

    public List<OperationStep> insertWithSteps(int data, int position) {
        operationSteps.clear();

        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException("Invalid position: " + position);
        }

        Node newNode = new Node(data);

        // 步骤1: 创建新节点
        OperationStep step1 = new OperationStep("insert", data, position, "创建新节点: " + data);
        step1.highlightedNode = newNode;
        recordNodeStates(step1);
        operationSteps.add(step1);

        if (position == 0) {
            // 头部插入
            OperationStep step2 = new OperationStep("insert", data, position,
                    "将新节点插入到链表头部，原头节点后移");
            newNode.next = head;
            head = newNode;
            step2.highlightedNode = newNode;
            recordNodeStates(step2);
            operationSteps.add(step2);
        } else {
            Node current = head;

            // 遍历到插入位置的前一个节点
            for (int i = 0; i < position - 1; i++) {
                OperationStep step = new OperationStep("insert", data, position,
                        "遍历到位置 " + i + "，节点值: " + current.data);
                step.highlightedNode = current;
                recordNodeStates(step);
                operationSteps.add(step);
                current = current.next;
            }

            // 插入新节点
            OperationStep step3 = new OperationStep("insert", data, position,
                    "在位置 " + position + " 插入新节点，后续节点后移");
            newNode.next = current.next;
            current.next = newNode;
            step3.highlightedNode = newNode;
            recordNodeStates(step3);
            operationSteps.add(step3);
        }

        size++;

        // 完成插入
        OperationStep finalStep = new OperationStep("insert", data, position,
                "插入完成！链表大小: " + size);
        recordNodeStates(finalStep);
        operationSteps.add(finalStep);

        return new ArrayList<>(operationSteps);
    }

    public List<OperationStep> deleteWithSteps(int position) {
        operationSteps.clear();

        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Invalid position: " + position);
        }

        if (position == 0) {
            // 标记要删除的头部节点
            OperationStep step1 = new OperationStep("delete", head.data, position,
                    "标记要删除的头部节点: " + head.data);
            step1.highlightedNode = head;
            recordNodeStates(step1);
            operationSteps.add(step1);

            // 删除头部节点
            OperationStep step2 = new OperationStep("delete", head.data, position,
                    "删除头部节点，后续节点前移");
            head = head.next;
            recordNodeStates(step2);
            operationSteps.add(step2);
        } else {
            Node current = head;

            // 遍历到要删除节点的前一个节点
            for (int i = 0; i < position - 1; i++) {
                OperationStep step = new OperationStep("delete", 0, position,
                        "遍历到位置 " + i + "，节点值: " + current.data);
                step.highlightedNode = current;
                recordNodeStates(step);
                operationSteps.add(step);
                current = current.next;
            }

            Node nodeToDelete = current.next;

            // 标记要删除的节点
            OperationStep step2 = new OperationStep("delete", nodeToDelete.data, position,
                    "标记要删除的节点: " + nodeToDelete.data);
            step2.highlightedNode = nodeToDelete;
            recordNodeStates(step2);
            operationSteps.add(step2);

            // 执行删除
            OperationStep step3 = new OperationStep("delete", nodeToDelete.data, position,
                    "删除节点，后续节点前移");
            current.next = current.next.next;
            recordNodeStates(step3);
            operationSteps.add(step3);
        }

        size--;

        // 完成删除
        OperationStep finalStep = new OperationStep("delete", 0, position,
                "删除完成！链表大小: " + size);
        recordNodeStates(finalStep);
        operationSteps.add(finalStep);

        return new ArrayList<>(operationSteps);
    }

    private void recordNodeStates(OperationStep step) {
        step.nodeStates.clear();
        Node current = head;
        int index = 0;

        while (current != null) {
            String state = "normal";
            if (step.highlightedNode != null && current == step.highlightedNode) {
                state = "highlight";
            }
            step.nodeStates.add(new NodeState(current.data, index, state));
            current = current.next;
            index++;
        }
    }

    // 兼容性方法
    public void insert(int data, int position) {
        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException("Invalid position: " + position);
        }

        Node newNode = new Node(data);

        if (position == 0) {
            newNode.next = head;
            head = newNode;
        } else {
            Node current = head;
            for (int i = 0; i < position - 1; i++) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }

    public void delete(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Invalid position: " + position);
        }

        if (position == 0) {
            head = head.next;
        } else {
            Node current = head;
            for (int i = 0; i < position - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
        }
        size--;
    }

    public int getSize() { return size; }
    public Node getHead() { return head; }
    public boolean isEmpty() { return size == 0; }
    public List<OperationStep> getOperationSteps() { return new ArrayList<>(operationSteps); }
}