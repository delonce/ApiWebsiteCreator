package main.factories;

import com.sun.net.httpserver.HttpHandler;
import main.builders.DefaultWebsiteBuilder;
import main.storages.StorageManager;
import main.website.Website;

import java.io.IOException;
import java.util.HashMap;

public interface AbstractWebsiteApplyFactory {
    Website createWebsite(DefaultWebsiteBuilder builder, int port) throws IOException;

    StorageManager createStorageManager();

    HashMap<String, HttpHandler> createHandler();

    static void createContext(Website website, HashMap<String, HttpHandler> handler) {
        for (String key : handler.keySet()) {
            website.getHttpServer().createContext(key, handler.get(key));
        }
    }
}

