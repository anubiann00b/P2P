package p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Data {
    
    public static final int MAX_BUFFER = 256;
    private byte[] buf;
    
    public Data() {
        buf = new byte[MAX_BUFFER];
    }
    
    public Data(String s) {
        buf = s.getBytes();
    }
    
    public static void send(MulticastSocket s, Data d) throws IOException {
        s.send(new DatagramPacket(d.buf, d.buf.length, InetAddress.getByName(Main.IP), Main.PORT));
    }
    
    public static Data receive(MulticastSocket s) throws IOException {
        Data d = new Data();
        s.receive(new DatagramPacket(d.buf, d.buf.length));
        return d;
    }
    
    @Override
    public String toString() {
        return new String(buf);
    }
}
