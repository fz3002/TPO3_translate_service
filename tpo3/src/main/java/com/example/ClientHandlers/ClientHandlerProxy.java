package com.example.ClientHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import com.example.Clients.ProxyServerRequestClient;
import com.example.Servers.LanguageServer;

public class ClientHandlerProxy implements Runnable {

    private Socket clientSocket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String[] reqReceived;
    private List<LanguageServer> languages;

    public ClientHandlerProxy(Socket clientSocket, List<LanguageServer> languages) {
        this.clientSocket = clientSocket;
        this.languages = languages;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            try{
                clientSocket.close();
            }catch (Exception e1){ }
        }
        
    }

    @Override
    public void run() {
        System.out.println("Client Handler Started");
        try {
            for (String messageReceived; (messageReceived = in.readLine()) != null; ){
                System.out.println(messageReceived);
                if (!messageReceived.startsWith("{") && !messageReceived.endsWith("}")) {
                    out.println("Message formating error");
                    clientSocket.close();
                } else {
                    messageReceived = messageReceived.substring(1, messageReceived.length() - 1);
                    reqReceived = messageReceived.split(",");
                    System.out.println(reqReceived[1]);
                    LanguageServer receivingServer = findLanguageServer(reqReceived[1]);
                    if (receivingServer != null) {
                        System.out.println("test");
                        ProxyServerRequestClient client = new ProxyServerRequestClient();
                        String messageToSend = "{" + reqReceived[0] + "," + clientSocket.getInetAddress().getHostAddress()
                                + "," + reqReceived[2] + "}";
                        client.connect(InetAddress.getLocalHost().getHostAddress(), receivingServer.getListeningPort());
                        client.sendMessage(messageToSend);
                        client.disconnect();
                        out.println("SUCCESS");
                    }else{
                        out.println("ERROR no such dictionary");
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private LanguageServer findLanguageServer(String language) {
        for (LanguageServer server : languages) {
            //System.out.println(server.getLanguageDictionaryLanguage());
            if (server.getLanguageDictionaryLanguage().equals(language)) {
                return server;
            }
        }
        return null;
    }
}