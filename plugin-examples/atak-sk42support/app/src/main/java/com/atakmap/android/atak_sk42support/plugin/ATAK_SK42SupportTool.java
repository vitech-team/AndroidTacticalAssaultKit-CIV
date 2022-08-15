
package com.atakmap.android.atak_sk42support.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.atak.plugins.impl.AbstractPluginTool;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.navigation.NavButtonManager;
import com.atakmap.android.navigation.models.NavButtonModel;
import com.atakmap.util.Disposable;

public class ATAK_SK42SupportTool extends AbstractPluginTool implements Disposable {

    private final static String TAG = "ATAK_SK42SupportTool";

    public ATAK_SK42SupportTool(final Context context) {
        super(context,
                context.getString(R.string.app_name),
                context.getString(R.string.app_name),
                context.getResources().getDrawable(R.drawable.ic_launcher),
                "com.atakmap.android.atak_sk42support.SHOW_ATAK_SK42Support");

        AtakBroadcast.getInstance().registerReceiver(br,
                new AtakBroadcast.DocumentedIntentFilter(
                        "com.atakmap.android.atak_sk42support.plugin.iconcount"));

    }

    private final BroadcastReceiver br = new BroadcastReceiver() {
        private int count = 0;

        @Override
        public void onReceive(Context c, Intent intent) {
            // Get the button model used by this plugin
            NavButtonModel mdl = NavButtonManager.getInstance()
                    .getModelByPlugin(ATAK_SK42SupportTool.this);
            if (mdl != null) {
                // Increment the badge count and refresh
                mdl.setBadgeCount(++count);
                NavButtonManager.getInstance().notifyModelChanged(mdl);
                Log.d(TAG, "increment visual count to: " + count);
            }
        }
    };

    @Override
    public void dispose() {
        AtakBroadcast.getInstance().unregisterReceiver(br);
    }

}
