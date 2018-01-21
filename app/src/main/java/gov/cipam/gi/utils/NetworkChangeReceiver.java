package gov.cipam.gi.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public abstract class NetworkChangeReceiver extends BroadcastReceiver {

    public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    public void onReceive(Context context, Intent intent) {

        dismissSnackbar();

        if (intent.getAction().equals(CONNECTIVITY_CHANGE_ACTION)) {

            setUpLayout();
        }
    }

    /**
     * PURPOSE: Dismisses error snackbar when network becomes available
     */
    protected abstract void dismissSnackbar();

    /**
     * PURPOSE: Show error fragment when there is no network state
     */
    protected abstract void setUpLayout();
}
