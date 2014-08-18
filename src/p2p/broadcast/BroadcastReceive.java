package p2p.broadcast;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;
import p2p.Main;
import p2p.util.Data;
import p2p.util.Debug;

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
        
        Debug.print("Starting broadcast listener, waiting for broadcasts.");
        
        Data d;
        try {
            d = Data.receive(recvSocket);
        } catch (SocketTimeoutException e) {
            throw new RuntimeException("Socket timed out: " + e);
        }
        
        Debug.print("Recieved broadcast packet: " + d);
        Map<String, String> map = d.interperet();
    }
}
