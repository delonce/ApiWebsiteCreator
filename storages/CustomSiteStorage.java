package main.storages;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class CustomSiteStorage extends StorageManager {

    @Override
    public void createPool() {
        File poolDir = new File(getPoolName());
        if (!poolDir.mkdir()) {
            System.out.println("Creating pool error " + poolDir.getName());
        }
    }

    @Override
    public boolean uploadData(String path, LinkedList<String> formatFileData) throws IOException {
        if (checkFileType(path)) {
            if (getHeadPoolFile() == null) {
                setHeadPoolFile(path);
            }

            FileWriter writer = new FileWriter(path, true);

            for (String line : formatFileData) {
                writer.write(line + "\n");
            }

            writer.flush();
            writer.close();

            return true;
        } else {
            return false;
        }
    }

    public boolean checkFileType(String path) {
        String fileType = path.split("\\.")[1];

        return fileType.equals("html") || fileType.equals("css");
    }
}
