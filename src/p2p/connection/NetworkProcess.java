package p2p.connection;

public abstract class NetworkProcess implements Runnable {
    
    public abstract void response(Connection c, boolean r);
}