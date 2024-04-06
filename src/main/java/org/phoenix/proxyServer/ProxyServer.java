package org.phoenix.proxyServer;

import java.io.*;
import java.net.*;

public class ProxyServer {
    private static final int PORT = 8080;
    private static final String SERVER_ADDRESS = "example.com";
    private static final int SERVER_PORT = 80;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Proxy server listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle client request in a new thread
                Thread thread = new Thread(() -> handleClientRequest(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClientRequest(Socket clientSocket) {
        try {
            // Read request from client
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String request = clientReader.readLine();
            System.out.println("Received request from client: " + request);

            // Forward request to the actual server
            try (Socket serverSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                 PrintWriter serverWriter = new PrintWriter(serverSocket.getOutputStream(), true);
                 BufferedReader serverReader = new BufferedReader(
                         new InputStreamReader(serverSocket.getInputStream()));
                 PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true)) {

                serverWriter.println(request);

                // Receive response from the actual server
                String response;
                while ((response = serverReader.readLine()) != null) {
                    // Forward response to the client
                    clientWriter.println(response);
                }
            }

            // Close connections
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

