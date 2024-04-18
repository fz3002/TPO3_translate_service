package com.example.ClientHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.example.Wrappers.StringWrapper;

public class ClientHandlerListener implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private StringWrapper translated;
    public boolean received;

    public ClientHandlerListener(StringWrapper tranlated, Socket socket) {
        this.socket = socket;
        this.translated = tranlated;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            translated.setValue(in.readLine());
            out.println("ACK");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
