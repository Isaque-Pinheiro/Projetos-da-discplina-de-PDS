package com.isaque.ufc.app.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServidorEnvia {
    public ServidorEnvia(ArrayList<Socket> listaUsuario, Object message, String info, String name) throws IOException {
        String messages = info + "." + message + "." + name;
        PrintWriter pwOut = null;
        for (Socket s : listaUsuario) {
            pwOut = new PrintWriter(s.getOutputStream(), true);
            pwOut.println(messages);
        }
    }

    public ServidorEnvia(Socket s, Object message, String info) throws IOException {
        String messages = info + "." + message;
        PrintWriter pwOut = new PrintWriter(s.getOutputStream(), true);
        pwOut.println(messages);
    }
    
}
