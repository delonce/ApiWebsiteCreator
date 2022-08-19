package main.handlers.customSite;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.handlers.AbstractSiteHandler;
import java.io.*;

public class CustomSiteHandler extends AbstractSiteHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Serving the request");
        String path = getHeadPoolFile(httpExchange.getLocalAddress().toString());

        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder htmlBuilder = new StringBuilder();

        String line;

        while((line = br.readLine()) != null){
            htmlBuilder.append(line);
        }

        fr.close();
        br.close();

        sendResponse(httpExchange, htmlBuilder);
    }

    private void sendResponse(HttpExchange httpExchange, StringBuilder htmlBuilder) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();

        httpExchange.sendResponseHeaders(200, htmlBuilder.length());
        outputStream.write(htmlBuilder.toString().getBytes());

        outputStream.flush();
        outputStream.close();
        httpExchange.close();
    }
}
