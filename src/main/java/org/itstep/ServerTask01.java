package org.itstep;

import java.io.IOException;

public class ServerTask01 {
    public static void main(String[] args) throws IOException {
        ServerHTTP serverHTTP = new ServerHTTP(80);
        serverHTTP.serverRequest();
    }
}
