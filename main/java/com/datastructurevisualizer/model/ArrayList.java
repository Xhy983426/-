package com.datastructurevisualizer.model;


import java.util.List;

public class ArrayList {
    private int[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    private List<OperationStep> operationSteps;

    public class OperationStep {
        public String type; // "insert", "delete", "create", "move", "check", "prepare", "mark", "complete"
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

    public ArrayList() {
        elements = new int[DEFAULT_CAPACITY];
        size = 0;
        operationSteps = new java.util.ArrayList<OperationStep>();
    }

    // 带步骤演示的插入方法
    public List<OperationStep> insertWithSteps(int index, int element) {
        operationSteps.clear();

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // 步骤1: 检查容量
        OperationStep step1 = new OperationStep("check", element, index,
                "检查数组容量，当前大小: " + size + ", 容量: " + elements.length);
        operationSteps.add(step1);

        ensureCapacity();


        // 如果是第一个元素插入，直接插入
        if (size == 0) {
            // 步骤2: 直接插入第一个元素
            OperationStep insertStep = new OperationStep("insert", element, index,
                    "插入第一个元素: " + element);
            insertStep
                    .highlightedIndex = index;
            elements
                    [index] = element;
            size
                    ++;
            insertStep
                    .arrayState = toArray();
            operationSteps
                    .add(insertStep);

            // 步骤3: 完成插入
            OperationStep finalStep = new OperationStep("complete", element, index,
                    "插入完成！新数组大小: " + size);
            finalStep
                    .arrayState = toArray();
            operationSteps
                    .add(finalStep);

            return new java.util.ArrayList<OperationStep>(operationSteps);
        }

        // 步骤2: 创建插入位置空间
        if (index < size) {
            OperationStep step2 = new OperationStep("prepare", element, index,
                    "从位置 " + index + " 开始，后续元素需要向后移动");
            step2.highlightedIndex = index;
            operationSteps.add(step2);

            // 步骤3: 逐步移动元素
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

        // 步骤4: 插入新元素
        OperationStep insertStep = new OperationStep("insert", element, index,
                "在位置 " + index + " 插入新元素: " + element);
        insertStep.highlightedIndex = index;
        elements[index] = element;
        insertStep.arrayState = toArray();
        operationSteps.add(insertStep);

        size++;

        // 步骤5: 完成插入
        OperationStep finalStep = new OperationStep("complete", element, index,
                "插入完成！新数组大小: " + size);
        finalStep.arrayState = toArray();
        operationSteps.add(finalStep);

        return new java.util.ArrayList<OperationStep>(operationSteps);
    }

    // 带步骤演示的删除方法
    public List<OperationStep> deleteWithSteps(int index) {
        operationSteps.clear();

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        int deletedValue = elements[index];

        // 步骤1: 标记要删除的元素
        OperationStep step1 = new OperationStep("mark", deletedValue, index,
                "标记要删除位置 " + index + " 的元素: " + deletedValue);
        step1.highlightedIndex = index;
        operationSteps.add(step1);

        // 步骤2: 逐步前移元素
        for (int i = index; i < size - 1; i++) {
            OperationStep moveStep = new OperationStep("move", elements[i + 1], i,
                    "移动元素 " + elements[i + 1] + " 从位置 " + (i + 1) + " 到位置 " + i);
            moveStep.highlightedIndex = i + 1;
            moveStep.movingIndexes = new int[]{i + 1, i};
            elements[i] = elements[i + 1];
            moveStep.arrayState = toArray();
            operationSteps.add(moveStep);
        }

        // 步骤3: 更新大小
        size--;

        // 步骤4: 完成删除
        OperationStep finalStep = new OperationStep("complete", deletedValue, index,
                "删除完成！删除元素: " + deletedValue + ", 新数组大小: " + size);
        finalStep.arrayState = toArray();
        operationSteps.add(finalStep);

        return new java.util.ArrayList<OperationStep>(operationSteps);
    }

    // 原有的简单方法（保持兼容性）
    public void add(int element) {
        ensureCapacity();
        elements[size++] = element;
    }

    public void insert(int index, int element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity();
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        size--;
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
        int[] result = new int[size];
        System.arraycopy(elements, 0, result, 0, size);
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
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }
}