// Stack.java
package com.datastructurevisualizer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Stack implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Integer> elements;
    private int capacity;

    // 可序列化的栈版本
    public static class SerializableData implements Serializable {
        private static final long serialVersionUID = 1L;
        public List<Integer> elements;
        public int capacity;

        public SerializableData(Stack stack) {
            this.elements = stack.elements != null ? new ArrayList<>(stack.elements) : new ArrayList<>();
            this.capacity = stack.capacity;
        }
    }

    public Stack(int capacity) {
        this.capacity = capacity;
        this.elements = new ArrayList<>();
    }

    // 默认构造函数
    public Stack() {
        this(10); // 默认容量为10
    }

    // 序列化方法
    public SerializableData getSerializableData() {
        return new SerializableData(this);
    }

    // 反序列化方法
    public static Stack fromSerializableData(SerializableData serializable) {
        if (serializable == null) {
            return new Stack(10); // 默认容量
        }

        Stack stack = new Stack(serializable.capacity);
        stack.elements = new ArrayList<>(serializable.elements);
        return stack;
    }

    // 存档管理方法
    public ArchiveManager.ArchiveData saveToArchive(String description) {
        return new ArchiveManager.ArchiveData("stack", this.getSerializableData(), description);
    }

    public static Stack loadFromArchive(ArchiveManager.ArchiveData archiveData) {
        if (archiveData != null && archiveData.data instanceof SerializableData) {
            return fromSerializableData((SerializableData) archiveData.data);
        }
        return new Stack(10); // 默认容量
    }

    // 原有的操作方法保持不变...
    public void push(int element) {
        if (isFull()) {
            throw new IllegalStateException("Stack is full");
        }
        elements.add(element);
    }

    public int pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return elements.remove(elements.size() - 1);
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public boolean isFull() {
        return elements.size() >= capacity;
    }

    public int size() {
        return elements.size();
    }

    public List<Integer> getElements() {
        return new ArrayList<>(elements);
    }

    public int getCapacity() {
        return capacity;
    }
}