// TreeArchiveManager.java
package com.datastructurevisualizer.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TreeArchiveManager {
    private static final String SAVE_DIRECTORY = "saves";
    private static final String TREE_STRUCTURE_EXTENSION = ".tree";

    public static class TreeArchiveData implements Serializable {
        private static final long serialVersionUID = 1L;
        public String treeType; // "binary", "bst", "huffman", "avl"
        public Object data;
        public String description;
        public long timestamp;

        public TreeArchiveData(String treeType, Object data, String description) {
            this.treeType = treeType;
            this.data = data;
            this.description = description;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            return String.format("%s - %s (%tF %tT)",
                    treeType, description, timestamp, timestamp);
        }
    }

    // 创建保存目录
    private static void ensureSaveDirectory() {
        File directory = new File(SAVE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // 保存树形结构
    public static boolean saveTreeStructure(TreeArchiveData archiveData, String filename) {
        try {
            ensureSaveDirectory();
            File file = new File(SAVE_DIRECTORY, filename + TREE_STRUCTURE_EXTENSION);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(archiveData);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 加载树形结构
    public static TreeArchiveData loadTreeStructure(String filename) {
        try {
            File file = new File(SAVE_DIRECTORY, filename + TREE_STRUCTURE_EXTENSION);
            if (!file.exists()) {
                return null;
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (TreeArchiveData) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 获取所有保存的树文件列表
    public static List<String> getSavedTreeFiles() {
        List<String> files = new ArrayList<>();
        ensureSaveDirectory();

        File directory = new File(SAVE_DIRECTORY);
        File[] savedFiles = directory.listFiles((dir, name) -> name.endsWith(TREE_STRUCTURE_EXTENSION));

        if (savedFiles != null) {
            for (File file : savedFiles) {
                String filename = file.getName();
                filename = filename.substring(0, filename.length() - TREE_STRUCTURE_EXTENSION.length());
                files.add(filename);
            }
        }

        return files;
    }

    // 删除保存文件
    public static boolean deleteTreeSaveFile(String filename) {
        File file = new File(SAVE_DIRECTORY, filename + TREE_STRUCTURE_EXTENSION);
        return file.exists() && file.delete();
    }
}