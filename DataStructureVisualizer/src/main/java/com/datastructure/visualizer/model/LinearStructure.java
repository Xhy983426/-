package com.datastructure.visualizer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class LinearStructure<T> implements DataStructure<T> {
    protected List<T> elements;

    public LinearStructure() {
        this.elements = new ArrayList<>();
    }

    public List<T> getElements() {
        return Collections.unmodifiableList(elements);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }
}