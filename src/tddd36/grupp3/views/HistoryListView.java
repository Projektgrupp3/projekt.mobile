package tddd36.grupp3.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.R;
import tddd36.grupp3.resources.Event;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryListView extends ListActivity implements Observer, View.OnTouchListener{

	private ArrayList<Event> historyList;
	private MissionHistoryAdapter historyAdapter;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.historylist);

		historyList = new ArrayList<Event>();
		historyList = MainView.db.getAllRowsAsArrayList("mission");
		historyAdapter = new MissionHistoryAdapter(this.getBaseContext(), R.layout.missionhistoryitem, historyList);
		TextView empty = new TextView(this);
		empty.setText("Inga ändringar loggade");
		getListView().setEmptyView(empty);
		setListAdapter(historyAdapter);

		MainView.db.addObserver(this);
	}
	public void update(Observable observable, final Object data) {
		if(data instanceof Event){
			runOnUiThread(new Runnable(){
				public void run() {
					if(!historyList.contains((Event)data)){
						historyList.add(((Event) data));
						historyAdapter.notifyDataSetChanged();
					}
				}
			});
		}
	}
	
	/**
	 * Method invoked when a list item is clicked. 
	 */
	public void onListItemClick(ListView parent, View v, int position, long id){
		Event ev = historyList.get(position);
		Toast.makeText(this, ev.getObjectDescription(), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Anonymous inner class for filling the mission history list with history items
	 * @author Emil
	 *
	 */
	public class MissionHistoryAdapter extends ArrayAdapter<Event>{
		private Context context;
		private TextView historyHeader;
		private TextView historyChange;
		private TextView historyTime;
		private	List<Event> items;

		public MissionHistoryAdapter(Context context, int textViewResourceId, List<Event> items) {
			super(context, textViewResourceId, items);
			this.items = items;
			this.context = context;
		}

		public int getCount() {
			return this.items.size();
		}

		public Event getItem(int index) {
			return this.items.get(index);
		}

		/**
		 * Automatically called by the list activity when populating the list with historyevents.
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater vi = (LayoutInflater)this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = vi.inflate(R.layout.missionhistoryitem, parent, false);
			}

			// Get item
			Event historyItem = getItem(position);

			// Get reference to TextView - Header
			historyHeader = (TextView) row.findViewById(R.id.historyHeader);

			// Get reference to TextView - Change
			historyChange = (TextView) row.findViewById(R.id.historyChange);

			// Get reference to TextView - Time
			historyTime = (TextView) row.findViewById(R.id.historyTime);
			//Set history header
			historyHeader.setText("Händelse-ID: "+ historyItem.getID());

			// Set history change description
			historyChange.setText("Beskrivning: "+historyItem.getMessage());

			// Set history time description
			historyTime.setText("Senast ändrad: "+ historyItem.getLastChanged());

			return row;
		}
	}

	@Override
	public void onBackPressed(){
		getParent().onBackPressed();
	}
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}
