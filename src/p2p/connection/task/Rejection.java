package p2p.connection.task;

import java.net.DatagramSocket;
import java.net.InetAddress;
import p2p.util.Data;
import p2p.util.Debug;

public class Rejection implements Runnable {
    
    DatagramSocket socket;
    InetAddress destIp;
    int destPort;
    
    public Rejection(DatagramSocket s, InetAddress ip, int port) {
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