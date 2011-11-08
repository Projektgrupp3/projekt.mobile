package tddd36.grupp3.models;

import tddd36.grupp3.R;
import tddd36.grupp3.R.layout;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsModel extends PreferenceActivity {
	@Override

	public void onCreate(Bundle savedInstanceState) {       
		super.onCreate(savedInstanceState); 
		addPreferencesFromResource(R.layout.settings);       
	}
}
