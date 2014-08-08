package p2p;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastTask implements Runnable {
    
    private enum State {
        DISCONNECTED, CONNECTING, CONNECTED;
    }
    
    private static final int CONNECTION_TIMEOUT = 5000;
    private State state;
    
    public MulticastTask() {
        state = State.DISCONNECTED;
    }
    
    @Override
    public void run() {
        try {
            InetAddress group = InetAddress.getByName("203.0.113.0");
            MulticastSocket socket = new MulticastSocket(Main.PORT);
            socket.setSoTimeout(CONNECTION_TIMEOUT);
            socket.joinGroup(group);
            
            while (state == State.DISCONNECTED) {
                Data request = new Data("RJ");
                Data.send(socket, request);
                Data d = null;
                
                d = Data.receive(socket);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}