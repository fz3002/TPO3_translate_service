package com.example.Servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.LanguageDictionary;
import com.example.ClientHandlers.ClientHandlerLangServer;
import com.example.Interfaces.Server;

public class LanguageServer implements Server{
    private LanguageDictionary ld = null;
    private ServerSocket serverSocket = null;

    public LanguageServer(ServerSocket serverSocket, LanguageDictionary ld) {
        this.ld = ld;

        serviceConnections();
    }

    @Override
    public void serviceConnections() {
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Connection established");
                new Thread(new ClientHandlerLangServer(clientSocket, ld)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        
        try {
            String path = args[0];
            //TODO: validate path
            new LanguageServer(new ServerSocket(), new LanguageDictionary(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
