package com.example.ClientHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.example.LanguageDictionary;
import com.example.Clients.LanguageServerResponseClient;

public class ClientHandlerLangServer implements Runnable {

    private Socket socket = null;
    private LanguageDictionary ld = null;
    private BufferedReader in;
    private PrintWriter out;
    private String[] req;

    public ClientHandlerLangServer(Socket socket, LanguageDictionary ld) {
        this.socket = socket;
        this.ld = ld;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String mesReceived = in.readLine();
            if (!mesReceived.startsWith("{") && !mesReceived.endsWith("}")) {
                out.println("Message formating error");
            } else {
                mesReceived = mesReceived.substring(1, mesReceived.length() - 1);
                req = mesReceived.split(",");
                String translated = ld.get(req[0]);
                LanguageServerResponseClient client = new LanguageServerResponseClient();
                client.connect(req[1], Integer.parseInt(req[2]));
                client.sendMessage(translated);
                client.disconnect();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
