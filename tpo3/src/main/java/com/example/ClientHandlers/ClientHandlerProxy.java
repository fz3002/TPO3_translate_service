package com.example.ClientHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandlerProxy implements Runnable{

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String[] reqReceived;

    public ClientHandlerProxy(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try{
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            String messageReceived = in.readLine();
            
            if(!messageReceived.startsWith("{")&&!messageReceived.endsWith("}")){
                out.println("Message formating error");
                clientSocket.close();
            }else{
                messageReceived = messageReceived.substring(1, messageReceived.length() -1);
                reqReceived = messageReceived.split(",");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}