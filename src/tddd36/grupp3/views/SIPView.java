package tddd36.grupp3.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.R;
import tddd36.grupp3.models.MapModel;
import tddd36.grupp3.resources.Contact;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


public class SIPView extends ListActivity implements View.OnTouchListener, Observer{

	public Cursor cur;
	private ArrayList<Contact> contactList;
	private String[] contactNames;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactlist);
		contactList = new ArrayList<Contact>();
		contactList = MainView.db.getAllRowsAsArrayList("contacts");
		contactNames = new String[contactList.size()];
		int index = 0;
		for(Contact c : contactList){
			contactNames[index] = c.getName()+ " @ "+ c.getSipaddress();
			index++;
		}

		ContactAdapter adapter = new ContactAdapter(this.getBaseContext(), R.layout.contactitem,contactList);
		setListAdapter(adapter);
	}

	public void onListItemClick(ListView parent, View v, int position, long id){
		String[] contact = contactNames[position].split(" @ ",2);
		Toast.makeText(getBaseContext(), "Name: "+ contact[0]+"\nAddress: "+contact[1], Toast.LENGTH_SHORT).show();
		Gson gson = new Gson();
//		Intent callIntent = new Intent(getBaseContext(), MakeCall.class);

//		startActivity(callIntent);
		
		Intent callIntent = new Intent(getParent(), MakeCall.class);
 		TabGroupActivity parentActivity = (TabGroupActivity)getParent();
		callIntent.putExtra("info", contact);
 		parentActivity.startChildActivity("MakeCall", callIntent);
	}

	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public class ContactAdapter extends ArrayAdapter<Contact>{
		private Context context;
		private TextView contactName;
		private TextView contactAddress;
		private List<Contact> contacts = new ArrayList<Contact>();

		public ContactAdapter(Context context, int textViewResourceId,
				List<Contact> objects) {
			super(context, textViewResourceId, objects);
			this.context = context;
			this.contacts = objects;
		}

		public int getCount() {
			return this.contacts.size();
		}

		public Contact getItem(int index) {
			return this.contacts.get(index);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				// ROW INFLATION
				LayoutInflater inflater = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.contactitem, parent, false);
			}

			// Get item
			Contact contact = getItem(position);

			// Get reference to TextView - contactname
			contactName = (TextView) row.findViewById(R.id.contactName);

			// Get reference to TextView - contactaddress
			contactAddress = (TextView) row.findViewById(R.id.contactAddress);

			//Set contact name
			contactName.setText(contact.name);

			// Set contact sip address
			contactAddress.setText(contact.sipaddress);
			return row;
		}
	}
	/**
	 * Kallas p� n�r h�rdvaru-meny-knappen trycks in
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.settings:
			startActivity(new Intent(getBaseContext(), tddd36.grupp3.views.SettingsView.class));	
			return true;
		case R.id.status:
			//noop
			return true;
		case R.id.logout:
			final AlertDialog logout = new AlertDialog.Builder(SIPView.this).create();
			logout.setMessage("�r du s�ker p� att du vill avsluta?");
			logout.setButton("Ja", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which){
					finish();
				}
			});
			logout.setButton2("Nej", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					logout.dismiss();					
				}
			});	
			logout.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	public void onBackPressed(){
		getParent().onBackPressed();
	}

}
