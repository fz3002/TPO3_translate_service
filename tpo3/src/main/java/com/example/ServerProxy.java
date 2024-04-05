package com.example;

/**
 * Hello world!
 *
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerProxy {

    private ServerSocket serverSocket = null;


    public ServerProxy(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    
    public void serviceConnections() {
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Connection established");
                new Thread(new ClientHandler(clientSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
