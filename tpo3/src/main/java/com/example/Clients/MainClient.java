package com.example.Clients;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
    
    private static int currentLangServerPort = 2137;

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
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connected to " + socket.getInetAddress());
    }

    public void sendMessage(String message) throws IOException {
        out.println(message);
        String response = in.readLine();
        System.out.println("Response: " + response);
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    private void createDefaultLanguageServers() throws IOException {
        File dataDir = new File(PATH_TO_DATA);
        System.out.println(dataDir.getAbsolutePath());
        File[] files = dataDir.listFiles();
        for (File file : files) {
            langServers.add(new LanguageServer(new ServerSocket(currentLangServerPort++), new LanguageDictionary(file.getAbsolutePath())));
        }
        for (LanguageServer languageServer : langServers) {
            new Thread(languageServer, "Language Server " + languageServer.getLanguageDictionaryLanguage()).start();
        }
    }

    public static void main(String[] args) {

        MainClient client = new MainClient();

        try {
            // starts Main server
            ServerProxy serverProxy = new ServerProxy(new ServerSocket(SENDPORT));
            new Thread(serverProxy, "ServerProxyThread").start();

            // Starts Default Language Servers
            client.createDefaultLanguageServers();

            // Passes working default Language Servers to main server
            serverProxy.setLangServers(client.langServers);

            InetAddress address = InetAddress.getLocalHost();
            System.out.println(address.getHostAddress());

            // Sending Request and showing answer
            while (true) {
                boolean sendReq = client.gui.newInputAvailable();
                if (sendReq) {
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
