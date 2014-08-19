package p2p.connection;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {
    
    public static ConnectionManager instance = new ConnectionManager();
    
    public List<Connection> sockets;
    
    private ConnectionManager() {
        sockets = new ArrayList<Connection>();
    }

    void connect(InetAddress destIp, int destPort) {
        Connection c = new Connection(destIp, destPort);
        sockets.add(c);
    }
}