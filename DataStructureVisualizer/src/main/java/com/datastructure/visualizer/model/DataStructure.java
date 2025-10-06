package com.datastructure.visualizer.model;

public interface DataStructure<T> {
    void insert(T data);
    void delete(T data);
    boolean search(T data);
    void clear();
    String visualize();
    int size();
    boolean isEmpty();
}