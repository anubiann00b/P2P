package p2p.connection;

import java.net.DatagramSocket;
import java.net.InetAddress;
import p2p.connection.task.Acceptance;
import p2p.connection.task.Rejection;
import p2p.util.Action;
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
            Action.suggestAction(null);
            return;
        }
        
        Connection.MANAGER.sockets.stream().forEach((c) -> {
            c.send(new Data(Data.CONFIRM_JOIN, destIp.getHostAddress() + ' ' + destPort), this);
        });
        
        int nodes = Connection.MANAGER.sockets.size();
        while (socketAcceptCounter<nodes) {
            try {
                synchronized(this) {
                    this.wait();
                }
            } catch (InterruptedException e) {
                Debug.print("Interrupted! Counter at " + socketAcceptCounter);
            }
            
            if (socketAcceptCounter < 0)
                break;
        }
        
        Action.suggestAction(null);
        
        if (socketAcceptCounter < 0)
            new Thread(new Rejection(socket, destIp, destPort)).start();
        else
            new Thread(new Acceptance(socket, destIp, destPort)).start();
        
        Connection.MANAGER.sockets.stream().forEach((c) -> {
            c.processFinished();
        });
    }

    @Override
    public void response(Connection c, boolean r) {
        if (r)
            socketAcceptCounter++;
        else
            socketAcceptCounter = Integer.MIN_VALUE;
        synchronized(this) {
            this.notify();
        }
    }
}