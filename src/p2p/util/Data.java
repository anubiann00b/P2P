package p2p.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import p2p.Main;

public class Data {
    
    public static final int MAX_BUFFER = 256;
    public static final String REQUEST_JOIN = "RJ";
    private byte[] buf;
    
    public Data() {
        buf = new byte[MAX_BUFFER];
    }
    
    public Data(String s) {
        buf = s.getBytes();
    }
    
    public Data(byte[] b) {
        buf = b;
    }
    
    public static void send(DatagramSocket s, Data d) {
        try {
            s.send(new DatagramPacket(d.buf, d.buf.length, InetAddress.getByName(Main.IP), Main.PORT));
        } catch (IOException e) {
            throw new RuntimeException("Failed to send: " + e);
        }
    }
    
    public static Data receive(DatagramSocket s) throws SocketTimeoutException {
        Data d = new Data();
        try {
            s.receive(new DatagramPacket(d.buf, d.buf.length));
        } catch(SocketTimeoutException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("Failed to recieve: " + e);
        }
        return d;
    }
    
    @Override
    public String toString() {
        return new String(buf);
    }
}
