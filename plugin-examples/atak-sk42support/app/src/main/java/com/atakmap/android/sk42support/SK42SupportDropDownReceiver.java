
package com.atakmap.android.sk42support;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.preference.AtakPreferences;
import com.atakmap.android.sk42support.plugin.R;
import com.atakmap.android.sk42support.sk42.CoordDialogViewUpgraded;
import com.atakmap.coremap.conversions.CoordinateFormat;
import com.atakmap.coremap.maps.coords.GeoPointMetaData;

public class SK42SupportDropDownReceiver extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = SK42SupportDropDownReceiver.class.getSimpleName();

    public static final String SHOW_PLUGIN = "com.atakmap.android.sk42support.SHOW_PLUGIN";
    private final View templateView;
    private final Context pluginContext;

    /**************************** CONSTRUCTOR *****************************/

    public SK42SupportDropDownReceiver(final MapView mapView,
                                       final Context context) {
        super(mapView);
        this.pluginContext = context;

        // Remember to use the PluginLayoutInflator if you are actually inflating a custom view
        // In this case, using it is not necessary - but I am putting it here to remind
        // developers to look at this Inflator
        templateView = PluginLayoutInflater.inflate(context,
                R.layout.main_layout, null);

    }

    /**************************** PUBLIC METHODS *****************************/

    public void disposeImpl() {
    }

    /**************************** INHERITED METHODS *****************************/

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action == null)
            return;

        if (action.equals(SHOW_PLUGIN)) {
            AlertDialog.Builder b = new AlertDialog.Builder(getMapView().getContext());
            LayoutInflater inflater = LayoutInflater.from(this.pluginContext);

            final CoordDialogViewUpgraded coordView = (CoordDialogViewUpgraded) inflater.inflate(R.layout.draper_coord_dialog_upgraded, null);
            coordView.setMainAppContext(getMapView().getContext());
            b.setTitle(com.atakmap.app.R.string.rb_coord_title)
                .setView(coordView)
                .setPositiveButton(com.atakmap.app.R.string.ok,
                    (dialog, which) -> {
                        GeoPointMetaData gp = coordView.getPoint();
                        if (coordView.getResult() == CoordDialogViewUpgraded.Result.VALID_CHANGED) {
                            Toast.makeText(getMapView().getContext(), gp.toString(), Toast.LENGTH_LONG).show();
                        }

                    });
            CoordinateFormat _cFormat;
            AtakPreferences sharedPrefs = new AtakPreferences(getMapView().getContext());
            _cFormat = CoordinateFormat
                .find(sharedPrefs
                    .get(
                        "coord_display_pref",
                        getMapView().getContext()
                            .getString(
                                com.atakmap.app.R.string.coord_display_pref_default)));

            coordView.setParameters(null, MapView.getMapView().getPoint(),
                    _cFormat);

            // Overrides setPositive button onClick to keep the window open when the
            // input is invalid.
            final AlertDialog locDialog = b.create();
            locDialog.setCancelable(false);
            locDialog.show();

//            Log.d(TAG, "showing plugin drop down");
//            showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
//                    HALF_HEIGHT, false, this);
        }
    }

    @Override
    public void onDropDownSelectionRemoved() {
    }

    @Override
    public void onDropDownVisible(boolean v) {
    }

    @Override
    public void onDropDownSizeChanged(double width, double height) {
    }

    @Override
    public void onDropDownClose() {
    }

}
