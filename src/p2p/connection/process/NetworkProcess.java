package p2p.connection.process;

import p2p.connection.Connection;

public abstract class NetworkProcess implements Runnable {
    
    public abstract void response(Connection c, boolean r);
}