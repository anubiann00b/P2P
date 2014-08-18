package p2p.util;

import java.util.HashMap;
import java.util.Map;

import p2p.util.Data;

@FunctionalInterface
public interface Interpreter {
    public static final Interpreter BASIC = s -> {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Data.TYPE, s.substring(0, 2));
        return map;
    };
    public static final Interpreter FIRST_PACKET = s -> {
        Map<String, String> map = new HashMap<String, String>();
        String connections = s.substring(2, 4);
        map.put(Data.NUM_CONNECTIONS, connections);
        return map;
    };
    
    public Map<String, String> interpret(String s);
}
