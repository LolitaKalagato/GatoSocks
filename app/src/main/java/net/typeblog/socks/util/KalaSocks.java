package net.typeblog.socks.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import net.typeblog.socks.IVpnService;
import net.typeblog.socks.SocksVpnService;

import java.util.ArrayList;


public class KalaSocks {

    private int port = 443;
    private static KalaSocks socks;
    private Profile mProfile;

    private IVpnService mBinder;


    private KalaSocks(){

    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName p1, IBinder binder) {
            mBinder = IVpnService.Stub.asInterface(binder);
            ProxyStatus.getInstance().publishState(ProxyState.CONNECTED);
        }

        @Override
        public void onServiceDisconnected(ComponentName p1) {
            ProxyStatus.getInstance().publishState(ProxyState.FAILED);
            mBinder = null;
        }
    };

    public static KalaSocks getInstance(){
        if(socks == null){
            socks = new KalaSocks();
        }

        return socks;
    }

    public void set(String ipString, int port, Context context) throws IllegalArgumentException {
        ProfileManager mManager = new ProfileManager(context.getApplicationContext());
        mProfile = mManager.getDefault();

        if(ipString == null || ipString.isEmpty()){
            throw new IllegalArgumentException("Please send ip-string");
        }
        if(port != 0) {
            this.port = port;
        }
        mProfile.setServer(ipString);
        mProfile.setPort(this.port);
        bindService(context);
    }

    private void bindService(Context context){
        if(context != null && mBinder == null) {
            context.bindService(new Intent(context, SocksVpnService.class), mConnection, 0);
        }
    }


    public void stopVpn(Context context) {
        if (mBinder == null)
            return;

        try {
            mBinder.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(ProxyStatus.getInstance() != null) {
            ProxyStatus.getInstance().publishState(ProxyState.DISCONNECTED);
        }
        mBinder = null;
        context.unbindService(mConnection);
    }

   public void startVpn(Context context) throws NullPointerException{
        if(mProfile == null){
            throw new NullPointerException("Please use set method to initialize the profile");
        }
       Utility.startVpn(context, mProfile);
       bindService(context);
   }





}
