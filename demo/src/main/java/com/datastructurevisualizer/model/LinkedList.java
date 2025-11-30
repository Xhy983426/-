package com.datastructurevisualizer.model;

import java.io.Serializable;
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
        public String type; // "insert", "delete", "create", "move", "highlight", "complete", "pointer_change"
        public int value;
        public int position;
        public String description;
        public List<NodeState> nodeStates;
        public Node highlightedNode;
        public Node[] movingNodes;
        public Node newNode;
        public PointerOperation pointerOp; // 指针操作信息

        public OperationStep(String type, int value, int position, String description) {
            this.type = type;
            this.value = value;
            this.position = position;
            this.description = description;
            this.nodeStates = new ArrayList<>();
            this.movingNodes = new Node[0];
        }
    }
    public class PointerOperation {
        public Node fromNode;    // 指针来源节点
        public Node toNode;      // 指针指向节点
        public String operation; // 操作描述
        public boolean isNew;    // 是否是新指针

        public PointerOperation(Node fromNode, Node toNode, String operation, boolean isNew) {
            this.fromNode = fromNode;
            this.toNode = toNode;
            this.operation = operation;
            this.isNew = isNew;
        }
    }

    public class NodeState {
        public int data;
        public int index;
        public String state; // "normal", "highlight", "new", "removed", "moving"
        public Node nodeRef; // 节点引用

        public NodeState(int data, int index, String state, Node nodeRef) {
            this.data = data;
            this.index = index;
            this.state = state;
            this.nodeRef = nodeRef;
        }
    }

    public LinkedList() {
        head = null;
        size = 0;
        operationSteps = new ArrayList<>();
    }

    public static class SerializableNode implements Serializable {
        private static final long serialVersionUID = 1L;
        public int data;
        public SerializableNode next;

        public SerializableNode(Node node) {
            this.data = node.data;
            this.next = null;
        }
    }

    // 可序列化的链表版本
    public static class LinkedListSerializable implements Serializable {
        private static final long serialVersionUID = 1L;
        public SerializableNode head;
        public int size;

        public LinkedListSerializable(LinkedList list) {
            this.size = list.size;
            if (list.head != null) {
                this.head = convertToSerializable(list.head);
            }
        }

        private SerializableNode convertToSerializable(Node node) {
            if (node == null) return null;

            SerializableNode serialNode = new SerializableNode(node);
            serialNode.next = convertToSerializable(node.next);
            return serialNode;
        }
    }

    // 序列化方法
    public LinkedListSerializable toSerializable() {
        return new LinkedListSerializable(this);
    }

    // 反序列化方法
    public static LinkedList fromSerializable(LinkedListSerializable serializable) {
        LinkedList list = new LinkedList();
        if (serializable != null) {
            list.size = serializable.size;
            list.head = convertFromSerializable(serializable.head);
        }
        return list;
    }

    private static Node convertFromSerializable(SerializableNode serialNode) {
        if (serialNode == null) return null;

        LinkedList list = new LinkedList(); // 创建临时实例来创建Node
        Node node = list.new Node(serialNode.data); // 使用临时实例创建内部类实例
        node.next = convertFromSerializable(serialNode.next);
        return node;
    }

    // 存档管理方法
    public ArchiveManager.ArchiveData saveToArchive(String description) {
        return new ArchiveManager.ArchiveData("linkedlist", this.toSerializable(), description);
    }

    public static LinkedList loadFromArchive(ArchiveManager.ArchiveData archiveData) {
        if (archiveData != null && archiveData.data instanceof LinkedListSerializable) {
            return fromSerializable((LinkedListSerializable) archiveData.data);
        }
        return new LinkedList();
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

}
