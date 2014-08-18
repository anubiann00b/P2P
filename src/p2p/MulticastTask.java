package p2p;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class MulticastTask implements Runnable {
    
    private enum State {
        DISCONNECTED, CONNECTING, CONNECTED, CREATE;
    }
    
    private static final int CONNECTION_TIMEOUT = 5000;
    private State state;
    DatagramSocket socket;
    
    public MulticastTask() {
        state = State.DISCONNECTED;
    }
    
    @Override
    public void run() {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(CONNECTION_TIMEOUT);
            socket.setBroadcast(true);
        } catch (SocketException e) {
            throw new RuntimeException("Can't setup socket: " + e);
        }

        state = State.CONNECTING;

        while (state == State.CONNECTING) {
            Data request = new Data(Data.REQUEST_JOIN);
            Data.send(socket, request);
            Data d;
            try {
                d = Data.receive(socket);
            } catch (SocketTimeoutException e) {
                state = State.CREATE;
                break;
            }
            System.out.println(d);
        }
        if (state == State.CONNECTED)
            setupConnection();
        if (state == State.CREATE)
            createNetwork();
    }
    
    private void createNetwork() {
        
    }
    
    private void setupConnection() {
        
    }
}