package main.handlers.commonHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.handlers.AbstractSiteHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

public class LinkToFileHandler extends AbstractSiteHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = getPoolName(httpExchange.getLocalAddress().toString()) + httpExchange.getRequestURI();

        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder htmlBuilder = new StringBuilder();

        String line;

        while((line = br.readLine()) != null){
            htmlBuilder.append(line);
        }

        fr.close();
        br.close();

        sendPesponse(httpExchange, htmlBuilder);
    }

    private void sendPesponse(HttpExchange httpExchange, StringBuilder htmlBuilder) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();

        httpExchange.sendResponseHeaders(200, htmlBuilder.length());
        outputStream.write(htmlBuilder.toString().getBytes());

        outputStream.flush();
        outputStream.close();
        httpExchange.close();
    }
}
