package com.example.Servers;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.example.LanguageDictionary;
import com.example.ClientHandlers.ClientHandlerProxy;
import com.example.Interfaces.Server;

public class ServerProxy implements Server {

    private ServerSocket serverSocket = null;
    private List<LanguageServer> langServers = new ArrayList<LanguageServer>();
    final static int LISTENINGPORT = 5454;
    private final String PATH_TO_DATA = System.getProperty("user.dir") + "/data";
    private static int currentLangServerPort = 2137;

    public ServerProxy(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void serviceConnections() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection established");
                new Thread(new ClientHandlerProxy(clientSocket, langServers), "Client Handler Thread Server Proxy")
                        .start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<LanguageServer> getLangServers() {
        return this.langServers;
    }

    public void setLangServers(List<LanguageServer> langServers) {
        this.langServers = langServers;
    }

    private void createDefaultLanguageServers() throws IOException {
        File dataDir = new File(PATH_TO_DATA);
        System.out.println(dataDir.getAbsolutePath());
        File[] files = dataDir.listFiles();
        for (File file : files) {
            langServers.add(new LanguageServer(new ServerSocket(currentLangServerPort++),
                    new LanguageDictionary(file.getAbsolutePath())));
        }
        for (LanguageServer languageServer : langServers) {
            new Thread(languageServer, "Language Server " + languageServer.getLanguageDictionaryLanguage()).start();
        }
    }

    public static int getCurrentLangServerPort() {
        return currentLangServerPort;
    }

    public static void incrementCurrentLangServerPort(){
        currentLangServerPort++;
    }

    public static void main(String args[]) {
        ServerProxy proxy;
        try {
            proxy = new ServerProxy(new ServerSocket(LISTENINGPORT));
            
            // Starts Default Language Servers
            proxy.createDefaultLanguageServers();

            // Passes working default Language Servers to main server
            proxy.setLangServers(proxy.langServers);

            // Servicing requests from clients
            proxy.serviceConnections();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
