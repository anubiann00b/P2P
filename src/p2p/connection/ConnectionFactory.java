package p2p.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import p2p.Main;
import p2p.util.Debug;

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
            
            Debug.print("Made connection to " + s.getRemoteSocketAddress() + ":" + s.getPort());
            new Thread(new Connection(s, this)).start();
            
            connected++;
        }
    }
}
