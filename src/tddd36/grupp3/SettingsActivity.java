package tddd36.grupp3;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
	@Override

	public void onCreate(Bundle savedInstanceState) {       
		super.onCreate(savedInstanceState); 
		addPreferencesFromResource(R.layout.settings);       
	}
}
