package p2p;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
            socket.bind(new InetSocketAddress(7777));
            socket.setSoTimeout(CONNECTION_TIMEOUT);
            socket.setBroadcast(true);
            socket.setLoopbackMode(true);
            state = State.CONNECTING;
            socket.joinGroup(group);
            state = State.CONNECTED;
            
            while (state == State.CONNECTED) {
                Data request = new Data("RJ");
                Data.send(socket, request);
                Data d = Data.receive(socket);
                System.out.println(d);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}