package main.builders;

import main.website.Website;

import java.io.IOException;

public interface DefaultWebsiteBuilder {

    void createWebsite();

    void buildName(String name);

    void buildPort(int port);

    void buildHttpServer() throws IOException;

    Website getWebsite();
}
