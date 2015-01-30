package org.ossabawisland.exploreossabaw.data;

/**
 * Stores data for one particular location, rather than for all locations
 * like StockData, which is used to populate the individual locations.
 * @author Matt Antonelli
 */
public class JournalLocation {

	private String key;
	private int imageId, iconId, blurbId;
	
	public JournalLocation(String key, int imageId, int iconId, int blurbId) {
		this.key = key;
		this.imageId = imageId;
		this.iconId = iconId;
		this.blurbId = blurbId;
	}

	public String getKey() {
		return key;
	}

	public int getImageId() {
		return imageId;
	}

	public int getIconId() {
		return iconId;
	}

	public int getBlurbId() {
		return blurbId;
	}
	
}
