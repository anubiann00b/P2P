package p2p.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
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
            }
        }
    }
}