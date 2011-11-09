package tddd36.grupp3.views;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import tddd36.grupp3.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class ChooseUnitActivity extends Activity implements OnItemSelectedListener, OnClickListener {

	Spinner spinner;
	Button bContinue;
	ArrayList<String> unitNames = new ArrayList<String>();
	ArrayList<String> allUnits = new ArrayList<String>();
	File path = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unit);	

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, unitNames);
		spinner = (Spinner) findViewById(R.id.spinner1);
		bContinue = (Button) findViewById(R.id.bContinue);
		bContinue.setOnClickListener(this);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		//	getUnits();
		//	recieveUnits();
		//	addUnits();

	}

	protected void addUnits(){

		if(allUnits != null){
			for(String str : allUnits){
				unitNames.add(str);
			}
		}
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		int position = spinner.getSelectedItemPosition();
		switch(position){
		case 0:
			path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
			break;
		case 1:
			path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			break;
		case 2:
			path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			break;
		}
	}

	public void onNothingSelected(AdapterView<?> arg0) {
	}

	public void onClick(View v) {
		startActivity(new Intent(getBaseContext(),tddd36.grupp3.MainMapActivity.class));
	}
	protected void onPause() {
		//finishes ChooseUnitActivity activity when traveling to mainmapactivity
		super.onPause();
		finish();
	}

}
