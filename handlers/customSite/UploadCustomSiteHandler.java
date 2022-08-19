package main.handlers.customSite;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.handlers.AbstractSiteHandler;
import main.website.WebsiteRunner;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;

public class UploadCustomSiteHandler extends AbstractSiteHandler implements HttpHandler {
    private String fileName;
    private Integer requestPort;

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

        if (formatFileData == null) {
            sendSimpleError(httpExchange, 401, "invalid token");
            return;
        }

        WebsiteRunner websiteRunner = createWebRunner(requestPort);
        String path = websiteRunner.getClientStorage().getPoolName() + "/" + fileName;

        if (websiteRunner.uploadStorage(path, formatFileData)) {
            sendResponse(httpExchange, websiteRunner);
        } else {
            sendSimpleError(httpExchange, 501, "unsupported file format");
        }
    }

    protected void sendResponse(HttpExchange httpExchange, WebsiteRunner websiteRunner) throws IOException, JSONException {
        OutputStream outputStream = httpExchange.getResponseBody();

        JSONObject reply = new JSONObject();
        reply.put("address", websiteRunner.getClientSite().getServerAddress());
        reply.put("name", websiteRunner.getClientSite().getName());

        httpExchange.sendResponseHeaders(200, reply.toString().length());
        outputStream.write(reply.toString().getBytes());

        outputStream.flush();
        outputStream.close();

        httpExchange.close();
    }

    private LinkedList<String> formatFileData(String[] fileData) {
        LinkedList<String> linkedList = new LinkedList<>(Arrays.asList(fileData));

        requestPort = Integer.parseInt(linkedList.get(3).replace(" ", ""));
        try {
            fileName = getFileName(linkedList.get(5));
        } catch (Exception e) {
            fileName = getFileName(linkedList.get(9));
        }

        if (isCanBeAccepted(requestPort, linkedList)) {
            System.out.println(fileName + " " + requestPort);

            for (int i = 0; i < 7; i++) {
                linkedList.removeFirst();
            }

            linkedList.removeLast();
            return linkedList;
        } else {
            return null;
        }
    }
}
