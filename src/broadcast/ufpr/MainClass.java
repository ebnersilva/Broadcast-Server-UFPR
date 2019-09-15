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
import jdk.javadoc.internal.tool.Main;

/**
 *
 * @author elielton
 */
public class MainClass {
    public static void main(String[] args) {
        int porta = 9876;
        int numConn = 1;

        try {
            DatagramSocket serverSocket = null; //entra no try
            serverSocket = new DatagramSocket(porta); // 
            serverSocket.setBroadcast(true);

            byte[] receiveData = new byte[1024]; // vetor para receber valor do cliente
            byte[] sendData = new byte[1024]; // vetor para receber valor do cliente
            
            while (true) {
                // este while faz ficar "rodando" continuamente o software monitor
                // espera o pacote do cliente
                DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
                System.out.println("Esperando por datagrama UDP na porta " + porta);
                // Recebe o pacote do cliente
                serverSocket.receive(receivePacket);
                System.out.println("Datagrama UDP [" + numConn + "] recebido... => Ip: "+ receivePacket.getAddress() + " => Tamanho do pacote: "+ receivePacket.getLength());
                
                // Cria uma string que pega a mensagem do cliente
                String sentence = new String(receivePacket.getData());
                
                // Se a string contém TE354 no início da mensagem, o software printa a mensagem
                if (sentence.substring(0, 5).equals("TE354")) {
                    System.out.println(sentence);
                }
                
                // Retornar os dados para o broadcast da rede
                InetAddress IPAddress = InetAddress.getByName("192.168.100.255");
                int port = receivePacket.getPort();
                String capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes(); //Criamos a string em bytes
                // Cria o objeto para retornar o pacote para o broadcast
                DatagramPacket sendPacket = new DatagramPacket(sendData,
                                sendData.length, IPAddress, port);
                
                // Envia o pacote pelo serverSOcket
                try {
                    serverSocket.send(sendPacket);
       
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o servidor" + e);
        }
}
    
}
