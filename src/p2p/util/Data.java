package p2p.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import p2p.Main;

public class Data {
    
    public static final int MAX_BUFFER = 256;
    
    public static final String REQUEST_JOIN = "RJ";
    public static final String ACCEPT_JOIN = "AJ";
    public static final String REJECT_JOIN = "RE";
    public static final String FIRST_CONNECTION = "FC";
    public static final String CONFIRM_JOIN = "CJ";
    public static final String ACKNOWLEDGE = "AK";
    public static final String NO_ACKNOWLEDGE = "NA";
    
    public static final String NUM_CONNECTIONS = "NUM_CONNECTIONS";
    public static final String TYPE = "TYPE";
    public static final String NEW_IP = "NEW_IP";
    public static String NEW_PORT = "NEW_PORT";
    
    private static Map<String, Interpreter> interpreters;
    
    public static void init() {
        interpreters = new HashMap<String, Interpreter>();
        interpreters.put(FIRST_CONNECTION, Interpreter.FIRST_CONNECTION);
        interpreters.put(REQUEST_JOIN, Interpreter.BASIC);
        interpreters.put(ACCEPT_JOIN, Interpreter.BASIC);
        interpreters.put(REJECT_JOIN, Interpreter.BASIC);
        interpreters.put(ACKNOWLEDGE, Interpreter.BASIC);
        interpreters.put(NO_ACKNOWLEDGE, Interpreter.BASIC);
        interpreters.put(CONFIRM_JOIN, Interpreter.CONFIRM_JOIN);
    }
    
    public byte[] buf;
    public InetAddress src;
    public int port;
    
    public Data() {
        buf = new byte[MAX_BUFFER];
    }
    
    public Data(String s) {
        buf = s.getBytes();
    }
    
    public Data(byte[] b) {
        buf = b;
    }
    
    public Data(String type, String data) {
        buf = (type + " " +  data).getBytes();
    }
    
    public Map<String, String> interperet() {
        String s = new String(buf);
        return interpreters.get(s.substring(0, 2)).interpret(s.trim());
    }
    
    public static void send(DatagramSocket s, InetAddress destIp, int destPort, Data d) {
        try {
            s.send(new DatagramPacket(d.buf, d.buf.length, destIp, destPort));
        } catch (IOException e) {
            throw new RuntimeException("Failed to send: " + e);
        }
    }
    
    public static void send(DatagramSocket s, Data d) {
        try {
            send(s, InetAddress.getByName(Main.IP), Main.PORT, d);
        } catch (UnknownHostException e) {
            throw new RuntimeException("Something has gone horribly wrong" + e);
        }
    }
    
    public static Data receive(DatagramSocket s) throws SocketTimeoutException {
        Data d = new Data();
        DatagramPacket dp = new DatagramPacket(d.buf, d.buf.length);
        try {
            s.receive(dp);
        } catch(SocketTimeoutException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("Failed to recieve: " + e);
        }
        d.src = dp.getAddress();
        d.port = dp.getPort();
        return d;
    }
    
    @Override
    public String toString() {
        return new String(buf).trim();
    }

    public String type() {
        return new String(buf).substring(0, 2);
    }
}