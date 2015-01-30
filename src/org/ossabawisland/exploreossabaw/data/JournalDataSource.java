package org.ossabawisland.exploreossabaw.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Singleton class for accessing data stored in the sharedPreferences
 * @author CJ Jordan
 */
public class JournalDataSource {
	
	private static final String PREFKEY = "ossabaw";
	private static SharedPreferences entryPref;
	private static JournalDataSource instance = new JournalDataSource();
	
	public JournalDataSource(){
	}
	
	/**
	 * SharedPreferences entryPref is instantiated and populated if null 
	 * Active instantiation of instance is used
	 * @return instance of JournalDataSource
	 */
	public static JournalDataSource getInstance(Context context) {
		entryPref = context.getSharedPreferences(PREFKEY, Context.MODE_PRIVATE);
		if (entryPref.getAll().isEmpty()){
			SharedPreferences.Editor editor = entryPref.edit();
			editor.putString("Alligators", "");
			editor.putString("Middle Beach", "");
			editor.putString("South Beach", "");
			editor.putString("Middle Place", "");
			editor.putString("Main Road", "");
			editor.putString("Club House", "");
			editor.putString("Boarding House", "");
			editor.putString("Slave Tabbies", "");
			editor.commit();
		}
		return instance;
	}
	/**
	 * creates a list of journalEntryItems from
	 * the keys and texts stored in SharedPreferences entryPrefs
	 * @return ArrayList<JournalEntryItem>
	 */
	
	public ArrayList<JournalEntryItem> findAll(){	
		Map<String, ?> entryMap = entryPref.getAll();
		//Sort entries
		SortedSet<String> keys = new TreeSet<String>(entryMap.keySet());
		ArrayList<JournalEntryItem> entryList = new ArrayList<JournalEntryItem>();
		
		for(String x: keys){
			JournalEntryItem entry = new JournalEntryItem();
			entry.setKey(x);
			entry.setText((String)entryMap.get(x));
			entryList.add(entry);
		}
	
		return entryList;
	}
	
	/**
	 * compiles a list of all keys in the SharedPreferences entryPref
	 * @return List<String>
	 */
	
	public List<String> findAllKeys(){		
		Map<String, ?> entryMap = entryPref.getAll();
		//Sort entries
		SortedSet<String> keys = new TreeSet<String>(entryMap.keySet());
		ArrayList<String> keyList = new ArrayList<String>(keys);
		return keyList;
	}
	
	/**
	 * commits new key/text pair to the SharedPref
	 * @param entry - the pair to be added 
	 * @return  boolean indicating successful update of SharedPref entryPref
	 */
	
	public boolean update(JournalEntryItem entry){
		SharedPreferences.Editor editor = entryPref.edit();
		editor.putString(entry.getKey(), entry.getText());
		editor.commit();
		return true;
	}
	
	/**
	 * deletes indicated key/text pair from SharedPref
	 * @param entry - the pair to be removed
	 * @return boolean indicated successful removal
	 */
	
	public boolean remove(JournalEntryItem entry){	
		if(entryPref.contains(entry.getKey())){
			SharedPreferences.Editor editor = entryPref.edit();
			editor.remove(entry.getKey());
			editor.commit();
			return true;
		}
		return false;
	}

}
