package p2p;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

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
    }
}
