package p2p.connection;

import java.net.DatagramSocket;
import java.net.InetAddress;
import p2p.util.Data;
import p2p.util.Debug;

public class AddNodeProcess implements Runnable {
    
    @Override
    public void run() {
        Debug.print("Attempting to add new node.");
    }
}

class Acceptance implements Runnable {
    
    DatagramSocket socket;
    InetAddress destIp;
    int destPort;
    
    Acceptance(DatagramSocket s, InetAddress ip, int port) {
        socket = s;
        destIp = ip;
        destPort = port;
    }
    
    @Override
    public void run() {
        Debug.print("Accepting join from " + destIp + ":" + destPort);
        Data.send(socket, destIp, destPort, new Data(Data.ACCEPT_JOIN));
    }
}