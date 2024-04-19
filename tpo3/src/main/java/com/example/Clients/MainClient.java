package com.example.Clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

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

    

    public static void main(String[] args) {

        MainClient client = new MainClient();

        try {
            

            ListenerServer server = new ListenerServer();
            new Thread(server).start();

            InetAddress address = InetAddress.getLocalHost();
            System.out.println(address.getHostAddress());

            // Sending Request and showing answer
            while (true) {

                boolean sendReq = client.gui.newInputAvailable();
                
                // TRANSLATION REQUEST

                if (sendReq) {

                    
                    int LISTENINGPORT = random.nextInt(1025,65535);

                    server.setSocket(new ServerSocket(LISTENINGPORT));

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
                        client.gui.raiseError(client.responseFromProxyServer);
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
                        client.gui.raiseError(receivedAnswer);
                        receivedAnswer = null;
                        server.received.setValue(null);
                        continue;
                    }

                    client.gui.getLabel().setText("Answer: " + receivedAnswer); //Displaying answer
                    receivedAnswer = null;
                    server.received.setValue(null);
                    server.getServerSocket().close();
                }

                // ADD NEW SERVER REQUEST
                // TODO
                
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
