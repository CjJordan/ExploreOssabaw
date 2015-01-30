package org.ossabawisland.exploreossabaw;

import java.util.ArrayList;

import org.ossabawisland.exploreossabaw.data.JournalLocation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A custom array adapter used to populate the journal fragment with data from
 * each location, including picture, title, and blurb.
 * @author Matt Antonelli
 */
public class OssabawArrayAdapter extends ArrayAdapter<JournalLocation> {
	
	private ArrayList<JournalLocation> locations;
	private Options options;
	private Context context;
	private int layoutResourceId;

	public OssabawArrayAdapter(Context context, int layoutResourceId, ArrayList<JournalLocation> locations) {
		super(context, layoutResourceId, locations);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.locations = locations;
		Options options = new Options();
		options.inPurgeable = true;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
        	// Inflate the view if it isn't already inflated
        	convertView = ((Activity) context).getLayoutInflater().inflate(layoutResourceId, parent, false);
        }
        
        // Populate all of the data for the location represented by the given view
    	String title = locations.get(position).getKey();
    	
    	int drawableId = locations.get(position).getIconId();
    	int blurbId = locations.get(position).getBlurbId();
    	
    	Bitmap bitmap = BitmapFactory.decodeResource(
				convertView.getResources(), drawableId, options);
    	
    	ImageView imageView = (ImageView) convertView.findViewById(R.id.listImage);
    	imageView.setImageBitmap(bitmap);
    	imageView.setContentDescription(title);
    	
    	TextView titleView = (TextView) convertView.findViewById(R.id.listTitle);
		titleView.setText(title);
		
		TextView descView = (TextView) convertView.findViewById(R.id.listDesc);
		descView.setText(convertView.getResources().getString(blurbId));
        
        return convertView;
    }

}
