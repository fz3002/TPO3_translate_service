package com.example.Servers;

/**
 * Hello world!
 *
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.ClientHandlers.ClientHandlerProxy;

public class ServerProxy {

    private ServerSocket serverSocket = null;


    public ServerProxy(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

        serviceConnections();
    }
    
    public void serviceConnections() {
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Connection established");
                new Thread(new ClientHandlerProxy(clientSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
