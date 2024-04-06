package org.phoenix.proxyServer;


import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class MainServer {
    private static Set<PrintWriter> clients = new HashSet<>();

    public static void main(String[] args) {
        try {
            try (ServerSocket serverSocket = new ServerSocket(12345)) {
                System.out.println("Main server started. Waiting for clients...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket);
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    clients.add(out);

                    Thread clientThread = new Thread(new ClientHandler(clientSocket));
                    clientThread.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received from client: " + inputLine);
                    broadcast(inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void broadcast(String message) {
        for (PrintWriter client : clients) {
            client.println(message);
        }
    }
}
