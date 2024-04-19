package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.Servers.LanguageServer;

public class LanguageServersRepository {

    private static List<LanguageServer> servers = new ArrayList<>();

    private LanguageServersRepository() {
    }

    public static synchronized List<LanguageServer> getAll(){
        return servers;
    }

    public static synchronized void addServer(LanguageServer server){
        servers.add(server);
    }

    public static synchronized void removeServer(LanguageServer server){
        servers.remove(server);
    }

    public static synchronized LanguageServer getServer(String language){
        for (LanguageServer server : servers){
            if (server.getLanguageDictionaryLanguage() == language){
                return server;
            }
        }
        return null;
    }
}
