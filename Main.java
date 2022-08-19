package main;

import main.builders.DefaultWebsiteBuilder;
import main.builders.SimpleWebsiteBuilder;
import main.factories.AbstractWebsiteApplyFactory;
import main.factories.HeadWebsiteFactory;

import main.website.ProtectedWebsiteRunner;
import java.io.*;


public class Main {
    public static void main(String[] args) throws IOException {
        AbstractWebsiteApplyFactory factory = new HeadWebsiteFactory();
        DefaultWebsiteBuilder builder = new SimpleWebsiteBuilder();

        ProtectedWebsiteRunner websiteRunner = new ProtectedWebsiteRunner();
        websiteRunner.startWebsiteApply(factory, builder);
    }
}
