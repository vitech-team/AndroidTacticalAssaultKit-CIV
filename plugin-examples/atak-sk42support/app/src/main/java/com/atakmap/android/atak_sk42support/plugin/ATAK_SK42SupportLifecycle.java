
package com.atakmap.android.atak_sk42support.plugin;


import android.content.Context;

import com.atak.plugins.impl.AbstractPluginLifecycle;
import com.atakmap.android.atak_sk42support.ATAK_SK42SupportMapComponent;

class ATAK_SK42SupportLifecycle extends AbstractPluginLifecycle {
    public ATAK_SK42SupportLifecycle(Context ctx) {
        super(ctx, new ATAK_SK42SupportMapComponent());
    }
}
