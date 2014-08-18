package p2p;

public class Main {
    
    public static final String IP = "255.255.255.255";
    public static final int PORT = 4242;
    public static final int MAX_NETWORK_SIZE = 50;
    
    public static void main(String[] args) {
        new Thread(new NetworkJoin()).start();
    }
}
