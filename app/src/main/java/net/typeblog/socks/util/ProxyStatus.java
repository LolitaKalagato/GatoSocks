package net.typeblog.socks.util;


import java.util.LinkedList;
import java.util.Vector;

public class ProxyStatus  {
    private static ProxyStatus proxyStatus;
    private int state = ProxyState.DISCONNECTED;
    private static final Vector<ProxyStateListener> listeners;
    static {
        listeners = new Vector<>();
    }
    private ProxyStatus(){}

    public static ProxyStatus getInstance(){
        if(proxyStatus == null) {
            proxyStatus = new ProxyStatus();
        }
        return proxyStatus;
    }



    public synchronized void addListener(ProxyStateListener listener){
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        publishState(state);
    }



    public synchronized void publishState(int state){
        this.state = state;
        if(!ProxyStatus.listeners.isEmpty()) {
            for (ProxyStateListener stateListener : ProxyStatus.listeners){
                if(stateListener != null) {
                    stateListener.proxyStateChange(state);
                }
            }
        }
    }

}


