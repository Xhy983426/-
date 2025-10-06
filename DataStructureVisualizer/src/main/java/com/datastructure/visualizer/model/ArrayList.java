package com.datastructure.visualizer.model;
import java.util.Arrays;

public class ArrayList<T> extends LinearStructure<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] array;
    private int size;

    public ArrayList() {
        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public void insert(T data) {
        ensureCapacity();
        array[size++] = data;
        elements.add(data);
    }

    @Override
    public void delete(T data) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(data)) {
                for (int j = i; j < size - 1; j++) {
                    array[j] = array[j + 1];
                }
                array[size - 1] = null;
                size--;
                elements.remove(data);
                break;
            }
        }
    }

    @Override
    public boolean search(T data) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(data)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        array = new Object[DEFAULT_CAPACITY];
        size = 0;
        elements.clear();
    }

    @Override
    public String visualize() {
        return "Array: " + Arrays.toString(Arrays.copyOf(array, size));
    }

    @Override
    public int size() {
        return size;
    }

    private void ensureCapacity() {
        if (size == array.length) {
            array = Arrays.copyOf(array, array.length * 2);
        }
    }

    // 移除getArray()方法，因为它会导致类型问题
    // 或者修改为返回正确类型的方法
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        return (T[]) Arrays.copyOf(array, size);
    }

    // 或者添加一个获取特定位置元素的方法
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) array[index];
    }
}