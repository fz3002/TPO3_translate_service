package com.example.ClientHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.example.LanguageDictionary;
import com.example.Clients.ProxyServerRequestClient;
import com.example.Servers.LanguageServer;
import com.example.Servers.ServerProxy;

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
            try {
                clientSocket.close();
            } catch (Exception e1) {
            }
        }

    }

    @Override
    public void run() {

        System.out.println("Client Handler Started");

        try {

            for (String messageReceived; (messageReceived = in.readLine()) != null;) {

                System.out.println(messageReceived);

                if ((!messageReceived.startsWith("{") || !messageReceived.endsWith("}"))
                        && !messageReceived.startsWith("CREATE")) {

                    out.println("Message formating error");
                    clientSocket.close();

                } else if (messageReceived.startsWith("{") && messageReceived.endsWith("}")) {

                    messageReceived = messageReceived.substring(1, messageReceived.length() - 1);
                    reqReceived = messageReceived.split(",");
                    LanguageServer receivingServer = findLanguageServer(reqReceived[1]);

                    if (receivingServer != null) {

                        out.println("SUCCESS");

                        ProxyServerRequestClient client = new ProxyServerRequestClient();
                        String messageToSend = "{" + reqReceived[0] + ","
                                + clientSocket.getInetAddress().getHostAddress()
                                + "," + reqReceived[2] + "}";

                        client.connect(InetAddress.getLocalHost().getHostAddress(), receivingServer.getListeningPort());
                        client.sendMessage(messageToSend);
                        client.disconnect();

                    } else {

                        out.println("ERROR no such dictionary");
                    
                    }
                } else {

                    String pathToSource = messageReceived.split(" ")[1];
                    int port = ServerProxy.getCurrentLangServerPort();
                    languages.add(new LanguageServer(new ServerSocket(port++), new LanguageDictionary(pathToSource)));

                    new Thread(languages.getLast(),
                            "Language Server " + languages.getLast().getLanguageDictionaryLanguage()).start();

                    ServerProxy.incrementCurrentLangServerPort();

                    out.println("SUCCESS");
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
            // System.out.println(server.getLanguageDictionaryLanguage());
            if (server.getLanguageDictionaryLanguage().equals(language)) {
                return server;
            }
        }
        return null;
    }
}