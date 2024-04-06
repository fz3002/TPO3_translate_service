package com.example.ClientHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
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
            String mesReceived = in.readLine();
            if(!mesReceived.startsWith("{") && !mesReceived.endsWith("}")){ //TODO: Change validation method
                out.println("Message formating error");
            }else {
                mesReceived = mesReceived.substring(-1, mesReceived.length() - 1);
                req = mesReceived.split(",");
                String translated = ld.getData().get(req[0]);
                LanguageServerResponseClient client = new LanguageServerResponseClient();
                client.connect(InetAddress.getLocalHost().getHostAddress(), Integer.parseInt(req[1]));
                client.sendMessage(translated);
                client.disconnect();
            }
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
