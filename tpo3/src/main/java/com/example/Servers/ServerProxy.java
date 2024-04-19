package com.example.Servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.example.LanguageServersRepository;
import com.example.ClientHandlers.ClientHandlerProxy;
import com.example.Interfaces.Server;

public class ServerProxy implements Server {

    final static int LISTENINGPORT = 5454;

    private ServerSocket serverSocket = null;
    private List<LanguageServer> langServers = null;

    public ServerProxy(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.langServers = LanguageServersRepository.getAll();
        System.out.println(langServers);
    }

    @Override
    public void serviceConnections() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection established");
                new Thread(new ClientHandlerProxy(clientSocket, langServers), "Client Handler Thread Server Proxy").start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        ServerProxy proxy;
        try {
            proxy = new ServerProxy(new ServerSocket(LISTENINGPORT));
            proxy.serviceConnections();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public List<LanguageServer> getLangServers() {
        return this.langServers;
    }

    public void setLangServers(List<LanguageServer> langServers) {
        this.langServers = langServers;
    }
}
