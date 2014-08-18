package p2p.util;

public enum Action {
    
    ADD_NEW;
    
    private static Action current = null;
    
    public boolean suggestAction(Action a) {
        if (current == null) {
            current = a;
            return true;
        } else return current.equals(a);
    }
}
