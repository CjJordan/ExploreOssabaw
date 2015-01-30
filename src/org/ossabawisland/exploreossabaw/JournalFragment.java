package org.ossabawisland.exploreossabaw;

import java.util.ArrayList;
import java.util.List;

import org.ossabawisland.exploreossabaw.data.JournalDataSource;
import org.ossabawisland.exploreossabaw.data.JournalEntryItem;
import org.ossabawisland.exploreossabaw.data.JournalLocation;
import org.ossabawisland.exploreossabaw.data.StockData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * The journal fragment is where the user can access a list
 * of journal entries and edit them by tapping on the desired item
 * @author CJ Jordan
 */
public class JournalFragment extends ListFragment{

	public JournalDataSource dataSource;
	private ArrayList<JournalLocation> journalLocations;
	List<JournalEntryItem> entryList;
	
	private static final int EDITOR_ACTIVITY_REQ = 9001;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View v = inflater.inflate(R.layout.fragment_journal, null);
	    dataSource = JournalDataSource.getInstance(this.getActivity());
	    entryList = dataSource.findAll();
	    return v;
	}
	
	 @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        
	        if(journalLocations == null) {
		    	populateJournalLocations();
		    }
    
	        OssabawArrayAdapter adapter =
					new OssabawArrayAdapter(this.getActivity(), R.layout.activity_list_item, journalLocations);
			setListAdapter(adapter);	 
	    }
	
	// Populate the list of locations with all locations and their data
	private void populateJournalLocations() {
		journalLocations = new ArrayList<JournalLocation>();
		
		StockData data = StockData.getInstance();
		for(String key : data.getKeyList()) {
			JournalLocation location = new JournalLocation(key, data.getImageId(key), 
					data.getIconId(key), data.getBlurbId(key));
			journalLocations.add(location);
		}
	}

	/**
	 * creates an entry editor intent for the entry associated with
	 * the given title
	 * @param title - title of the location/ sharedPref key
	 * 
	 */
	public void openEntry(String title){
		
		//if entry already exists for this site edit
		for (JournalEntryItem entry : entryList){
			if(entry.getKey().equals(title)){
				Intent intent = new Intent(this.getActivity(), EntryEditorActivity.class);
				intent.putExtra("key", entry.getKey());
				intent.putExtra("text", entry.getText());
				startActivityForResult(intent, EDITOR_ACTIVITY_REQ); 
				return;
			}
		}
		
	}
	
	/**
	 * On click Listener: creates an entry editor intent for the entry associated with
	 * the list item that was clicked
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		JournalEntryItem entry = entryList.get(position);
		Intent intent = new Intent(this.getActivity(), EntryEditorActivity.class);
		//android sdk - local data storage to pass complex obj
		intent.putExtra("key", entry.getKey());
		intent.putExtra("text", entry.getText());
		startActivityForResult(intent, EDITOR_ACTIVITY_REQ); 
	}
	
	/**
	 * On Activity Result, the data passed back from the entry editor (the key 
	 * and all text including additions) is committed to the sharedPref
	 */

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == EDITOR_ACTIVITY_REQ && resultCode == Activity.RESULT_OK){
			JournalEntryItem entry = new JournalEntryItem();
			entry.setKey(data.getStringExtra("key"));
			entry.setText(data.getStringExtra("text"));
			dataSource.update(entry);	
			entryList = dataSource.findAll();
		}
	}

}

