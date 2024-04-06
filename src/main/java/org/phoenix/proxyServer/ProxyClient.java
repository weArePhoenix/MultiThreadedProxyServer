package org.phoenix.proxyServer;

import java.io.*;
import java.net.*;

public class ProxyClient {
    private static final String PROXY_ADDRESS = "localhost";
    private static final int PROXY_PORT = 8080;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(PROXY_ADDRESS, PROXY_PORT);
            System.out.println("Connected to proxy server");

            // Send request to proxy server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("GET / HTTP/1.1");
            out.println("Host: example.com");
            out.println(); // End of request

            // Receive response from proxy server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            // Close connections
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
