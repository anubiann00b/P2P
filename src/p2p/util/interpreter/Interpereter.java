package p2p.util.interpreter;

import java.util.HashMap;
import java.util.Map;
import p2p.util.Data;

public class Interpereter {
    
    public Map<String, String> interperet(String s) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Data.TYPE, s.substring(0, 2));
        return map;
    }
}
