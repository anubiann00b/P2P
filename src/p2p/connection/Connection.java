package p2p.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import p2p.connection.process.NetworkProcess;
import p2p.util.Action;
import p2p.util.Data;
import p2p.util.Debug;

public class Connection implements Runnable {
    
    public static final ConnectionManager MANAGER = ConnectionManager.instance;
    
    ConnectionFactory connectionAccept;
    Socket socket;
    InetAddress addr;
    int port;
    NetworkProcess currentProcess;
    
    ConnectionData connData;
    
    boolean sentConfirm = false;
    
    public Connection(InetAddress s, int p) {
        connData = new ConnectionData();
        addr = s;
        port = p;
    }
    
    public Connection(Socket s, ConnectionFactory c) {
        this(s.getInetAddress(), s.getPort());
        connectionAccept = c;
        socket = s;
    }
    
    public void processFinished() {
        currentProcess = null;
    }
    
    public void send(Data d, NetworkProcess p) {
        if (currentProcess != null)
            Debug.print("Error: currentProcess in connection not reset.");
        currentProcess = p;
        send(d);
    }
    
    public void send(Data d) {
        send(d, 0);
    }
    
    public void send(Data d, int delay) {
        new Thread(() -> {
            if (delay > 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Interrupted!? " + e);
                }
            }
            
            if (!d.type().equals(Data.DATA))
                Debug.print("Sending packet: " + d);
            
            if (d.type().equals(Data.CONFIRM_JOIN))
                sentConfirm = true;
            try {
                socket.getOutputStream().write(d.buf);
            } catch (IOException e) {
                throw new RuntimeException("Failed to send data: " + e);
            }
        }).start();
    }
    
    @Override
    public void run() {
        if (socket == null) {
            socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(addr, port));
            } catch (IOException e) {
                throw new RuntimeException("Failed to connect socket: " + e);
            }
            
            Debug.print("Connected to " + addr.getHostAddress() + ":" + port);
            Action.suggestAction(null);
            
            send(new Data(Data.FIRST_CONNECTION, String.valueOf(Connection.MANAGER.sockets.size())), 1000);
        }
        
        InputStream recvData;
        byte[] recvBuf = new byte[Data.MAX_BUFFER];
        
        try {
            recvData = socket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get input stream: " + e);
        }
        
        Debug.print("Ready to recieve.");
        
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Data d = new Data(Data.DATA, Connection.this.connData.raw);
                Connection.this.send(d);
            }
        }, 0, 2000);
        
        while(true) {
            try {
                recvData.read(recvBuf);
            } catch (IOException e) {
                throw new RuntimeException("Failed to get read from stream: " + e);
            }
            
            Data d = new Data(recvBuf);
            
            if (!d.type().equals(Data.DATA))
                Debug.print("Recieved: " + d);
            
            Map<String, String> data = d.interperet();
            switch(data.get(Data.TYPE)) {
                case Data.FIRST_CONNECTION:
                    int numConnections = Integer.valueOf(data.get(Data.NUM_CONNECTIONS));
                    connectionAccept.setConnections(numConnections);
                    break;
                case Data.CONFIRM_JOIN:
                    int actionResult;
                    try {
                        actionResult = Action.suggestAction(new Action(Action.Type.ADD_NEW,
                                InetAddress.getByName(data.get(Data.NEW_IP)),
                                Integer.valueOf(data.get(Data.NEW_PORT)), null));
                    } catch (UnknownHostException e) {
                        throw new RuntimeException("What?! " + e);
                    }
                    if (actionResult == Action.REJECTED)
                        send(new Data(Data.NO_ACKNOWLEDGE));
                    else
                        send(new Data(Data.ACKNOWLEDGE));
                    break;
                case Data.ACKNOWLEDGE:
                    if(currentProcess == null) {
                        Debug.print("Error: Recieved ACK without process.");
                        continue;
                    }
                    currentProcess.response(this, true);
                    break;
                case Data.NO_ACKNOWLEDGE:
                    if(currentProcess == null) {
                        Debug.print("Error: Recieved NACK without process.");
                        continue;
                    }
                    currentProcess.response(this, false);
                    break;
                case Data.DATA:
                    connData.update(data.get(Data.RAW_DATA));
                    break;
            }
        }
    }
}