package org.phoenix.proxiserver;
import java.io.*;
import java.net.*;
public class ProxyServer {
    public void start(int port) {
        try {
            // Create a ServerSocket to listen for incoming connections
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Proxy server started on port " + port);

            while (true) {
                // Accept incoming client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Create a new thread to handle client request
                new ProxyThread(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
