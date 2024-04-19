package com.example.Clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.GUI;
import com.example.Interfaces.Client;
import com.example.Servers.ListenerServer;

public class MainClient implements Client {

    final static int SENDPORT = 5454;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String responseFromProxyServer;
    public GUI gui;
    private static Random random = new Random();

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
        this.responseFromProxyServer = in.readLine();
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public boolean validateSource(String path) {

        Pattern pattern = Pattern.compile("\\{\\w+, \\w+\\}");
        Boolean linesMatch = false;
        if (!path.endsWith(".txt") && path.substring(path.lastIndexOf('/'), path.length() - 1).length() == 6)
            return false;
        try {
            linesMatch = Files
                    .lines(Paths.get(path))
                    .allMatch(line -> {
                        Matcher matcher = pattern.matcher(line);
                        return matcher.matches();
                    });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return linesMatch;
    }

    public static void main(String[] args) {

        MainClient client = new MainClient();

        while (true) {
            try {

                InetAddress address = InetAddress.getLocalHost();

                // Sending Request and showing answer

                System.out.println("test");

                boolean sendReq = client.gui.newInputAvailable();

                // TRANSLATION REQUEST

                if (sendReq) {

                    ListenerServer server = new ListenerServer();
                    new Thread(server).start();

                    int LISTENINGPORT = random.nextInt(1025, 65535);

                    ServerSocket serverSocket = new ServerSocket(LISTENINGPORT);

                    server.setSocket(serverSocket);

                    System.out.println("================================================================");

                    String[] userInput = client.gui.getUserInput();
                    String message = "{" + userInput[0] + "," + userInput[1] + "," + LISTENINGPORT + "}";

                    client.connect(address.getHostAddress(), SENDPORT); // Start connection to Proxy server
                    // TODO: Add timeout handling
                    // TODO: Add feature adding new langugae server in gui
                    // TODO: Chceck for errors in messeges
                    client.sendMessage(message); // Send message with request for transtaltion to proxy server

                    System.out.println(client.responseFromProxyServer);

                    if (client.responseFromProxyServer.startsWith("ERROR")) { // Handle error from server proxy
                        client.gui.showPopUp(client.responseFromProxyServer);
                        client.disconnect();
                        continue;
                    }

                    client.disconnect();

                    // RESPONSE FROM LANGUAGE SERVER HANDLING

                    String receivedAnswer = server.received.getValue();
                    while (receivedAnswer == null) {
                        receivedAnswer = server.received.getValue();
                    }
                    System.out.println("received: " + receivedAnswer);
                    if (receivedAnswer.startsWith("ERROR")) { // Handling error from server proxy
                        client.gui.showPopUp(receivedAnswer);
                        receivedAnswer = null;
                        server.received.setValue(null);
                        continue;
                    }

                    client.gui.getLabel().setText("Answer: " + receivedAnswer); // Displaying answer
                    receivedAnswer = null;
                    server.received.setValue(null);
                    server.setSocket(null);

                }

                // ADD NEW SERVER REQUEST
                if (client.gui.newServerCreationRequest) {
                    System.out.println("test add new server request");
                    String pathToSource = client.gui.getPathToSource();
                    if (client.validateSource(pathToSource)) {
                        client.connect(address.getHostAddress(), SENDPORT);
                        client.sendMessage(new String("CREATE, " + pathToSource));
                        client.gui.showPopUp(client.responseFromProxyServer);
                        client.disconnect();

                    } else {
                        client.gui.showPopUp("File is not a valid source for new Language Server");
                    }
                }

            } catch (IOException e) {
                client.gui.showPopUp(e.toString());
            }
        }
    }

}
