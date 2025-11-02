package com.datastructurevisualizer.model;

public interface LinearStructure extends DataStructure {
    void insert(int element, int position);
    void delete(int position);
}