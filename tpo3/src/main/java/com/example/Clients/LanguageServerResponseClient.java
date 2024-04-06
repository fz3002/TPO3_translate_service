package com.example.Clients;

import java.io.IOException;
import java.net.UnknownHostException;

import com.example.Interfaces.Client;

public class LanguageServerResponseClient implements Client{

    @Override
    public void connect(String host, int port) throws UnknownHostException, IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'connect'");
    }

    @Override
    public void sendMessage(String message) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendMessage'");
    }

    @Override
    public void disconnect() throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'disconnect'");
    }

}
