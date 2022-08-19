package main.builders;

import com.sun.net.httpserver.HttpServer;
import main.website.Website;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SimpleWebsiteBuilder implements DefaultWebsiteBuilder {
    private Website website;

    @Override
    public void createWebsite() {
        website = new Website();
    }

    @Override
    public void buildName(String name) {
        website.setName(name);
    }

    @Override
    public void buildPort(int port) {
        website.setPort(port);
    }

    @Override
    public void buildHttpServer() throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", website.getPort()), 0);
        website.setHttpServer(httpServer);
    }

    @Override
    public Website getWebsite() {
        return website;
    }
}
