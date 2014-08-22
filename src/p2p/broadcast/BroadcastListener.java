package p2p.broadcast;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;
import p2p.Main;
import p2p.connection.AddNodeProcess;
import p2p.connection.task.Rejection;
import p2p.util.Action;
import p2p.util.Data;
import p2p.util.Debug;

public class BroadcastListener implements Runnable {
    
    private DatagramSocket recvSocket;
    
    @Override
    public void run() {
        try {
            recvSocket = new DatagramSocket(null);
            recvSocket.setBroadcast(true);
            recvSocket.setReuseAddress(true);
            recvSocket.bind(new InetSocketAddress(Main.PORT));
        } catch (SocketException e) {
            throw new RuntimeException("Failed to create recv socket: " + e);
        }
        
        Debug.print("Starting broadcast listener, waiting for broadcasts.\n");
        
        while(true) {
            Data d;
            try {
                d = Data.receive(recvSocket);
            } catch (SocketTimeoutException e) {
                throw new RuntimeException("Socket timed out: " + e);
            }

            Debug.print("\nRecieved broadcast packet: " + d);
            Map<String, String> map = d.interperet();
            switch(map.get(Data.TYPE)) {
                case Data.REQUEST_JOIN:
                    int actionResult = Action.suggestAction(new Action(Action.Type.ADD_NEW, d.src, d.port, null));
                    if (actionResult == 1)
                        new Thread(new AddNodeProcess(recvSocket, d.src, d.port)).start();
                    else if (actionResult == -1)
                        new Thread(new Rejection(recvSocket, d.src, d.port)).start();
                    break;
            }
        }
    }
}