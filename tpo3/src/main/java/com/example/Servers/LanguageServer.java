package com.example.Servers;

import com.example.LanguageDictionary;
import com.example.Interfaces.Server;

public class LanguageServer implements Server{
    private LanguageDictionary ld = null;

    public LanguageServer(LanguageDictionary ld) {
        this.ld = ld;
    }

    @Override
    public void serviceConnections() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'serviceConnections'");
    }
}
