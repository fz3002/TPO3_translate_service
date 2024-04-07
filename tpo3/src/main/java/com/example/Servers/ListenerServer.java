package com.example.Servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.ClientHandlers.ClientHandlerListener;
import com.example.Interfaces.Server;
import com.example.Wrappers.StringWrapper;

public class ListenerServer implements Server {

    private ServerSocket serverSocket;
    public boolean newInput = false;
    public StringWrapper received;

    public ListenerServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

        serviceConnections();
    }

    @Override
    public void serviceConnections() {
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Connection established");
                new Thread(new ClientHandlerListener(received, clientSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
