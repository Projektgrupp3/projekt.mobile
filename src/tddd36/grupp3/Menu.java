package tddd36.grupp3;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {

	String menuItems[] = {"LoginActivity","exemple1","exemple2",
			"exemple3","exemple4"};


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String listItem = menuItems[position];
		try {
			Class startClass;
			startClass = Class.forName("tddd36.grupp3."+listItem);
			Intent startIntent = new Intent(Menu.this, startClass);
			startActivity(startIntent);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(Menu.this, 
				android.R.layout.simple_gallery_item, menuItems));

	}

}
