package p2p.util;

import java.util.HashMap;
import java.util.Map;

@FunctionalInterface
public interface Interpreter {
    
    public static final Interpreter BASIC = s -> {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Data.TYPE, s.substring(0, 2));
        return map;
    };
    
    public static final Interpreter FIRST_CONNECTION = s -> {
        Map<String, String> map = BASIC.interpret(s);
        String connections = s.substring(2, 4).trim();
        map.put(Data.NUM_CONNECTIONS, connections);
        return map;
    };
    
    public static final Interpreter CONFIRM_JOIN = s -> {
        Map<String, String> map = BASIC.interpret(s);
        int space = s.indexOf(' ', 3);
        System.out.println(space); // CJ 192.168.1.105 64584
        String ip = s.substring(3, space);
        map.put(Data.NEW_IP, ip);
        String port = s.substring(space+1, s.length());
        map.put(Data.NEW_PORT, port);
        return map;
    };
    
    public Map<String, String> interpret(String s);
}
