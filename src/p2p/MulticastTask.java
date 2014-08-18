package p2p;

import java.io.IOException;
import java.net.DatagramSocket;

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
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(CONNECTION_TIMEOUT);
            socket.setBroadcast(true);
            
            state = State.CONNECTING;
            
            while (state == State.CONNECTING) {
                Data request = new Data(Data.REQUEST_JOIN);
                Data.send(socket, request);
                Data d = Data.receive(socket);
                System.out.println(d);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}