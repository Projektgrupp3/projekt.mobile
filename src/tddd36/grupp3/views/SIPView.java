package tddd36.grupp3.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.R;
import tddd36.grupp3.resources.Contact;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
//		Gson gson = new Gson();
//		Intent callIntent = new Intent(getBaseContext(), Call.class);
//		callIntent.putExtra("info", contact);
//		startActivity(callIntent);
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
}
