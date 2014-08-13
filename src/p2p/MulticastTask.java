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
            InetAddress group = InetAddress.getByName(Main.IP);
            MulticastSocket socket = new MulticastSocket(Main.PORT);
            socket.setSoTimeout(CONNECTION_TIMEOUT);
            state = State.CONNECTING;
            socket.joinGroup(group);
            state = State.CONNECTED;
            
            while (state == State.CONNECTED) {
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