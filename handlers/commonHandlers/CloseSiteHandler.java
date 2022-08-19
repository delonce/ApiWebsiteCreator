package main.handlers.commonHandlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.handlers.AbstractSiteHandler;
import main.website.WebsiteRunner;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class CloseSiteHandler extends AbstractSiteHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) {
        System.out.println("Serving the request");

        if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {
            try {
                Headers requestHeaders = httpExchange.getRequestHeaders();
                int contentLength = Integer.parseInt(requestHeaders.getFirst("Content-length"));
                InputStream inputStream = httpExchange.getRequestBody();

                byte[] data = new byte[contentLength];
                inputStream.read(data);
                inputStream.close();
                String jsonData = new String(data);

                int requestPort = (int) (getJSONParam(jsonData, "port"));
                WebsiteRunner websiteRunner = WebsiteRunner.getClientRunner(requestPort);
                System.out.println(websiteRunner.getClientSite().getServerAddress());

                if (isCanBeAccepted(websiteRunner, jsonData)){
                    WebsiteRunner.closeSite(requestPort);
                    sendResponse(httpExchange, contentLength);
                } else {
                    sendSimpleError(httpExchange, 401, "invalid token");
                }
            } catch (NumberFormatException | IOException e) {
                System.out.println(e.getMessage());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void sendResponse(HttpExchange httpExchange, int contentLength) throws IOException {
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, contentLength);
        httpExchange.close();
    }
}