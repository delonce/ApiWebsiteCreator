package main.storages;

import java.io.IOException;
import java.util.LinkedList;

abstract public class StorageManager {
    private String poolName;
    private String headPoolFile;

    public String getHeadPoolFile() {
        return headPoolFile;
    }

    public void setHeadPoolFile(String headFilePath) {
        this.headPoolFile = headFilePath;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getPoolName() {
        return poolName;
    }

    abstract public void createPool();

    abstract public boolean uploadData(String path, LinkedList<String> formatFileData) throws IOException;
}
