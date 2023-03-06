
package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class Cliente extends Thread{
    
    private final String HOST = "172.25.3.161";
    private final int PORTA = 12345;
    
    public void enviar(String msg){
        try {
            //converter a mensagem em bytes
            byte[] dados = msg.getBytes();
            //criar um novo socket
            DatagramSocket s = new DatagramSocket(PORTA);
            //definir timeout para aresposta da mensagem
            final Duration timeout = Duration.ofSeconds(30);
            //guarda a hora da requisição
            LocalDateTime horaRequisicao;
            
            DatagramPacket pct = new DatagramPacket(
                dados,
                dados.length,
                InetAddress.getByName(HOST),
                PORTA
            );
            DatagramPacket ans = new DatagramPacket(
                new byte[4],
                new byte[4].length
            );
            
            horaRequisicao = LocalDateTime.now();
            new DatagramSocket().send(pct);
            
            while(LocalDateTime.now().compareTo(horaRequisicao.plus(timeout)) < 1) {
                s.receive(ans);
            }
            
            switch(ByteBuffer.wrap(ans.getData()).getInt()) {
                case 0 -> System.out.println("ERRO DE TIMEOUT");
                case 1 -> System.out.println("MENSAGEM RECEBIDA");
                default -> System.out.println("ERRO GENÉRICO");
            }
            
        } catch (Exception e) {
            System.err.println("ERRO: " + e.getMessage());
        }
    }
    
}
