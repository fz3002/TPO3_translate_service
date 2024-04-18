package com.example.Clients;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.example.GUI;
import com.example.LanguageDictionary;
import com.example.Interfaces.Client;
import com.example.Servers.LanguageServer;
import com.example.Servers.ListenerServer;
import com.example.Servers.ServerProxy;

public class MainClient implements Client {

    final static int LISTENINGPORT = 8080;
    final static int SENDPORT = 5454;
    final static int SERVERPROXY_LISTENINGPORT = 1122;

    private final String PATH_TO_DATA = System.getProperty("user.dir") + "/data";

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private List<LanguageServer> langServers = new ArrayList<>();
    public GUI gui;

    public MainClient() {
        this.gui = new GUI();
    }

    public void connect(String host, int port) throws UnknownHostException, IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        System.out.println("Connected to " + socket.getInetAddress());
    }

    public void sendMessage(String message) throws IOException {
        out.println(message);
        String response = in.readLine();
        System.out.println(response);
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    private void createDefaultLanguageServers() throws IOException {
        File dataDir = new File(PATH_TO_DATA);
        File[] files = dataDir.listFiles();
        for (File file : files) {
            langServers.add(new LanguageServer(new ServerSocket(), new LanguageDictionary(file.getAbsolutePath())));
        }
        for (LanguageServer languageServer : langServers) {
            new Thread(languageServer).start();
        }
    }

    public static void main(String[] args) {

        MainClient client = new MainClient();

        try {
            // starts Main server
            ServerProxy serverProxy = new ServerProxy(new ServerSocket(SERVERPROXY_LISTENINGPORT));
            new Thread(serverProxy).start();

            // Starts Default Language Servers
            client.createDefaultLanguageServers();

            // Passes working default Language Servers to main server
            serverProxy.setLangServers(client.langServers);

            InetAddress address = InetAddress.getLocalHost();
            System.out.println(address.getHostAddress());

            // Sending Request and showing answer
            while (true) {
                if (client.gui.newInput) {
                    String[] userInput = client.gui.getUserInput();
                    String message = "{" + userInput[0] + "," + userInput[1] + "," + LISTENINGPORT + "}";
                    client.connect(address.getHostAddress(), SENDPORT);
                    client.sendMessage(message);
                    client.disconnect();
                    ListenerServer server = new ListenerServer(new ServerSocket(LISTENINGPORT)); // Find a way not ot
                                                                                                 // block waitning for
                                                                                                 // responses, probably
                                                                                                 // closing server
                    String received = server.received.getValue();
                    client.gui.updateAnswer(received);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
