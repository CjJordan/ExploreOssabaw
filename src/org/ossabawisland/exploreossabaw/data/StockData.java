package org.ossabawisland.exploreossabaw.data;

import java.util.HashMap;
import java.util.LinkedList;

import org.ossabawisland.exploreossabaw.R;


/**
 * Singleton class used for accessing various data for each location
 * @author Matt Antonelli
 * @author CJ Jordan
 */
public class StockData {
	
	private HashMap<String, Integer> images = new HashMap<String, Integer>(),
			icons = new HashMap<String, Integer>(), blurbs = new HashMap<String, Integer>();
	private LinkedList<String> keyList = new LinkedList<String>();
	private static StockData instance = new StockData();
	
	/**
	 * All values are intialized in constructor
	 */
	private StockData() {
		keyList.add("Alligators");
		keyList.add("Boarding House");
		keyList.add("Club House");
		keyList.add("Main Road");
		keyList.add("Middle Beach");
		keyList.add("Middle Place");
		keyList.add("Slave Tabbies");
		keyList.add("South Beach");

		
		
		
		images.put("Alligators", R.drawable.alligators);
		images.put("Middle Beach", R.drawable.middlebeach);
		images.put("South Beach", R.drawable.southbeach);
		images.put("Middle Place", R.drawable.middleplace);
		images.put("Main Road", R.drawable.mainroad);
		images.put("Club House", R.drawable.clubhouse);
		images.put("Boarding House", R.drawable.boardinghouse);
		images.put("Slave Tabbies", R.drawable.slavetabbies);
		
		icons.put("Alligators", R.drawable.alligators_icon);
		icons.put("Middle Beach", R.drawable.middlebeach_icon);
		icons.put("South Beach", R.drawable.southbeach_icon);
		icons.put("Middle Place", R.drawable.middleplace_icon);
		icons.put("Main Road", R.drawable.mainroad_icon);
		icons.put("Club House", R.drawable.clubhouse_icon);
		icons.put("Boarding House", R.drawable.boardinghouse_icon);
		icons.put("Slave Tabbies", R.drawable.slavetabbies_icon);
			
		blurbs.put("Alligators", R.string.alligators_desc);
		blurbs.put("Middle Beach", R.string.middle_beach_desc);
		blurbs.put("South Beach", R.string.south_beach_desc);
		blurbs.put("Middle Place", R.string.middle_place_desc);
		blurbs.put("Main Road", R.string.main_road_desc);
		blurbs.put("Club House", R.string.club_house_desc);
		blurbs.put("Boarding House", R.string.boarding_house_desc);
		blurbs.put("Slave Tabbies", R.string.slave_tabbies_desc);
	}
	
	/**
	 * Active instantiation of instance is used
	 * @return instance of StockData
	 */
	public static StockData getInstance() {
		return instance;
	}
	
	public LinkedList<String> getKeyList() {
		return keyList;
	}
	
	public int getImageId(String key){
		return images.get(key);
	}
	
	public int getIconId(String key) {
		return icons.get(key);
	}
	
	public int getBlurbId(String key){
		return blurbs.get(key);
	}

}
