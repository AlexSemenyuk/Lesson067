package org.itstep;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

class ClientHTTP {
    private final String host;
    private final int port;

    public ClientHTTP (String host, int port) {
        this.host = host;
        this.port = port;
    }

    private String clientRequest(String request) throws IOException {
        StringBuilder responseBuilder = new StringBuilder();
        // tcp connection
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port));
            if (socket.isConnected()) {
                // http protocol
                try (OutputStream out = socket.getOutputStream();
                     InputStream in = socket.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                    out.write(request.getBytes()); // send request to server
                    String line;
                    while ((line = reader.readLine()) != null) { // get response
                        responseBuilder.append(line).append("\n");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseBuilder.toString();
    }

    String get(String path) throws IOException {
        String request = getRequest(path);
        System.out.println("-= Request =-");
        System.out.println(request);
        return clientRequest(request);
    }

    private String getRequest(String path) {
        return "GET %s HTTP/1.1\r\n".formatted(path) +
                "Host: %s\r\n".formatted(host) +
                "User-Agent: Java Http Client\r\n" +
                "Accepted: text/http\r\n" +
                "Connection: keep-alive\r\n" +
                "\r\n";
    }
}

