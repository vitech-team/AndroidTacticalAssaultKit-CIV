
package com.atakmap.android.atak_sk42support;

import android.annotation.SuppressLint;
import android.content.Context;

import com.atakmap.android.atak_sk42support.plugin.R;
import com.atakmap.android.preference.PluginPreferenceFragment;

public class ATAK_SK42SupportSubPreferenceFragment extends PluginPreferenceFragment {

    private static Context staticPluginContext;
    public static final String TAG = "ATAK_SK42SupportPreferenceFragment";

    /**
     * Only will be called after this has been instantiated with the 1-arg constructor.
     * Fragments must have a zero arg constructor.
     */
    public ATAK_SK42SupportSubPreferenceFragment() {
        this(staticPluginContext);
    }

    @SuppressLint("ValidFragment")
    public ATAK_SK42SupportSubPreferenceFragment(final Context pluginContext) {
        super(pluginContext, R.xml.subpreferences);
        staticPluginContext = pluginContext;
    }

    @Override
    public String getSubTitle() {
        return getSubTitle("ATAK_SK42Support Preferences",
                "ATAK_SK42Support SubPreferences");
    }
}
