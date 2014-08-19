package p2p.broadcast;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;
import p2p.Main;
import p2p.connection.AddNodeProcess;
import p2p.util.Action;
import p2p.util.Data;
import p2p.util.Debug;

public class BroadcastListener implements Runnable {
    
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
        switch(map.get(Data.TYPE)) {
            case Data.REQUEST_JOIN:
                if (Action.suggestAction(Action.ADD_NEW))
                    new Thread(new AddNodeProcess()).start();
                else
                    new Thread(new Rejection(recvSocket, d.src, d.port)).start();
                break;
        }
    }
}

class Rejection implements Runnable {
    
    DatagramSocket socket;
    InetAddress destIp;
    int destPort;
    
    Rejection(DatagramSocket s, InetAddress ip, int port) {
        socket = s;
        destIp = ip;
        destPort = port;
    }
    
    @Override
    public void run() {
        Debug.print("Rejecting join from " + destIp + ":" + destPort);
        Data.send(socket, destIp, destPort, new Data(Data.REJECT_JOIN));
    }
}