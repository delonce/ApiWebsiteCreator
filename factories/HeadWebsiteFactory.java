package main.factories;

import com.sun.net.httpserver.HttpHandler;
import main.builders.DefaultWebsiteBuilder;
import main.handlers.commonHandlers.CloseSiteHandler;
import main.handlers.commonHandlers.SetNewNameHandler;
import main.handlers.customSite.CreateCustomSiteHandler;
import main.handlers.customSite.UploadCustomSiteHandler;
import main.handlers.protectedCustomSIte.CreateProtectedCustomSiteHandler;
import main.handlers.protectedSimpleSite.CreateProtectedSimpleSiteHandler;
import main.handlers.simpleSite.CreateSimpleSiteHandler;
import main.handlers.simpleSite.SimpleSiteHandler;
import main.storages.StorageManager;
import main.website.Website;

import java.io.IOException;
import java.util.HashMap;

public class HeadWebsiteFactory implements AbstractWebsiteApplyFactory {
    @Override
    public Website createWebsite(DefaultWebsiteBuilder builder, int port) throws IOException {
        builder.createWebsite();

        builder.buildName("Godsite");
        builder.buildPort(port);
        builder.buildHttpServer();

        return builder.getWebsite();
    }

    @Override
    public StorageManager createStorageManager() {
        return null;
    }

    @Override
    public HashMap<String, HttpHandler> createHandler() {
        HashMap<String, HttpHandler> pathMap = new HashMap<>();

        pathMap.put("/home", new SimpleSiteHandler());
        pathMap.put("/api/createSimpleSite", new CreateSimpleSiteHandler());
        pathMap.put("/api/createCustomSite", new CreateCustomSiteHandler());
        pathMap.put("/api/createProtectedSimpleSite", new CreateProtectedSimpleSiteHandler());
        pathMap.put("/api/createProtectedCustomSite", new CreateProtectedCustomSiteHandler());
        pathMap.put("/api/closeSite", new CloseSiteHandler());
        pathMap.put("/api/setNewName", new SetNewNameHandler());
        pathMap.put("/api/uploadCustomSite", new UploadCustomSiteHandler());

        return pathMap;
    }
}
