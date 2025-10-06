package com.datastructure.visualizer.model;

public class LinkedList<T> extends LinearStructure<T> {
    public static class Node<T> {
        public T data;
        public Node<T> next;

        public Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;

    public LinkedList() {
        this.head = null;
    }

    @Override
    public void insert(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        elements.add(data);
    }

    @Override
    public void delete(T data) {
        if (head == null) return;

        if (head.data.equals(data)) {
            head = head.next;
            elements.remove(data);
            return;
        }

        Node<T> current = head;
        while (current.next != null && !current.next.data.equals(data)) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
            elements.remove(data);
        }
    }

    @Override
    public boolean search(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public void clear() {
        head = null;
        elements.clear();
    }

    @Override
    public String visualize() {
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;
        while (current != null) {
            sb.append("[").append(current.data).append("] -> ");
            current = current.next;
        }
        sb.append("null");
        return sb.toString();
    }

    public Node<T> getHead() {
        return head;
    }
}