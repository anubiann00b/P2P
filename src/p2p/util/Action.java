package p2p.util;

import java.net.InetAddress;
import java.util.Objects;

public class Action {
    
    public enum Type {
        ADD_NEW;
    }
    
    private static Action current = null;
    
    public final Type type;
    public final InetAddress ip;
    public final int port;
    
    public Action(Type a, InetAddress i, int p) {
        type = a;
        ip = i;
        port = p;
    }
    
    public static boolean suggestAction(Action a) {
        if (current == null) {
            current = a;
            return true;
        } else return current.equals(a);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o==null || !(o instanceof Action))
            return false;
        Action a = (Action) o;
        return type.equals(a.type) && ip.equals(a.ip) && port==a.port;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.type);
        hash = 83 * hash + Objects.hashCode(this.ip);
        hash = 83 * hash + this.port;
        return hash;
    }
}
