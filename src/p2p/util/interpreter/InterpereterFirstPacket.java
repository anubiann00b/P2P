package p2p.util.interpreter;

import java.util.HashMap;
import java.util.Map;
import p2p.util.Data;

public class InterpereterFirstPacket implements Interpereter {

    @Override
    public Map<String, String> interperet(String s) {
        Map<String, String> map = new HashMap<String, String>();
        
        String connections = s.substring(2, 4);
        map.put(Data.NUM_CONNECTIONS, connections);
        
        return map;
    }
}
