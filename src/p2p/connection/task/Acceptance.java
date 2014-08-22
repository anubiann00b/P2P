package p2p.connection.task;

import java.net.DatagramSocket;
import java.net.InetAddress;
import p2p.Main;
import p2p.connection.Connection;
import p2p.util.Data;
import p2p.util.Debug;

public class Acceptance implements Runnable {
    
    DatagramSocket socket;
    InetAddress destIp;
    int destPort;
    
    public Acceptance(DatagramSocket s, InetAddress ip, int port) {
        socket = s;
        destIp = ip;
        destPort = port;
    }
    
    @Override
    public void run() {
        Debug.print("Accepting join from " + destIp.getHostAddress() + ":" + destPort + "\n");
        Data.send(socket, destIp, destPort, new Data(Data.ACCEPT_JOIN));
        Connection.MANAGER.connect(destIp, Main.PORT, destPort);
    }
}