package p2p;

import java.io.IOException;

public class Main {
    
    public static final int PORT = 4242;
    
    public static void main(String[] args) throws IOException {
        new Thread(new MulticastTask()).start();
    }
}
