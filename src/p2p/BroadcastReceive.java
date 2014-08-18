package p2p;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class BroadcastReceive implements Runnable {
    
    private DatagramSocket recvSocket;
    
    @Override
    public void run() {
        try {
            recvSocket = new DatagramSocket(null);
            recvSocket.setBroadcast(true);
            recvSocket.setReuseAddress(true);
            recvSocket.bind(new InetSocketAddress(Main.PORT));
        } catch (SocketException e) {
            throw new RuntimeException("Failed to create recv socket: " + e);
        }
    }
}
