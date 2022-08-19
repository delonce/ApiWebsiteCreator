package main.factories;

import com.sun.net.httpserver.HttpHandler;
import main.builders.DefaultWebsiteBuilder;
import main.handlers.simpleSite.SimpleSiteHandler;
import main.storages.StorageManager;
import main.website.Website;

import java.io.IOException;
import java.util.HashMap;

public class SimpleWebsiteFactory implements AbstractWebsiteApplyFactory {


    public Website createWebsite(DefaultWebsiteBuilder builder, int port) throws IOException {
        builder.createWebsite();

        builder.buildName("Default Name Site");
        builder.buildPort(port);
        builder.buildHttpServer();

        return builder.getWebsite();
    }


    public StorageManager createStorageManager() {
        return null;
    }


    public HashMap<String, HttpHandler> createHandler() {
        HashMap<String, HttpHandler> pathMap = new HashMap<>();

        pathMap.put("/home", new SimpleSiteHandler());

        return pathMap;
    }
}
