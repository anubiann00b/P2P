package p2p;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class MulticastTask implements Runnable {
    
    private enum State {
        DISCONNECTED, CONNECTING, CONNECTED, CREATE;
    }
    
    private static final int CONNECTION_TIMEOUT = 1000;
    private State state;
    DatagramSocket sendSocket;
    DatagramSocket recvSocket;
    
    public MulticastTask() {
        state = State.DISCONNECTED;
    }
    
    @Override
    public void run() {
        try {
            sendSocket = new DatagramSocket();
            sendSocket.setSoTimeout(CONNECTION_TIMEOUT);
            sendSocket.setBroadcast(true);
        } catch (SocketException e) {
            throw new RuntimeException("Can't setup socket: " + e);
        }

        state = State.CONNECTING;

        while (state == State.CONNECTING) {
            Data request = new Data(Data.REQUEST_JOIN);
            Data.send(sendSocket, request);
            Data d;
            try {
                d = Data.receive(sendSocket);
            } catch (SocketTimeoutException e) {
                state = State.CREATE;
                break;
            }
            System.out.println(d);
        }
        if (state == State.CONNECTED)
            joinNetwork();
        if (state == State.CREATE)
            createNetwork();
    }
    
    private void createNetwork() {
        try {
            recvSocket = new DatagramSocket(null);
            recvSocket.setBroadcast(true);
            recvSocket.setReuseAddress(true);
            recvSocket.bind(new InetSocketAddress(Main.PORT));
        } catch (SocketException e) {
            throw new RuntimeException("Failed to create recv socket: " + e);
        }
    }
    
    private void joinNetwork() {
        
    }
}