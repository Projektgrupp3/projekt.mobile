package tddd36.grupp3;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Handles SIP authentication settings for the Walkie Talkie app.
 */
public class SipSettings extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.sippreferences);
    }
}