
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class Servidor extends Thread {
    
    private final String HOST = "192.168.56.1";
    private final int PORTA = 12345;
    private ArrayList<Integer> portasCliente = new ArrayList<>();
    
    public Servidor(){
        System.out.println("Servidor iniciado na porta " + PORTA);
    }
    
    public void enviar(String estadoPlacar) {
        try {
            byte[] dados = estadoPlacar.getBytes();
            
            for(Integer porta : portasCliente) {
                DatagramPacket pct = new DatagramPacket(
                    dados,
                    dados.length,
                    InetAddress.getByName(HOST),
                    porta.intValue()
                );
                new DatagramSocket().send(pct);
                
                System.out.println("CONTEUDO:");
                System.out.println(estadoPlacar);
                System.out.println("ENVIADO NA PORTA:");
                System.out.println(porta);
            }
        } catch (IOException e) {
            System.err.println("ERRO: " + e.getMessage());
        }
    }
    
    @Override
    public void run() {
        try {
            DatagramSocket s = new DatagramSocket(PORTA);

            while (true) {
                byte[] msg = new byte[256];

                DatagramPacket pct = new DatagramPacket(
                    msg,
                    msg.length
                );
                s.receive(pct);
                
                Integer ansPorta = Integer.parseInt(new String(pct.getData()).trim());

                System.out.println("RECEBIDO: " + ansPorta.intValue());
                
                if(portasCliente.indexOf(ansPorta) == -1 && ansPorta != null) {
                    portasCliente.add(ansPorta);
                    
                    System.out.println("PORTA ADICIONADA: " + ansPorta);
                }
            }

        } catch (IOException e) {
            System.err.println("ERRO: " + e.getMessage());
        }
    }
    
}
