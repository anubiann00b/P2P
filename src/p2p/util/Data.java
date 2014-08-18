package p2p.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import p2p.Main;
import p2p.util.interpreter.Interpereter;
import p2p.util.interpreter.InterpereterFirst;

public class Data {
    
    public static final int MAX_BUFFER = 256;
    public static final String REQUEST_JOIN = "RJ";
    public static final String ACCEPT_JOIN = "AJ";
    public static final String REJECT_JOIN = "RE";
    public static final String FIRST_PACKET = "FP";
    private static Map<String, Interpereter> interpereters;
    
    public static String NUM_CONNECTIONS = "NUM_CONNECTIONS";
    public static String TYPE = "TYPE";
    
    public static void init() {
        interpereters = new HashMap<String, Interpereter>();
        interpereters.put(FIRST_PACKET, new InterpereterFirst());
        interpereters.put(REQUEST_JOIN, new Interpereter());
        interpereters.put(ACCEPT_JOIN, new Interpereter());
        interpereters.put(REJECT_JOIN, new Interpereter());
    }
    
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
    
    public Map<String, String> interperet() {
        String s = new String(buf);
        return interpereters.get(s.substring(0, 2)).interperet(s);
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
