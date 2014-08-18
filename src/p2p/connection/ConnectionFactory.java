package p2p.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import p2p.Main;

public class ConnectionFactory implements Runnable {
    
    private ServerSocket server;
    private int connected;
    private int connections;
    
    public void setConnections(int c) {
        connections = c;
    }
    
    @Override
    public void run() {
        connected = 0;
        connections = Main.MAX_NETWORK_SIZE;
        
        while (connected < connections) {
            Socket s;
            try {
                s = server.accept();
            } catch (IOException e) {
                throw new RuntimeException("Failed to get connection " + e);
            }
            
            new Thread(new Connection(s, this)).start();
        }
    }
}
