package com.example.ClientHandlers;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandlerListener implements Runnable{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String translated;
    public boolean received;

    public ClientHandlerListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // TODO Find a way to pass translated message or change to one threaded server
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

    public String getTranslated(){
        return translated;
    }
    
}
