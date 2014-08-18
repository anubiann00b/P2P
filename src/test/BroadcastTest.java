package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BroadcastTest {
    
    DatagramSocket c;
    
    public void start() {
        try {
            c = new DatagramSocket();
            c.setBroadcast(true);
            byte[] sendBuf = "RJ".getBytes();
            try {
                DatagramPacket sendPac = new DatagramPacket(sendBuf, sendBuf.length,
                        InetAddress.getByName("255.255.255.255"), 8888);
                c.send(sendPac);
                System.out.println("Join Request sent: 255.255.255.255");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] recvBuf = new byte[1024];
            DatagramPacket recvPac = new DatagramPacket(recvBuf, recvBuf.length);
            c.receive(recvPac);
            System.out.println("Response: " + recvPac.getAddress().getHostAddress());
            String message = new String(recvPac.getData()).trim();
            System.out.println("    Data: " + message);
            if (message.equals("JOIN_ACCEPT")) {
                System.out.println("Joined.");
            }
            c.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    public static void main(String[] args) {
        new BroadcastTest().start();
    }
}
