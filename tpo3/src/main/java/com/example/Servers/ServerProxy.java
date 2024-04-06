package com.example.Servers;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.ClientHandlers.ClientHandlerProxy;
import com.example.Interfaces.Server;

public class ServerProxy implements Server{

    private ServerSocket serverSocket = null;
    private static final int PORT = 1122;


    public ServerProxy(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

        serviceConnections();
    }
    
    @Override
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

    public static void main(String[] args) {
        try{
            new ServerProxy(new ServerSocket(PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
