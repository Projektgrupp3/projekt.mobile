package tddd36.grupp3.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.R;
import tddd36.grupp3.resources.Contact;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
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
import android.widget.TextView;
import android.widget.Toast;

/**
 * The SIPView tab. Contains the list of contacts and the methods needed for setting up a new call 
 * when a contact item is pressed. 
 * @author Projektgrupp 3 - Sjukvården
 *
 */
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

		ContactAdapter adapter = new ContactAdapter(this.getBaseContext(), R.layout.contactitem ,contactList);
		setListAdapter(adapter);
	}
	/**
	 * Method invoked when a list item is clicked. Starts a child activity for the SIPTabGroup.java
	 * so that when the call is ended, the user is taken back to the listactivity itself. 
	 */
	public void onListItemClick(ListView parent, View v, int position, long id){
		String[] contact = contactNames[position].split(" @ ",2);
		Intent callIntent = new Intent(getParent(), MakeCall.class);
		TabGroupActivity parentActivity = (TabGroupActivity)getParent();
		callIntent.putExtra("info", contact);
		parentActivity.startChildActivity("MakeCall", callIntent);
	}
	/**
	 * Dummy-method. Called when the SIPModel notifies its observers.
	 */
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}
	/**
	 * Dummy-method. 
	 */
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * Anonymous inner class for setting up a list of contacts and the structure of
	 * a contact item within the list itself.
	 * @author Projektgrupp 3 - Sjukvården
	 *
	 */
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
		/**
		 * Automatically called by the list activity when populating the list with contacts.
		 */
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
	 * Called when hardware "menu-button" is pressed.
	 * Inflates the mainmenu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}
	/**
	 * Called when an item is selected in the options menu
	 */
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
			getParent().onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	/**
	 * 
	 */
	public void onBackPressed(){
		getParent().onBackPressed();
	}

}
