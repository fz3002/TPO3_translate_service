package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try(InputStream input = clientSocket.getInputStream()){
            byte[] buffer = new byte[20];
            int bufferRead = input.read(buffer);

            System.out.println(new String(buffer, 0, 20));
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