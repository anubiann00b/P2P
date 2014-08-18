package p2p.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;
import p2p.util.Data;

public class Connection implements Runnable {
    
    private ConnectionAccept connectionAccept;
    private Socket socket;

    public Connection(Socket s, ConnectionAccept c) {
        socket = s;
        connectionAccept = c;
    }
    
    @Override
    public void run() {
        InputStream recvData;
        byte[] recvBuf = new byte[Data.MAX_BUFFER];
        try {
            recvData = socket.getInputStream();
            recvData.read(recvBuf);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get input stream " + e);
        }
        
        Data d = new Data(recvBuf);
        Map<String, String> data = d.interperet();
        int numConnections = Integer.valueOf(data.get(Data.NUM_CONNECTIONS));
        connectionAccept.setConnections(numConnections);
    }
}
