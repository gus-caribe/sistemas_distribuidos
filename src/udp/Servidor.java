
package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Servidor extends Thread {
    
    private final int PORTA = 12345;
    
    public Servidor(){
        System.out.println(
            "Servidor iniciado na porta " + PORTA
        );
    }
    
    @Override
    public void run(){
        try {
            //criar um novo socket
            DatagramSocket s = new DatagramSocket(PORTA);
            
            while(true){
                //definir a mensagem
                byte[] msg = new byte[256];
                byte[] answer = new byte[]{00, 00, 00, 01};
                
                //criar o pacote
                DatagramPacket pct = new DatagramPacket(
                    msg,
                    msg.length
                );
                DatagramPacket ans = new DatagramPacket(
                    answer,
                    answer.length
                );
                
                //receber o pacote
                s.receive(pct);
                new DatagramSocket().send(ans);
                
                //desempacotar
                String dados = new String(pct.getData()).trim();
                System.out.println("DE");
                //System.out.println(pct.getAddress().getHostAddress());
                System.out.println(pct.getAddress().getHostName());
                System.out.println("MSG");
                System.out.println(dados + "\n");
            }
            
        } catch (Exception e) {
            System.err.println("ERRO: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        new Servidor().start();
    }
    
}
