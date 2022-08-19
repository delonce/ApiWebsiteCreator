package main.factories;

import com.sun.net.httpserver.HttpHandler;
import main.builders.DefaultWebsiteBuilder;
import main.handlers.commonHandlers.LinkToFileHandler;
import main.handlers.customSite.CustomSiteHandler;
import main.storages.CustomSiteStorage;
import main.storages.StorageManager;
import main.website.Website;

import java.io.IOException;
import java.util.HashMap;

public class CustomWebsiteFactory implements AbstractWebsiteApplyFactory {
    public Website createWebsite(DefaultWebsiteBuilder builder, int port) throws IOException {
        builder.createWebsite();

        builder.buildName("Custom Site");
        builder.buildPort(port);
        builder.buildHttpServer();

        return builder.getWebsite();
    }


    public StorageManager createStorageManager() {
        return new CustomSiteStorage();
    }


    public HashMap<String, HttpHandler> createHandler() {
        HashMap<String, HttpHandler> pathMap = new HashMap<>();

        pathMap.put("/home", new CustomSiteHandler());
        pathMap.put("/", new LinkToFileHandler());

        return pathMap;
    }

}
