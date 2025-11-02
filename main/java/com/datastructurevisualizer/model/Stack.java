package com.datastructurevisualizer.model;

import java.util.ArrayList;
import java.util.List;

public class Stack {
    private List<Integer> elements;
    private int capacity;

    public Stack(int capacity) {
        this.elements = new ArrayList<>();
        this.capacity = capacity;
    }

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

    public int peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return elements.get(elements.size() - 1);
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