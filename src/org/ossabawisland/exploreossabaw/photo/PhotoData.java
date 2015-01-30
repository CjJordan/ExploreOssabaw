package org.ossabawisland.exploreossabaw.photo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * a data type to store photos taken by the user
 *  in a SharedPreference and the photos are associated 
 *  with a particular entry by there file name which contains a 
 *  date stamp and the appropriate key
 * @author CJ Jordan
 */
public class PhotoData {
	private static final String PREFKEY = "photo";
	private SharedPreferences photoPref;
	
	public PhotoData(Context context){
		photoPref = context.getSharedPreferences(PREFKEY, Context.MODE_PRIVATE);
	}
	
	/**
	 * creates a list of photo file names from
	 * the keys and texts stored in SharedPreferences photoPrefs
	 * @return List<String> - all file names
	 */
	
	public List<String> findAll(){
		
		Map<String, ?> entryMap = photoPref.getAll();
		//Sort entries
		SortedSet<String> keys = new TreeSet<String>(entryMap.keySet());
		ArrayList<String> keyList = new ArrayList<String>();
		
		for(String x: keys){
			keyList.add(x);
		}
	
		return keyList;
	}
	
	
	/**
	 * compiles a list of all keys in the SharedPreferences photoPref
	 * @return List<String>
	 */
	
	
	public List<String> findAllKeys(){
		
		Map<String, ?> entryMap = photoPref.getAll();
		//Sort entries
		SortedSet<String> keys = new TreeSet<String>(entryMap.keySet());
		ArrayList<String> keyList = new ArrayList<String>(keys);
		return keyList;
	}
	
	
	/**
	 * commits new key/text pair to the SharedPref
	 * @param string - the pair to be added 
	 * @return  boolean indicating successful update of SharedPref entryPref
	 */
	
	public boolean update(String string){
		SharedPreferences.Editor editor = photoPref.edit();
		editor.putString(string, string);
		editor.commit();
		return true;
	}
	
	/**
	 * deletes indicated key/text pair from SharedPref
	 * @param string - the pair to be removed
	 * @return boolean indicated successful removal
	 */
	
	public boolean remove(String string){
		
		if(photoPref.contains(string)){
			SharedPreferences.Editor editor = photoPref.edit();
			editor.remove(string);
			editor.commit();
			return true;
		}
		return false;
	}

}

	
	




