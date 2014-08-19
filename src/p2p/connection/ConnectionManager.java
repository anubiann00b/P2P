package p2p.connection;

import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {
    
    public static ConnectionManager instance = new ConnectionManager();
    
    List<Connection> sockets;
    
    private ConnectionManager() {
        sockets = new ArrayList<Connection>();
    }
}