package com.example.diogo.server_socket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity_Server extends AppCompatActivity{

    TextView msg;

    public static void main(String[] args) throws IOException{

        ServerSocket serverSocket = new ServerSocket(8080);
        Socket socket;

        while(true){
            System.out.println("Esperando conexão... ... ...");

            socket = serverSocket.accept();
            System.out.println("Conectado com cliente");

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            out.writeBytes("Servidor: " + in.readUTF());
            out.flush();

            System.out.println("Mensagem enviada!");
            System.out.println("Fechando conexão");

            out.close();
            socket.close();
        }
    }
}

