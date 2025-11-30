package com.datastructurevisualizer.model;

import java.io.Serializable;

import java.util.List;

public class ArrayList implements Serializable {
    private static final long serialVersionUID = 1L;

    private int[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    private transient List<OperationStep> operationSteps;

    public class OperationStep implements Serializable {
        private static final long serialVersionUID = 1L;
        public String type;
        public int value;
        public int position;
        public String description;
        public int[] arrayState;
        public int highlightedIndex = -1;
        public int[] movingIndexes;

        public OperationStep(String type, int value, int position, String description) {
            this.type = type;
            this.value = value;
            this.position = position;
            this.description = description;
            this.arrayState = toArray();
            this.movingIndexes = new int[0];
        }
    }

    // 可序列化的ArrayList版本
    public static class ArrayListSerializable implements Serializable {
        private static final long serialVersionUID = 1L;
        public int[] elements;
        public int size;

        public ArrayListSerializable(ArrayList list) {
            this.elements = list.elements != null ? list.elements.clone() : new int[0];
            this.size = list.size;
        }
    }

    public ArrayList() {
        elements = new int[DEFAULT_CAPACITY];
        size = 0;
        operationSteps = new java.util.ArrayList<OperationStep>();
    }

    // 序列化方法
    // 在 ArrayList 类中添加以下方法

    // 序列化方法
    public ArrayListSerializable toSerializable() {
        return new ArrayListSerializable(this);
    }

    // 反序列化方法
    public static ArrayList fromSerializable(ArrayListSerializable serializable) {
        ArrayList list = new ArrayList();
        if (serializable != null && serializable.elements != null) {
            list.elements = serializable.elements.clone();
            list.size = serializable.size;
        }
        return list;
    }

    // 存档管理方法
    public ArchiveManager.ArchiveData saveToArchive(String description) {
        return new ArchiveManager.ArchiveData("array", this.toSerializable(), description);
    }

    public static ArrayList loadFromArchive(ArchiveManager.ArchiveData archiveData) {
        if (archiveData != null && archiveData.data instanceof ArrayListSerializable) {
            return fromSerializable((ArrayListSerializable) archiveData.data);
        }
        return new ArrayList();
    }

    // 原有的操作方法保持不变
    public java.util.ArrayList<OperationStep> insertWithSteps(int index, int element) {
        operationSteps.clear();

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // 步骤1: 检查容量
        OperationStep step1 = new OperationStep("check", element, index,
                "检查数组容量，当前大小: " + size + ", 容量: " + elements.length);
        operationSteps.add(step1);

        ensureCapacity();

        if (size == 0) {
            OperationStep insertStep = new OperationStep("insert", element, index,
                    "插入第一个元素: " + element);
            insertStep.highlightedIndex = index;
            elements[index] = element;
            size++;
            insertStep.arrayState = toArray();
            operationSteps.add(insertStep);

            OperationStep finalStep = new OperationStep("complete", element, index,
                    "插入完成！新数组大小: " + size);
            finalStep.arrayState = toArray();
            operationSteps.add(finalStep);

            return new java.util.ArrayList<OperationStep>(operationSteps);
        }

        if (index < size) {
            OperationStep step2 = new OperationStep("prepare", element, index,
                    "从位置 " + index + " 开始，后续元素需要向后移动");
            step2.highlightedIndex = index;
            step2.arrayState = toArray();
            operationSteps.add(step2);

            for (int i = size - 1; i >= index; i--) {
                OperationStep moveStep = new OperationStep("move", elements[i], i + 1,
                        "移动元素 " + elements[i] + " 从位置 " + i + " 到位置 " + (i + 1));
                moveStep.highlightedIndex = i;
                moveStep.movingIndexes = new int[]{i, i + 1};
                elements[i + 1] = elements[i];
                moveStep.arrayState = toArray();
                operationSteps.add(moveStep);
            }
        }

        OperationStep insertStep = new OperationStep("insert", element, index,
                "在位置 " + index + " 插入新元素: " + element);
        insertStep.highlightedIndex = index;
        elements[index] = element;
        size++;
        insertStep.arrayState = toArray();
        operationSteps.add(insertStep);

        OperationStep finalStep = new OperationStep("complete", element, index,
                "插入完成！新数组大小: " + size);
        finalStep.arrayState = toArray();
        operationSteps.add(finalStep);

        return new java.util.ArrayList<OperationStep>(operationSteps);
    }

    public List<OperationStep> deleteWithSteps(int index) {
        operationSteps.clear();

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        int deletedValue = elements[index];

        OperationStep step1 = new OperationStep("mark", deletedValue, index,
                "标记要删除位置 " + index + " 的元素: " + deletedValue);
        step1.highlightedIndex = index;
        operationSteps.add(step1);

        for (int i = index; i < size - 1; i++) {
            OperationStep moveStep = new OperationStep("move", elements[i + 1], i,
                    "移动元素 " + elements[i + 1] + " 从位置 " + (i + 1) + " 到位置 " + i);
            moveStep.highlightedIndex = i + 1;
            moveStep.movingIndexes = new int[]{i + 1, i};
            elements[i] = elements[i + 1];
            moveStep.arrayState = toArray();
            operationSteps.add(moveStep);
        }

        size--;

        OperationStep finalStep = new OperationStep("complete", deletedValue, index,
                "删除完成！删除元素: " + deletedValue + ", 新数组大小: " + size);
        finalStep.arrayState = toArray();
        operationSteps.add(finalStep);

        return new java.util.ArrayList<OperationStep>(operationSteps);
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return elements[index];
    }

    public int size() {
        return size;
    }

    public int[] toArray() {
        int actualSize = 0;
        for (int i = 0; i < elements.length; i++) {
            if (i < size || elements[i] != 0) {
                actualSize = i + 1;
            }
        }
        int[] result = new int[actualSize];
        System.arraycopy(elements, 0, result, 0, actualSize);
        return result;
    }

    public int getCapacity() {
        return elements.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public List<OperationStep> getOperationSteps() {
        return new java.util.ArrayList<OperationStep>(operationSteps);
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            int[] newElements = new int[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }
}