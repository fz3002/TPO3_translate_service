package com.example.Servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.ClientHandlers.ClientHandlerListener;
import com.example.Interfaces.Server;
import com.example.Wrappers.StringWrapper;

public class ListenerServer implements Server, Runnable {

    private ServerSocket serverSocket;
    public boolean newInput = false;
    volatile public StringWrapper received = new StringWrapper();

    public ListenerServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ListenerServer() {}

    @Override
    public void serviceConnections() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Listening Server Connection established");
                new Thread(new ClientHandlerListener(received, clientSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        serviceConnections();
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public void setSocket(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }
}
