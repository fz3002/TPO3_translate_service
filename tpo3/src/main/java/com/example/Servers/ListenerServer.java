package com.example.Servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.ClientHandlers.ClientHandlerListener;
import com.example.Interfaces.Server;
import com.example.Wrappers.StringWrapper;

public class ListenerServer implements Server, Runnable {

    volatile private ServerSocket serverSocket;
    public boolean newInput = false;
    volatile public StringWrapper received = new StringWrapper();

    public ListenerServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ListenerServer() {
    }

    @Override
    public void serviceConnections() {
        while (true) {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Listening Server Connection established");
                    ClientHandlerListener clientHandler = new ClientHandlerListener(received, clientSocket);
                    try {
                        String received = clientHandler.in.readLine();
                        clientHandler.translated.setValue(received);
                        clientHandler.out.println("ACK");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        serviceConnections();
    }

    public synchronized ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public synchronized void setSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
}
