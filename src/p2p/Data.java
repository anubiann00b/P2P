package p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Data {
    
    public static final int MAX_BUFFER = 256;
    private byte[] buf;
    
    public Data(String s) {
        if (s == null)
            buf = new byte[Data.MAX_BUFFER];
        else
            buf = s.getBytes();
    }
    
    public static void send(MulticastSocket s, Data d) throws IOException {
        s.send(new DatagramPacket(d.buf, d.buf.length, InetAddress.getByName("203.0.113.0"), Main.PORT));
    }
    
    public static Data receive(MulticastSocket s) throws IOException {
        Data d = new Data(null);
        s.receive(new DatagramPacket(d.buf, d.buf.length));
        return d;
    }
}
