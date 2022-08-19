package main.website;

import com.sun.net.httpserver.HttpServer;

public class Website {
    private HttpServer httpServer;
    private String name;
    private int port;

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public void setHttpServer(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    public String getServerAddress() {
        return httpServer.getAddress().toString();
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Website{" +
                "httpServer=" + httpServer +
                ", name='" + name + '\'' +
                ", port=" + port +
                '}';
    }
}
