package p2p.broadcast;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import p2p.util.Data;

public class NetworkJoin implements Runnable {
    
    private enum State {
        DISCONNECTED, CONNECTING, CONNECTED, CREATE;
    }
    
    private static final int CONNECTION_TIMEOUT = 1000;
    private State state;
    DatagramSocket sendSocket;
    BroadcastReceive broadcastRecv;
    
    public NetworkJoin() {
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
        
        broadcastRecv = new BroadcastReceive();
        
        if (state == State.CONNECTED)
            joinNetwork();
        if (state == State.CREATE)
            createNetwork();
    }
    
    private void createNetwork() {
        new Thread(new BroadcastReceive()).start();
    }
    
    private void joinNetwork() {
        
        new Thread(new BroadcastReceive()).start();
    }
}