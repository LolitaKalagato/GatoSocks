package net.typeblog.socks.util;

public interface ProxyState {
    int DISCONNECTED = 0;
    int CONNECTED = 1;
    int FAILED = 2;
}
