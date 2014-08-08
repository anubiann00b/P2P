package p2p;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Main {
    
    public static final int PORT = 4242;
    
    public static void main(String[] args) throws IOException {
        Main m = new Main();
        m.findNetwork();
    }
    
    void findNetwork() throws IOException {
        InetAddress group = InetAddress.getByName("203.0.113.0");
        MulticastSocket socket = new MulticastSocket(Main.PORT);
        Data accept = new Data("RJ");
        
        boolean in = false;
        
        while (!in) {
            Data d = Data.receive(socket);
        }
    }
}
