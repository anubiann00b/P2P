package p2p.connection;

import java.net.DatagramSocket;
import java.net.InetAddress;
import p2p.util.Data;
import p2p.util.Debug;

public class AddNodeProcess implements Runnable {
    
    InetAddress destIp;
    int destPort;
    
    public AddNodeProcess(InetAddress ip, int p) {
        destIp = ip;
        destPort = p;
    }
    
    @Override
    public void run() {
        Debug.print("Attempting to add new node.");
        for (Connection c : Connection.MANAGER.sockets) {
            c.send(new Data(Data.CONFIRM_JOIN, destIp.getHostAddress() + ' ' + destPort));
        }
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
        Debug.print("Accepting join from " + destIp.getHostAddress() + ":" + destPort);
        Data.send(socket, destIp, destPort, new Data(Data.ACCEPT_JOIN));
        Connection.MANAGER.connect(destIp, destPort);
    }
}