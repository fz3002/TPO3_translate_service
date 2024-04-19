package com.example.Servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.LanguageDictionary;
import com.example.ClientHandlers.ClientHandlerLangServer;
import com.example.Interfaces.Server;

public class LanguageServer implements Server, Runnable {
    private LanguageDictionary ld = null;
    private ServerSocket serverSocket = null;

    public LanguageServer(ServerSocket serverSocket, LanguageDictionary ld) {
        this.serverSocket = serverSocket;
        this.ld = ld;
    }

    @Override
    public void serviceConnections() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Language Connection established");
                new Thread(new ClientHandlerLangServer(clientSocket, ld)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        serviceConnections();
    }

    public String getLanguageDictionaryLanguage() {
        return ld.languageCode;
    }

    public int getListeningPort() {
        return serverSocket.getLocalPort();
    }
}
