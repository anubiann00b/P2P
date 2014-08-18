package p2p.util.interpreter;

import java.util.Map;
import p2p.util.Data;

public class InterpereterFirstPacket extends Interpereter {

    @Override
    public Map<String, String> interperet(String s) {
        Map<String, String> map = super.interperet(s);
        
        String connections = s.substring(2, 4);
        map.put(Data.NUM_CONNECTIONS, connections);
        
        return map;
    }
}
