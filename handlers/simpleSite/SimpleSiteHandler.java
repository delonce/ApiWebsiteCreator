package main.handlers.simpleSite;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.website.Website;
import main.website.WebsiteRunner;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleSiteHandler implements HttpHandler {
    private String helloLine = "";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if("GET".equals(httpExchange.getRequestMethod())) {
            InetSocketAddress localAddress = httpExchange.getLocalAddress();
            System.out.println("New connection on " + localAddress);

            helloLine = setHelloLine(localAddress.toString());
        }

        sendResponse(httpExchange);
    }

    private String setHelloLine(String address) {
        Pattern pattern = Pattern.compile(":\\d+");
        Matcher matcher = pattern.matcher(address);
        StringBuilder port = new StringBuilder();

        while (matcher.find()) {
            port.append(address, matcher.start(), matcher.end());
        }

        port = new StringBuilder(port.toString().replace(":", ""));
        Website website = WebsiteRunner.getClientRunner(Integer.parseInt(port.toString().trim())).getClientSite();

        return website.getName();
    }

    private void sendResponse(HttpExchange httpExchange) throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.
                append("<h1>").append("Hello from Simple Server! My Name is ").append(helloLine)
                .append("</h1>");

        httpExchange.sendResponseHeaders(200, htmlBuilder.length());
        outputStream.write(htmlBuilder.toString().getBytes());

        outputStream.flush();
        outputStream.close();
    }
}
