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
            buf = new byte[MAX_BUFFER];
        else
            buf = s.getBytes();
    }
    
    public static void send(MulticastSocket s, Data d) throws IOException {
        s.send(new DatagramPacket(d.buf, d.buf.length, InetAddress.getByName(Main.IP), Main.PORT));
    }
    
    public static Data receive(MulticastSocket s) throws IOException {
        Data d = new Data(null); //Why not use a constructor that takes 0 args?
        s.receive(new DatagramPacket(d.buf, d.buf.length));
        return d;
    }
}
