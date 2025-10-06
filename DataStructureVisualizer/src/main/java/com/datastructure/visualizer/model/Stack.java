package com.datastructure.visualizer.model;

public class Stack<T> extends LinearStructure<T> {
    private java.util.Stack<T> stack;

    public Stack() {
        this.stack = new java.util.Stack<>();
    }

    public void push(T data) {
        stack.push(data);
        elements.add(data);
    }

    public T pop() {
        if (!stack.isEmpty()) {
            T data = stack.pop();
            elements.remove(data);
            return data;
        }
        return null;
    }

    public T peek() {
        return stack.isEmpty() ? null : stack.peek();
    }

    // 栈的插入就是入栈
    @Override
    public void insert(T data) {
        push(data);
    }

    // 栈的删除需要特殊处理，通常只删除栈顶
    @Override
    public void delete(T data) {
        // 对于栈，我们通常只从顶部删除
        // 这里实现为删除指定元素（如果存在）
        if (stack.remove(data)) {
            elements.remove(data);
        }
    }

    @Override
    public boolean search(T data) {
        return stack.contains(data);
    }

    @Override
    public void clear() {
        stack.clear();
        elements.clear();
    }

    @Override
    public String visualize() {
        return "Stack (Top): " + stack.toString();
    }

    @Override
    public int size() {
        return stack.size();
    }

    // 栈特有方法
    public boolean isFull() {
        // 理论上栈可以动态增长，这里返回false
        return false;
    }
}