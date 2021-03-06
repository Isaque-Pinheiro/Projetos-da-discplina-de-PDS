package com.isaque.ufc.app.controlador;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.isaque.ufc.app.tela.TelaServidor;

import java.util.Map;

public class Servidor implements Runnable {
    private int porta;
    public static ArrayList<Socket> listaUsuario = null;
    public static Vector<String> nomeUsuario = null;    
    public static Map<String, Socket> map = null;
    public static ServerSocket ss = null;
    public static boolean flag = true;

    public Servidor(int porta) throws IOException {
        this.porta = porta;
    }

    public void run() {
        Socket s = null;
        listaUsuario = new ArrayList<Socket>();   
        nomeUsuario = new Vector<String>();     
        map = new HashMap<String, Socket>();  
        
        TelaServidor.console.append("Servidor iniciado! ---->> " + "\r\n");
        System.out.println("Servidor iniciado!");

        try {
            ss = new ServerSocket(porta);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        while (flag) {
            try {
                s = ss.accept();
                listaUsuario.add(s);
                new Thread(new ServidorRecebe(s, listaUsuario, nomeUsuario, map)).start();
            } catch (IOException e) {
                TelaServidor.console.append("O servidor foi encerrado! ---->> " +  "\r\n");
            }
        }
    }
}
