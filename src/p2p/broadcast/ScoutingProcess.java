package p2p.broadcast;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import p2p.connection.ConnectionFactory;
import p2p.util.Data;
import p2p.util.Debug;

public class ScoutingProcess implements Runnable {
    
    private enum State {
        DISCONNECTED, CONNECTING, CONNECTED, CREATE;
    }
    
    private static final int CONNECTION_TIMEOUT = 1000;
    private State state;
    DatagramSocket sendSocket;
    
    public ScoutingProcess() {
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
        
        Debug.print("Opened scouting socket, attempting to connect.");
        
        state = State.CONNECTING;
        
        while (state == State.CONNECTING) {
            Data request = new Data(Data.REQUEST_JOIN);
            Data.send(sendSocket, request);
            Data d;
            try {
                d = Data.receive(sendSocket);
            } catch (SocketTimeoutException e) {
                state = State.CREATE;
                Debug.print("No response, creating network.");
                break;
            }
            if (d.interperet().get(Data.TYPE).equals(Data.ACCEPT_JOIN)) {
                Debug.print("Response, found network.");
                state = State.CONNECTED;
            }
        }
        
        if (state == State.CONNECTED)
            joinNetwork();
        if (state == State.CREATE)
            createNetwork();
    }
    
    private void createNetwork() {
        new Thread(new BroadcastListener()).start();
    }
    
    private void joinNetwork() {
        new Thread(new ConnectionFactory()).start();
        new Thread(new BroadcastListener()).start();
    }
}