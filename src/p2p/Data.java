package p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Data {
    
    public static final int MAX_BUFFER = 256;
    static String REQUEST_JOIN = "RJ";
    private byte[] buf;
    
    public Data() {
        buf = new byte[MAX_BUFFER];
    }
    
    public Data(String s) {
        buf = s.getBytes();
    }
    
    public static void send(DatagramSocket s, Data d) throws IOException {
        s.send(new DatagramPacket(d.buf, d.buf.length, InetAddress.getByName(Main.IP), Main.PORT));
    }
    
    public static Data receive(DatagramSocket s) throws IOException {
        Data d = new Data();
        s.receive(new DatagramPacket(d.buf, d.buf.length));
        return d;
    }
    
    @Override
    public String toString() {
        return new String(buf);
    }
}
