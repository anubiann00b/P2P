package p2p;

import p2p.broadcast.NetworkJoin;
import p2p.util.Data;
import p2p.util.Debug;

public class Main {
    
    public static final String IP = "255.255.255.255";
    public static final int PORT = 4242;
    public static final int MAX_NETWORK_SIZE = 50;
    
    public static void main(String[] args) {
        Data.init();
        Debug.print("Starting.");
        new Thread(new NetworkJoin()).start();
    }
}
