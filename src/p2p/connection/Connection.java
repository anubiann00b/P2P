package p2p.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import p2p.util.Action;
import p2p.util.Data;

public class Connection implements Runnable {
    
    public static final ConnectionManager MANAGER = ConnectionManager.instance;
    
    private ConnectionFactory connectionAccept;
    private Socket socket;
    private InetAddress addr;
    private int port;
    
    public Connection(InetAddress s, int p) {
        addr = s;
        port = p;
    }
    
    public Connection(Socket s, ConnectionFactory c) {
        socket = s;
        connectionAccept = c;
    }
    
    public void send(Data d) {
        try {
            socket.getOutputStream().write(d.buf);
        } catch (IOException e) {
            throw new RuntimeException("Failed to send data: " + e);
        }
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
        }
        
        InputStream recvData;
        byte[] recvBuf = new byte[Data.MAX_BUFFER];
        
        try {
            recvData = socket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get input stream: " + e);
        }
        
        while(true) {
            try {
                recvData.read(recvBuf);
            } catch (IOException e) {
                throw new RuntimeException("Failed to get read from stream: " + e);
            }

            Data d = new Data(recvBuf);
            Map<String, String> data = d.interperet();
            switch(data.get(Data.TYPE)) {
                case Data.FIRST_CONNECTION:
                    int numConnections = Integer.valueOf(data.get(Data.NUM_CONNECTIONS));
                    connectionAccept.setConnections(numConnections);
                    break;
                case Data.CONFIRM_JOIN:
                    InetAddress ip;
                    try {
                        ip = InetAddress.getByName(data.get(Data.NEW_IP));
                    } catch (UnknownHostException e) {
                        throw new RuntimeException("Failed to interperet IP: " + e);
                    }
                    
                    int newPort = Integer.valueOf((data.get(Data.NEW_PORT)));
                    if (Action.suggestAction(new Action(Action.Type.ADD_NEW, ip, newPort)))
                        send(new Data(Data.ACKNOWLEDGE));
                    else
                        send(new Data(Data.NO_ACKNOWLEDGE));
                    break;
            }
        }
    }
}