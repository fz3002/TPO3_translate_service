package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    final static int listeningPort = 8080;
    final static int sendPort = 5454;
    private Socket socket;
    private ServerSocket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(){
    }

    public void connectToProxy(String host) throws UnknownHostException, IOException {
        socket = new Socket(host , 1122);
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
        Client client = new Client();
        try{
            InetAddress address = InetAddress.getLocalHost();
            System.out.println(address.getHostAddress());
            //TODO: server listening on port
            //client.connectToProxy(address.getHostAddress());
            GUI gui = new GUI();
            while(true){
                if(gui.newInput){
                    String[] userInput = gui.getUserInput();
                    String message = "{" + userInput[0] + "," + userInput[1] + "," + listeningPort + "}";
                    //client.sendMessage(message);
                }
            }
            
            //client.disconnect();
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }


}
