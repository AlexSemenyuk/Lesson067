package org.itstep;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ServerHTTP {
    private final int port;

    public ServerHTTP(int port) {
        this.port = port;
    }

    public String serverRequest() throws IOException {
        StringBuilder responseBuilder = new StringBuilder();
        // tcp connection
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Wait for connection...");

        while (true) {
            Socket client = serverSocket.accept();
            executorService.submit(() -> {
                // http protocol
                try (InputStream in = client.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                     OutputStream out = client.getOutputStream();
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
                    System.out.println("Connection is successful");
                    String line;
                    String pathRequest = "";
                    String hostRequest = "";
                    String connectionRequest = "";
                    while ((line = reader.readLine()) != null) {
//                System.err.println(line);
                        if (line.toLowerCase().contains("get")) {
                            int indexEndOfPath = line.indexOf("HTTP/1.1");
                            pathRequest = line.substring(4, indexEndOfPath - 1);
                            System.out.println("pathRequest = " + pathRequest);
                        }
                        if (line.toLowerCase().contains("host")) {
//                    int indexEndOfHost = line.indexOf("\r\n");
//                    System.out.println("indexEndOfHost = " + indexEndOfHost);
                            hostRequest = line.substring(6);
                            System.out.println("hostRequest = " + hostRequest);
                        }
                        if (line.toLowerCase().contains("connection")) {
//                    int indexEndOfCon = line.indexOf("\r\n");
                            connectionRequest = line.substring(12);
                            System.out.println("connectionRequest = " + connectionRequest);
                        }
                        if (line.equals("")) {
                            break;
                        }
                    }
                    String response = "";
                    Path path = Path.of("html/" + pathRequest);
                    String status = "";
//                    System.out.println("Files.exists(pathRequest) - " + Files.exists(Path.of(pathRequest)));
//                    System.out.println("Files.exists(path) - " + Files.exists(path));
                    if (Files.exists(path)) {
                        status = "200 ОК";
                        response = getResponse(path, status, hostRequest, connectionRequest);
                        response += getFile(path);
                    } else {
                        status = "404 Not found";
                        path = Path.of("html/404.html");
                        System.out.println("status = " + status);
                        response = getResponse(path, status, hostRequest, connectionRequest);
                        response += getFile(path);
                    }
                    System.out.println("\n-= Response =-");
                    System.out.println(response);
                    writer.write(response);
//            System.out.println("hello");
//                return responseBuilder.toString();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return responseBuilder.toString();
            });
        }
    }

    private String getResponse(Path path, String status, String host, String connection) {
        return "%s HTTP/1.1 %s\r\n".formatted(path, status) +
                "Host: %s\r\n".formatted(host) +
                "User-Agent: Java Http Client\r\n" +
                "Accepted: text/http\r\n" +
                "Connection: %s\r\n".formatted(connection) +
                "\r\n";
    }

    private String getFile(Path path) throws IOException {
        String lineFile = "";
        String line;
        try (InputStream in = new FileInputStream(String.valueOf(path));
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            while ((line = reader.readLine()) != null) {
                lineFile += (line + "\n");
            }
        }
        return lineFile;
    }
}

