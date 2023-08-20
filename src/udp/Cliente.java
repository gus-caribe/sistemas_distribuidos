
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Cliente extends Thread {
    
    private final String HOST = "192.168.56.1";
    private final int PORTASERVIDOR = 12345;
    private Integer porta = null;
    private boolean portaEnviada = false;
    private DatagramSocket s;
    String ansString = "";
    Runnable atualizarPorta = new Runnable() {
        @Override
        public void run() {
            if(!portaEnviada && porta != null) {
                enviar(Integer.toString(porta));
            }
        }
    };

    public Cliente() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(atualizarPorta, 0, 1, TimeUnit.SECONDS);

        int portaTemp = 12346;
        
        while(true) {
            if(available(portaTemp)) {
                try {
                    porta = portaTemp;
                    s = new DatagramSocket(porta);
                    break;
                } catch (IOException e) {
                    portaTemp++;
                }
            } else {
                portaTemp++;
            }
        }
        
        System.out.println("Cliente iniciado na porta: " + portaTemp);
        
        enviar(Integer.toString(porta));
    }

    public static boolean available(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }
    
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {}
            }
        }
    
        return false;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Escutando na porta: " + porta);

            while (true) {
                byte[] msg = new byte[256];

                DatagramPacket pct = new DatagramPacket(
                    msg,
                    msg.length
                );
                s.receive(pct); 
                
                portaEnviada = true;
                
                ansString = new String(pct.getData());

                System.out.println("Recebido: " + ansString);
            }
        } catch (IOException e) {
            System.err.println("ERRO: " + e.getMessage());
        }
    }

    public final void enviar(String msg) {
        try {
            byte[] dados = msg.getBytes();
            System.out.println("Enviando na porta: " + PORTASERVIDOR + ", conteudo: " + msg);

            DatagramPacket pct = new DatagramPacket(
                dados,
                dados.length,
                InetAddress.getByName(HOST),
                PORTASERVIDOR
            );
            new DatagramSocket().send(pct);
        } catch (IOException e) {
            System.err.println("ERRO: " + e.getMessage());
        }
    }
    
}
