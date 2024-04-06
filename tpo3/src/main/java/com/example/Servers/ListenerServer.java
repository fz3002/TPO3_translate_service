package com.example.Servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.ClientHandlers.ClientHandlerListener;
import com.example.Interfaces.Server;

public class ListenerServer implements Server {

    private ServerSocket serverSocket;

    public ListenerServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

        serviceConnections();
    }

    @Override
    public void serviceConnections() {
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Connection established");
                new Thread(new ClientHandlerListener(clientSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
