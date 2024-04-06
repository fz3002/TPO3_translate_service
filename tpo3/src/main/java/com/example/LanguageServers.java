package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.Interfaces.Server;
import com.example.Servers.LanguageServer;

public class LanguageServers {

    private List<Server> servers = new ArrayList<Server>();

    public void addServer(String path){
        servers.add(new LanguageServer(new LanguageDictionary(path)));
    }

    public List<Server> getServers(){
        return servers;
    }
}
