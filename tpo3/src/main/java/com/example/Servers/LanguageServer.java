package com.example.Servers;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.LanguageDictionary;
import com.example.LanguageServersRepository;
import com.example.ClientHandlers.ClientHandlerLangServer;
import com.example.Interfaces.Server;

public class LanguageServer implements Server {
    private static final String PATH_TO_DATA = System.getProperty("user.dir") + "/data";
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

    public static void main(String args[]) {
        String language = args[0], filePath = null;
        int port = Integer.parseInt(args[1]);
        LanguageServer langServer;
        File dataDir = new File(PATH_TO_DATA);
        File[] files = dataDir.listFiles();
        for (File file : files) {
            if (file.getName().startsWith(language)) {
                filePath = file.getAbsolutePath();
                break;
            }
        }
        try {
            // TODO: ADD checking if server for this language exists
            if (filePath != null) {
                langServer = new LanguageServer(new ServerSocket(port), new LanguageDictionary(filePath));
                LanguageServersRepository.addServer(langServer);
                System.out.println(LanguageServersRepository.getAll().size());
                langServer.serviceConnections();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getLanguageDictionaryLanguage() {
        return ld.languageCode;
    }

    public int getListeningPort() {
        return serverSocket.getLocalPort();
    }
}
