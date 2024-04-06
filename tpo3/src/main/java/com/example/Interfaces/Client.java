package com.example.Interfaces;

import java.io.IOException;
import java.net.UnknownHostException;

public interface Client {
    public void connect(String host, int port) throws UnknownHostException, IOException;
    public void sendMessage(String message) throws IOException;
    public void disconnect() throws IOException;
}
