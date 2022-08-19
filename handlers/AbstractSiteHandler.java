package main.handlers;

import com.sun.net.httpserver.HttpExchange;
import main.builders.DefaultWebsiteBuilder;
import main.factories.AbstractWebsiteApplyFactory;
import main.storages.StorageManager;
import main.website.ProtectedWebsiteRunner;
import main.website.WebsiteRunner;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract public class AbstractSiteHandler {
    public WebsiteRunner createWebRunner(AbstractWebsiteApplyFactory factory, DefaultWebsiteBuilder builder) throws IOException {
        WebsiteRunner websiteRunner = new WebsiteRunner();
        websiteRunner.startWebsiteApply(factory, builder);

        return websiteRunner;
    }

    public ProtectedWebsiteRunner createProtectedWebRunner(AbstractWebsiteApplyFactory factory, DefaultWebsiteBuilder builder) throws IOException {
        ProtectedWebsiteRunner websiteRunner = new ProtectedWebsiteRunner();
        websiteRunner.startWebsiteApply(factory, builder);

        return websiteRunner;
    }

    public static void sendSimpleError(HttpExchange httpExchange, int errorCode, String reason) throws IOException, JSONException {
        OutputStream outputStream = httpExchange.getResponseBody();

        JSONObject reply = new JSONObject();
        reply.put("reason", reason);

        httpExchange.sendResponseHeaders(errorCode, reply.toString().length());
        outputStream.write(reply.toString().getBytes());

        outputStream.flush();
        outputStream.close();

        httpExchange.close();
    }

    public WebsiteRunner createWebRunner(int requestPort) {
        return WebsiteRunner.getClientRunner(requestPort);
    }

    public String getFileName(String line) {
        Pattern pattern = Pattern.compile("filename=.+\"");
        Matcher matcher = pattern.matcher(line);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            result.append(line, matcher.start(), matcher.end());
        }

        return result.toString().split("=")[1].replace("\"", "");
    }

    private StorageManager findClientStorage(String address) {
        Pattern pattern = Pattern.compile(":\\d+");
        Matcher matcher = pattern.matcher(address);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            result.append(address, matcher.start(), matcher.end());
        }

        result = new StringBuilder(result.toString().replace(":", ""));
        int requestPort = Integer.parseInt(result.toString());

        return WebsiteRunner.getClientRunner(requestPort).getClientStorage();
    }

    public Object getJSONParam(String jsonData, String key) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);
        return jsonObject.get(key);
    }

    public boolean isCanBeAccepted(WebsiteRunner websiteRunner, String jsonData) {
        if (websiteRunner.getClass() == WebsiteRunner.class) {
            return true;
        } else {
            int token;

            try {
                token = (int) getJSONParam(jsonData, "token");
            } catch (Exception e)
            {
                return false;
            }

            return ((ProtectedWebsiteRunner) websiteRunner).checkInputToken(token);
        }
    }

    public boolean isCanBeAccepted(int port, LinkedList<String> inputTextData) {
        WebsiteRunner websiteRunner = WebsiteRunner.getClientRunner(port);

        if (websiteRunner.getClass() == WebsiteRunner.class) {
            return true;
        } else {
            int token;

            try {
                token = Integer.parseInt(inputTextData.get(7).replace(" ", ""));
            } catch (Exception e)
            {
                return false;
            }

            return ((ProtectedWebsiteRunner) websiteRunner).checkInputToken(token);
        }
    }


    public String getHeadPoolFile(String address) {
        return findClientStorage(address).getHeadPoolFile();
    }

    public String getPoolName(String address) {
        return findClientStorage(address).getPoolName();
    }
}
