package main.handlers.protectedCustomSIte;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.builders.DefaultWebsiteBuilder;
import main.builders.SimpleWebsiteBuilder;
import main.factories.AbstractWebsiteApplyFactory;
import main.factories.CustomWebsiteFactory;
import main.handlers.AbstractSiteHandler;
import main.website.ProtectedWebsiteRunner;
import main.website.WebsiteRunner;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;

public class CreateProtectedCustomSiteHandler extends AbstractSiteHandler implements HttpHandler {
    private String fileName;

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

                writeInputData(httpExchange, data);
            } catch (NumberFormatException | IOException | JSONException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void writeInputData(HttpExchange httpExchange, byte[] data) throws IOException, JSONException {
        String[] arrayData = new String(data).split("\\R");
        LinkedList<String> formatFileData = formatFileData(arrayData);

        AbstractWebsiteApplyFactory factory = new CustomWebsiteFactory();
        DefaultWebsiteBuilder builder = new SimpleWebsiteBuilder();

        ProtectedWebsiteRunner websiteRunner = createProtectedWebRunner(factory, builder);

        String path = websiteRunner.getClientStorage().getPoolName() + "/" + fileName;

        if (websiteRunner.uploadStorage(path, formatFileData)) {
            sendResponse(httpExchange, websiteRunner);
        } else {
            sendSimpleError(httpExchange, 501, "unsupported file format");
            WebsiteRunner.closeSite(websiteRunner.getClientSite().getPort());
        }
    }

    protected LinkedList<String> formatFileData(String[] fileData) {
        LinkedList<String> linkedList = new LinkedList<>(Arrays.asList(fileData));
        fileName = getFileName(linkedList.get(1));

        linkedList.removeFirst();
        linkedList.removeFirst();
        linkedList.removeFirst();
        linkedList.removeLast();

        return linkedList;
    }

    protected void sendResponse(HttpExchange httpExchange, ProtectedWebsiteRunner websiteRunner) throws IOException, JSONException {
        OutputStream outputStream = httpExchange.getResponseBody();

        JSONObject reply = new JSONObject();
        reply.put("address", websiteRunner.getClientSite().getServerAddress());
        reply.put("name", websiteRunner.getClientSite().getName());
        reply.put("token", websiteRunner.getPersonalToken());

        httpExchange.sendResponseHeaders(200, reply.toString().length());
        outputStream.write(reply.toString().getBytes());

        outputStream.flush();
        outputStream.close();

        httpExchange.close();
    }
}
