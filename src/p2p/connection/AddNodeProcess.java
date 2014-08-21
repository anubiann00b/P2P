package p2p.connection;

import java.net.DatagramSocket;
import java.net.InetAddress;
import p2p.Main;
import p2p.util.Data;
import p2p.util.Debug;

public class AddNodeProcess extends NetworkProcess {
    
    DatagramSocket socket;
    InetAddress destIp;
    int destPort;
    
    int socketAcceptCounter;
    
    public AddNodeProcess(DatagramSocket s, InetAddress ip, int p) {
        destIp = ip;
        destPort = p;
        socket = s;
        socketAcceptCounter = 0;
    }
    
    @Override
    public void run() {
        Debug.print("Attempting to add new node.");
        if (Connection.MANAGER.sockets.isEmpty()) {
            new Thread(new Acceptance(socket, destIp, destPort)).start();
            return;
        }
        
        Connection.MANAGER.sockets.stream().forEach((c) -> {
            c.send(new Data(Data.CONFIRM_JOIN, destIp.getHostAddress() + ' ' + destPort), this);
        });
        
        int nodes = Connection.MANAGER.sockets.size();
        while (socketAcceptCounter>0 && socketAcceptCounter<nodes);
    }

    @Override
    public void response(Connection c, boolean r) {
        if (r)
            socketAcceptCounter++;
        else
            socketAcceptCounter = Integer.MIN_VALUE;
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
        Connection.MANAGER.connect(destIp, Main.PORT);
    }
}