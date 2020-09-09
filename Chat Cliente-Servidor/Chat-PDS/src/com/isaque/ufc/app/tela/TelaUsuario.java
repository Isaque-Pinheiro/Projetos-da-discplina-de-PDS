package com.isaque.ufc.app.tela;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.isaque.ufc.app.controlador.UsuarioEnvia;
import com.isaque.ufc.app.controlador.UsuarioRecebe;

public class TelaUsuario {
    public static JFrame window;
    public static JButton connect, sair;
    public static JTextArea textMessage;
    public static Socket socket = null;
    public static JList<String> user;

    JTextField nomeUsuario, porta, message, msgPriv;
    JButton msgPrivada, enviar;

    public TelaUsuario() {
        init();
    }

    public void init() {
        window = new JFrame("Cliente | Chat-PDS");
        window.setLayout(null);
        window.setBounds(400, 400, 540, 520);
        window.setResizable(true);

        JLabel label_nomeUsuario = new JLabel("Nome ou Ip:");
        label_nomeUsuario.setBounds(10, 28, 70, 30);
        window.add(label_nomeUsuario);

        nomeUsuario = new JTextField();
        nomeUsuario.setBounds(80, 28, 70, 30);
        window.add(nomeUsuario);

        JLabel label_porta = new JLabel("Porta:");
        label_porta.setBounds(170, 28, 50, 30);
        window.add(label_porta);

        porta = new JTextField();
        porta.setBounds(210, 28, 70, 30);
        window.add(porta);

        connect = new JButton("Entrar");
        connect.setBounds(310, 28, 90, 30);
        window.add(connect);

        sair = new JButton("Sair");
        sair.setBounds(422, 28, 90, 30);
        window.add(sair);

        textMessage = new JTextArea();
        textMessage.setBounds(10, 70, 340, 220);
        textMessage.setEditable(false);

        textMessage.setLineWrap(true);
        textMessage.setWrapStyleWord(true);

        JLabel label_text = new JLabel("Caixa de mensagens");
        label_text.setBounds(100, 58, 200, 50);
        window.add(label_text);

        JScrollPane paneText = new JScrollPane(textMessage);
        paneText.setBounds(10, 90, 360, 240);
        window.add(paneText);

        JLabel label_listaUsuario = new JLabel("Usuários online");
        label_listaUsuario.setBounds(380, 58, 200, 50);
        window.add(label_listaUsuario);

        user = new JList<String>();
        JScrollPane paneUser = new JScrollPane(user);
        paneUser.setBounds(375, 90, 140, 240);
        window.add(paneUser);

        JLabel label_Alerta = new JLabel("Escrever mensagem");
        label_Alerta.setBounds(10, 320, 180, 50);
        window.add(label_Alerta);
        
        
        message = new JTextField();
        message.setBounds(10, 355, 188, 30);
        message.setText(null);
        window.add(message);

        JLabel label_Aviso = new JLabel("Digite o ID do usuário");
        label_Aviso.setBounds(10, 384, 250, 50);
        window.add(label_Aviso);

        msgPriv = new JTextField();
        msgPriv.setBounds(10, 420, 188, 30);
        window.add(msgPriv);

        msgPrivada = new JButton("Enviar privado");
        msgPrivada.setBounds(190, 420, 115, 30);
        window.add(msgPrivada);

        enviar = new JButton("Enviar geral");
        enviar.setBounds(190, 355, 115, 30);
        window.add(enviar);

        myEvent();  
        window.setVisible(true);
    }

    public void myEvent() {
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (socket != null && socket.isConnected()) {
                    try {
                        new UsuarioEnvia(socket, getnomeUsuario(), "3", "");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });

        sair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (socket == null) {
                    JOptionPane.showMessageDialog(window, "A conexão já está encerrada!");
                } 
                
                else if (socket != null && socket.isConnected()) {
                    try {
                        new UsuarioEnvia(socket, getnomeUsuario(), "3", "");
                        connect.setText("Entrar");
                        sair.setText("saiu!");
                        socket.close();
                        socket = null;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        connect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (socket != null && socket.isConnected()) {
                    JOptionPane.showMessageDialog(window, "Você já está conectado!");
                } else {
                    String ipString = "127.0.0.1";
                    String portaClinet = porta.getText();
                    String name = nomeUsuario.getText();

                    if ("".equals(name) || "".equals(portaClinet)) {
                        JOptionPane.showMessageDialog(window, "O usuário ou a porta podem estar vazios!");
                    } else {
                        try {
                            int portas = Integer.parseInt(portaClinet);
                            socket = new Socket(ipString, portas);
                            connect.setText("Entrou");
                            sair.setText("sair");
                            new UsuarioEnvia(socket, getnomeUsuario(), "2", "");
                            new Thread(new UsuarioRecebe(socket)).start();
                        } catch (Exception e2) {
                            JOptionPane.showMessageDialog(window, "Falha em conectar-se, verifique o ip e a porta ou se o servidor está ativo");
                        }
                    }
                }
            }
        });

        msgPrivada.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviarMsgmsgPriv();
            }
        });

        enviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviarMsg();
            }
        });
    }

    public void enviarMsg() {
        String messages = message.getText();
        if ("".equals(messages)) {
            JOptionPane.showMessageDialog(window, "Não há mensagem para enviar");
        } else if (socket == null || !socket.isConnected()) {
            JOptionPane.showMessageDialog(window, "O servidor não está ativo");
        } else {
            try {
                new UsuarioEnvia(socket, getnomeUsuario() + ": " + messages, "1", "");
                message.setText(null);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(window, "Falha ao enviar!");
            }
        }
    }

    public void enviarMsgmsgPriv() {
        String messages = message.getText();
        if ("".equals(messages)) {
            JOptionPane.showMessageDialog(window, "Não há mensagem para enviar!");
        } 
        
        else if (socket == null || !socket.isConnected()) {
            JOptionPane.showMessageDialog(window, "O servidor não está ativo");
        } 
        
        else {
            try {
                new UsuarioEnvia(socket, getnomeUsuario() + ": " + messages, "4", getmsgPrivada());
                TelaUsuario.textMessage.append(getnomeUsuario() + ": " + messages + "\r\n");
                message.setText(null);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(window, "Mensagem privada não enviada!");
            }
        }
    }

    public String getnomeUsuario() {
        return nomeUsuario.getText();
    }

    public String getmsgPrivada() {
        return msgPriv.getText();
    }
}
