package com.example.ClientHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import com.example.Clients.ProxyServerRequestClient;
import com.example.Servers.LanguageServer;

public class ClientHandlerProxy implements Runnable {

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String[] reqReceived;
    private List<LanguageServer> languages;

    public ClientHandlerProxy(Socket clientSocket, List<LanguageServer> languages) {
        this.clientSocket = clientSocket;
        this.languages = languages;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            String messageReceived = in.readLine();

            if (!messageReceived.startsWith("{") && !messageReceived.endsWith("}")) {
                out.println("Message formating error");
                clientSocket.close();
            } else {
                messageReceived = messageReceived.substring(1, messageReceived.length() - 1);
                reqReceived = messageReceived.split(",");
                // TODO: Finding exisiting servers and sending requests or returning error
                // response
                LanguageServer receivingServer = findLanguageServer(reqReceived[1]);
                if (receivingServer != null) {
                    ProxyServerRequestClient client = new ProxyServerRequestClient();
                    String messageToSend = "{" + reqReceived[0] + "," + clientSocket.getInetAddress().getHostAddress()
                            + ", " + reqReceived[2] + "}";
                    client.connect(InetAddress.getLocalHost().getHostAddress(), receivingServer.getListeningPort());
                    client.sendMessage(messageToSend);
                    client.disconnect();
                }else{
                    // TODO: Handling not finding server with given language
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
            if (server.getLanguageDictionaryLanguage() == language) {
                return server;
            }
        }
        return null;
    }
}