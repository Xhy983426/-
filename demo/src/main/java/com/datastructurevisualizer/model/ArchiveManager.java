// ArchiveManager.java
package com.datastructurevisualizer.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArchiveManager {
    private static final String SAVE_DIRECTORY = "saves";
    private static final String LINEAR_STRUCTURE_EXTENSION = ".linear";

    public static class ArchiveData implements Serializable {
        private static final long serialVersionUID = 1L;
        public String structureType; // "array", "linkedlist", "stack"
        public Object data;          // 改回 Object，使用运行时类型检查
        public String description;
        public long timestamp;

        public ArchiveData(String structureType, Object data, String description) {
            this.structureType = structureType;
            this.data = data;
            this.description = description;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            return String.format("%s - %s (%tF %tT)",
                    structureType, description, timestamp, timestamp);
        }
    }

    // 创建保存目录
    private static void ensureSaveDirectory() {
        File directory = new File(SAVE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // 保存线性结构
    public static boolean saveLinearStructure(ArchiveData archiveData, String filename) {
        try {
            ensureSaveDirectory();
            File file = new File(SAVE_DIRECTORY, filename + LINEAR_STRUCTURE_EXTENSION);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(archiveData);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 加载线性结构
    public static ArchiveData loadLinearStructure(String filename) {
        try {
            File file = new File(SAVE_DIRECTORY, filename + LINEAR_STRUCTURE_EXTENSION);
            if (!file.exists()) {
                return null;
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (ArchiveData) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 获取所有保存文件列表
    public static List<String> getSavedFiles() {
        List<String> files = new ArrayList<>();
        ensureSaveDirectory();

        File directory = new File(SAVE_DIRECTORY);
        File[] savedFiles = directory.listFiles((dir, name) -> name.endsWith(LINEAR_STRUCTURE_EXTENSION));

        if (savedFiles != null) {
            for (File file : savedFiles) {
                String filename = file.getName();
                filename = filename.substring(0, filename.length() - LINEAR_STRUCTURE_EXTENSION.length());
                files.add(filename);
            }
        }

        return files;
    }

    // 删除保存文件
    public static boolean deleteSaveFile(String filename) {
        File file = new File(SAVE_DIRECTORY, filename + LINEAR_STRUCTURE_EXTENSION);
        return file.exists() && file.delete();
    }
}