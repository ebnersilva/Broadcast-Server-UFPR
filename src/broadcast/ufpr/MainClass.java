/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package broadcast.ufpr;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.applet.Main;

/**
 *
 * @author ebner
 */
public class MainClass {
    public static void main(String[] args) {
        int porta = 9876;
        int numConn = 1;

        try {
            DatagramSocket serverSocket = null;
            serverSocket = new DatagramSocket(porta);

            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            
            while (true) {

                DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
                System.out.println("Esperando por datagrama UDP na porta " + porta);
                serverSocket.receive(receivePacket);
                System.out.println("Datagrama UDP [" + numConn + "] recebido... => Ip: "+ receivePacket.getAddress() + " => Tamanho do pacote: "+ receivePacket.getLength());

                String sentence = new String(receivePacket.getData());
                
                if (sentence.substring(0, 5).equals("TE354")) {
                    System.out.println(sentence);
                }

                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData,
                                sendData.length, IPAddress, port);
                
                try {
                    serverSocket.send(sendPacket);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o servidor");
        }
}
    
}
