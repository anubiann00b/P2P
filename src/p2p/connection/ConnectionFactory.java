package p2p.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import p2p.Main;
import p2p.util.Debug;

public class ConnectionFactory implements Runnable {
    
    public static final int SERVER_TIMEOUT = 3000;
    
    private ServerSocket server;
    private int connected;
    private int connections;
    
    public void setConnections(int c) {
        connections = c;
    }
    
    @Override
    public void run() {
        try {
            server = new ServerSocket(Main.PORT);
            server.setSoTimeout(SERVER_TIMEOUT);
        } catch (IOException e) {
            throw new RuntimeException("Failed to open socket " + e);
        }
        
        connected = 0;
        connections = Main.MAX_NETWORK_SIZE;
        
        Debug.print("Making connections...");
        
        while (connected < connections) {
            Socket s;
            try {
                s = server.accept();
            } catch (SocketTimeoutException e) {
                Debug.print("Socket timed out.");
                continue;
            } catch (IOException e) {
                throw new RuntimeException("Failed to get connection " + e);
            }
            
            Debug.print("Made connection to " + s.getRemoteSocketAddress());
            Connection.MANAGER.start(new Connection(s, this));
            
            connected++;
        }
        
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close ServerSocket " + e);
        }
        Debug.print("Connected to network!");
    }
}
