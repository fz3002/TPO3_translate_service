package com.example.Clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.Interfaces.Client;

public class LanguageServerResponseClient implements Client {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    @Override
    public void connect(String host, int port) throws UnknownHostException, IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connected to " + socket.getInetAddress());
    }

    @Override
    public void sendMessage(String message) throws IOException {
        out.println(message);
        String response = in.readLine();
    }

    @Override
    public void disconnect() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

}
