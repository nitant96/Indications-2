package gov.cipam.gi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by karan on 12/12/2017.
 */

public class NetworkUtil {

    public static boolean getConnectivityStatus(Context context) {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (null != netInfo) {
            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI)
                if (netInfo.isConnected())
                    haveConnectedWifi = true;
            if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                if (netInfo.isConnected())
                    haveConnectedMobile = true;
        }

        return haveConnectedWifi || haveConnectedMobile;
    }
}
