package p2p.connection;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionManager {
    
    public static ConnectionManager instance = new ConnectionManager();
    
    public List<Connection> sockets;
    
    private ConnectionManager() {
        sockets = new CopyOnWriteArrayList<Connection>();
    }

    void connect(InetAddress destIp, int destPort) {
        Connection c = new Connection(destIp, destPort);
        sockets.add(c);
    }
}