package com.example.Servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.example.ClientHandlers.ClientHandlerProxy;
import com.example.Interfaces.Server;

public class ServerProxy implements Server, Runnable {

    private ServerSocket serverSocket = null;
    private List<LanguageServer> langServers;

    public ServerProxy(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void serviceConnections() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection established");
                new Thread(new ClientHandlerProxy(clientSocket, langServers)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        serviceConnections();
    }

    public List<LanguageServer> getLangServers() {
        return this.langServers;
    }

    public void setLangServers(List<LanguageServer> langServers) {
        this.langServers = langServers;
    }
}
