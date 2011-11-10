package tddd36.grupp3.views;

import tddd36.grupp3.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsView extends PreferenceActivity {
	@Override

	public void onCreate(Bundle savedInstanceState) {       
		super.onCreate(savedInstanceState); 
		addPreferencesFromResource(R.layout.settings);       
	}
}
