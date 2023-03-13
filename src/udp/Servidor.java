
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.time.Duration;

public class Servidor extends Thread {
    
    private final String HOST = "192.168.56.1";
    private final int PORTA = 12346;
    private final int PORTACLIENTE = 12345;
    
    public Servidor(){
        System.out.println(
            "Servidor iniciado na porta " + PORTA
        );
        //run();
    }
    
    @Override
    public void run() {
        try {
            DatagramSocket s = new DatagramSocket(PORTA);
            System.out.println("escutando");

            while (true) {
                byte[] msg = new byte[256];

                DatagramPacket pct = new DatagramPacket(
                    msg,
                    msg.length
                );
                s.receive(pct);

                String ansString = new String(pct.getData()).trim();
                System.out.println("DE");
                System.out.println(pct.getAddress().getHostName());
                System.out.println("MSG");
                System.out.println(ansString + "\n");
            }

        } catch (IOException e) {
            System.err.println("ERRO: " + e.getMessage());
        }
    }

    public void enviar(String msg) {
        try {
            byte[] dados = msg.getBytes();
            System.out.println("enviando na porta: " + PORTACLIENTE);

            DatagramPacket pct = new DatagramPacket(
                    dados,
                    dados.length,
                    InetAddress.getByName(HOST),
                    PORTACLIENTE
            );
            new DatagramSocket().send(pct);
        } catch (IOException e) {
            System.err.println("ERRO: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        new Servidor().start();
    }
    
}
