package com.example.Clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.GUI;
import com.example.Interfaces.Client;
import com.example.Servers.ListenerServer;

public class MainClient implements Client {

    final static int LISTENINGPORT = 8080;
    final static int SENDPORT = 5454;
    private Socket socket;
    private ServerSocket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

    public MainClient(){
    }

    public void connect(String host, int port) throws UnknownHostException, IOException {
        socket = new Socket(host , port);
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

    public static void main(String[] args) {
        MainClient client = new MainClient();
        try{
            InetAddress address = InetAddress.getLocalHost();
            System.out.println(address.getHostAddress());
            GUI gui = new GUI();
            while(true){
                if(gui.newInput){
                    String[] userInput = gui.getUserInput();
                    String message = "{" + userInput[0] + "," + userInput[1] + "," + LISTENINGPORT + "}";
                    client.connect(address.getHostAddress(), SENDPORT);
                    client.sendMessage(message);
                    client.disconnect();
                    ListenerServer server = new ListenerServer(new ServerSocket(LISTENINGPORT)); //Find a way not ot block waitning for responses, probably closing server
                    String received = server.received.getValue();
                    gui.updateAnswer(received);
                }
                
            }
            
            //client.disconnect();
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }


}
