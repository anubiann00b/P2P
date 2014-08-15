package p2p;

public class Main {
    
    public static final String IP = "239.255.0.8";
    public static final int PORT = 4242;
    
    public static void main(String[] args) {
        new Thread(new MulticastTask()).start();
    }
}
