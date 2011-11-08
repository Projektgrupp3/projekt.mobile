package tddd36.grupp3.models;

import tddd36.grupp3.R;
import tddd36.grupp3.R.xml;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Handles SIP authentication settings for the Walkie Talkie app.
 */
public class SIPSettingsModel extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.sippreferences);
    }
}