package p2p.connection;

public class ConnectionData {
    
    String raw;
    int data;
    
    ConnectionData() {
        raw = "0";
        data = 0;
    }
    
    ConnectionData(String s) {
        update(s);
    }

    void update(String s) {
        raw = s;
        data = Integer.parseInt(s);
    }
}
