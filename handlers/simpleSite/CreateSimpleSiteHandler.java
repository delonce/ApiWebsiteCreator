package main.handlers.simpleSite;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.builders.DefaultWebsiteBuilder;
import main.builders.SimpleWebsiteBuilder;
import main.handlers.AbstractSiteHandler;
import main.website.WebsiteRunner;
import main.factories.AbstractWebsiteApplyFactory;
import main.factories.SimpleWebsiteFactory;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;

public class CreateSimpleSiteHandler extends AbstractSiteHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) {
        if (httpExchange.getRequestMethod().equalsIgnoreCase("GET")) {
            try {
                AbstractWebsiteApplyFactory factory = new SimpleWebsiteFactory();
                DefaultWebsiteBuilder builder = new SimpleWebsiteBuilder();

                WebsiteRunner websiteRunner = createWebRunner(factory, builder);

                JSONObject reply = new JSONObject();
                reply.put("address", websiteRunner.getClientSite().getServerAddress());
                reply.put("name", websiteRunner.getClientSite().getName());

                sendResponse(httpExchange, reply);
            } catch (NumberFormatException | IOException | JSONException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void sendResponse(HttpExchange httpExchange, JSONObject reply) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();

        httpExchange.sendResponseHeaders(200, reply.toString().length());
        outputStream.write(reply.toString().getBytes());

        outputStream.flush();
        httpExchange.close();
        outputStream.close();
    }
}
