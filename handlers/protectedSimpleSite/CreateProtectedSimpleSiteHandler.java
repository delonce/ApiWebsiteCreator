package main.handlers.protectedSimpleSite;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.builders.DefaultWebsiteBuilder;
import main.builders.SimpleWebsiteBuilder;
import main.factories.AbstractWebsiteApplyFactory;
import main.factories.SimpleWebsiteFactory;
import main.handlers.AbstractSiteHandler;
import main.website.ProtectedWebsiteRunner;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class CreateProtectedSimpleSiteHandler extends AbstractSiteHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equalsIgnoreCase("GET")) {
            try {
                AbstractWebsiteApplyFactory factory = new SimpleWebsiteFactory();
                DefaultWebsiteBuilder builder = new SimpleWebsiteBuilder();

                ProtectedWebsiteRunner websiteRunner = createProtectedWebRunner(factory, builder);

                OutputStream outputStream = httpExchange.getResponseBody();

                JSONObject reply = new JSONObject();
                reply.put("address", websiteRunner.getClientSite().getServerAddress());
                reply.put("name", websiteRunner.getClientSite().getName());
                reply.put("token", websiteRunner.getPersonalToken());

                httpExchange.sendResponseHeaders(200, reply.toString().length());
                outputStream.write(reply.toString().getBytes());

                outputStream.flush();
                httpExchange.close();
                outputStream.close();
            } catch (NumberFormatException | IOException | JSONException e) {
                System.out.println(e.getMessage());
            }
        } else {
            Headers requestHeaders = httpExchange.getRequestHeaders();

            int contentLength = Integer.parseInt(requestHeaders.getFirst("Content-length"));
            System.out.println(""+requestHeaders.getFirst("Content-length"));

            InputStream is = httpExchange.getRequestBody();

            byte[] data = new byte[contentLength];
            is.read(data);

            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, contentLength);

            System.out.println(new String(data));
            try {
                JSONObject jsonObject = new JSONObject(
                        new String(data)
                );

                System.out.println(jsonObject.get("port"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


        }
    }
}
