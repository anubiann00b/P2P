package p2p.broadcast;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;
import p2p.Main;
import p2p.util.Data;

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
        
        Data d;
        try {
            d = Data.receive(recvSocket);
        } catch (SocketTimeoutException e) {
            throw new RuntimeException("Socket timed out: " + e);
        }
        
        Map<String, String> map = d.interperet();
    }
}
