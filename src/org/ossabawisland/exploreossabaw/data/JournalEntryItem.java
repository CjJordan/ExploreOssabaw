package org.ossabawisland.exploreossabaw.data;


/**
 * Stores data, namely a key and text, for the individual journal entries
 * 
 * @author CJ Jordan
 */
public class JournalEntryItem {

	private String key;
	private String text;
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public static JournalEntryItem getNew(String title){
		String key = title;
		JournalEntryItem entry = new JournalEntryItem();
		entry.setKey(key);
		entry.setText("");
		return entry;
		
	}
	
	@Override
	public String toString() {		
		return this.getText();
	}
}
