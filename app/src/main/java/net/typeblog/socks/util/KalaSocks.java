package net.typeblog.socks.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import net.typeblog.socks.IVpnService;


public class KalaSocks {

    private String ipString;
    private int port = 443;
    private static KalaSocks socks;
    private Profile mProfile;

    private IVpnService mBinder;
    private boolean mRunning = false;
    private ProfileManager mManager;
    private KalaSocks(){

    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName p1, IBinder binder) {
            mBinder = IVpnService.Stub.asInterface(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName p1) {
            mBinder = null;
        }
    };

    public static KalaSocks getInstance(){
        if(socks == null){
            socks = new KalaSocks();
        }

        return socks;
    }

    public void set(String ipString, int port, Context context){
        mManager = new ProfileManager(context.getApplicationContext());
        mProfile = mManager.getDefault();

        if(ipString == null || ipString.isEmpty()){
            throw new IllegalArgumentException("Please send ip-string");
        }
        this.ipString = ipString;
        if(port != 0) {
            this.port = port;
        }
        mProfile.setServer(ipString);
        mProfile.setPort(port);
    }


    public void stopVpn(Context context) {
        if (mBinder == null)
            return;

        try {
            mBinder.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBinder = null;
        context.unbindService(mConnection);
    }

   public void startVpn(Context context){
       Utility.startVpn(context, mProfile);
   }





}