package main.website;

import com.sun.net.httpserver.HttpHandler;
import main.builders.DefaultWebsiteBuilder;
import main.factories.AbstractWebsiteApplyFactory;
import main.storages.StorageManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Objects;

public class WebsiteRunner {
    private Website clientSite;
    private StorageManager clientStorage;
    private static final LinkedHashMap<Integer, WebsiteRunner> allClients = new LinkedHashMap<>();
    private static final LinkedList<Integer> currentFreePorts = new LinkedList<>();

    public void startWebsiteApply(AbstractWebsiteApplyFactory factory, DefaultWebsiteBuilder builder) throws IOException {
        int port = getFreePort();
        System.out.println("Start creating site " + port);

        clientSite = factory.createWebsite(builder, port);
        clientStorage = factory.createStorageManager();

        if (clientStorage != null) {
            openNewStorage();
        }

        HashMap<String, HttpHandler> clientHandler = factory.createHandler();
        AbstractWebsiteApplyFactory.createContext(clientSite, clientHandler);

        runHttpServer();
        allClients.put(port, this);
        System.out.println("End creating site " + port);
    }

    public static void closeSite(int port) {
        System.out.println("Starting closing site " + port);

        Website website = allClients.get(port).getClientSite();
        StorageManager storageManager = allClients.get(port).getClientStorage();

        website.getHttpServer().stop(0);
        allClients.remove(port);

        if (storageManager != null) {
            clearStorage(String.valueOf(port));
        }

        System.out.println("Site " + port + " is closed");
        currentFreePorts.add(port);
        System.out.println(currentFreePorts);
    }

    public static WebsiteRunner getClientRunner(int clientPort) {
        return WebsiteRunner.allClients.get(clientPort);
    }

    public boolean uploadStorage(String path, LinkedList<String> formatData) throws IOException {
        return clientStorage.uploadData(path, formatData);
    }

    public void changeSiteName(String newName) {
        clientSite.setName(newName);
    }
    public StorageManager getClientStorage() {
        return clientStorage;
    }

    public Website getClientSite() { return clientSite; }

    private int getFreePort() {
        if (currentFreePorts.size() == 0) {
            return 8000 + allClients.size();
        } else {
            return currentFreePorts.pollFirst();
        }
    }

    private void openNewStorage() {
        String poolName = String.valueOf(clientSite.getPort());

        clientStorage.setPoolName(poolName);
        clientStorage.createPool();
    }

    private void runHttpServer() {
        clientSite.getHttpServer().setExecutor(null);
        clientSite.getHttpServer().start();
    }

    private static void clearStorage(String poolName) {
        File poolStorage = new File(String.valueOf(poolName));

        System.out.println("Starting clearing pool " + poolName);
        for (String fileName : Objects.requireNonNull(poolStorage.list())) {
            File dirFile = new File(poolName + "/" + fileName);
            if (!dirFile.delete()) {
                System.out.println("Problem with a " + dirFile.getName());
            }
        }

        if (poolStorage.delete()) {
            System.out.println("Storage deleted successfully");
        } else {
            System.out.println("Somthing wrong of pool " + poolName);
        }
    }
}
