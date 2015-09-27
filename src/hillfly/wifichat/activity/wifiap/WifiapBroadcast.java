package hillfly.wifichat.activity.wifiap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class WifiapBroadcast extends BroadcastReceiver {

    private NetworkInfo mNetworkInfo;
    private NetWorkChangeListener mListener;

    public WifiapBroadcast(NetWorkChangeListener listener) {
        mListener = listener;
    }

    public void onReceive(Context paramContext, Intent paramIntent) {

        // wifi open
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(paramIntent.getAction())) {
            int wifiState = paramIntent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                case WifiManager.WIFI_STATE_ENABLED:
                    mListener.wifiStatusChange();
                    break;
            }
        }

        // connect wifi
        else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(paramIntent.getAction())) {
            mNetworkInfo = paramIntent.getParcelableExtra("networkInfo");

            /**
             * 
             * 
             */
            if (mNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                mListener.WifiConnected();
            }
        }

    }

    public static abstract interface NetWorkChangeListener {

        /** Wifi connect success **/
        public abstract void WifiConnected();
        
        /** Wifi status change **/
        public abstract void wifiStatusChange();
    }
}