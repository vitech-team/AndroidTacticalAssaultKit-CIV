
package com.atakmap.android.atak_sk42support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

import com.atakmap.android.gui.ImportFileBrowserDialog;
import com.atakmap.android.gui.PanEditTextPreference;
import com.atakmap.android.atak_sk42support.plugin.R;
import com.atakmap.android.preference.PluginPreferenceFragment;
import com.atakmap.coremap.filesystem.FileSystemUtils;

import java.io.File;

public class ATAK_SK42SupportPreferenceFragment extends PluginPreferenceFragment {

    private static Context staticPluginContext;
    public static final String TAG = "ATAK_SK42SupportPreferenceFragment";

    /**
     * Only will be called after this has been instantiated with the 1-arg constructor.
     * Fragments must has a zero arg constructor.
     */
    public ATAK_SK42SupportPreferenceFragment() {
        super(staticPluginContext, R.xml.preferences);
    }

    @SuppressLint("ValidFragment")
    public ATAK_SK42SupportPreferenceFragment(final Context pluginContext) {
        super(pluginContext, R.xml.preferences);
        staticPluginContext = pluginContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ((PanEditTextPreference) findPreference("key_for_atak_sk42support"))
                    .checkValidInteger();
        } catch (Exception ignored) {
        }
        findPreference("test_file_browser")
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    public boolean onPreferenceClick(Preference pref) {

                        ImportFileBrowserDialog.show("Test File Browser",
                                null,
                                new String[] {
                                        ".txt"
                        },
                                new ImportFileBrowserDialog.DialogDismissed() {
                                    public void onFileSelected(
                                            final File file) {
                                        if (FileSystemUtils.isFile(file)) {
                                            Toast.makeText(getActivity(),
                                                    "file: " + file,
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    public void onDialogClosed() {
                                        //Do nothing
                                    }
                                }, ATAK_SK42SupportPreferenceFragment.this
                                        .getActivity());
                        return true;
                    }
                });

        // launch nested pref screen on click
        findPreference("nested_pref")
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference p) {
                        ATAK_SK42SupportPreferenceFragment.this
                                .showScreen(new ATAK_SK42SupportSubPreferenceFragment(
                                        staticPluginContext));
                        return true;
                    }
                });
    }

    @Override
    public String getSubTitle() {
        return getSubTitle("Tool Preferences", "ATAK_SK42Support Preferences");
    }
}
