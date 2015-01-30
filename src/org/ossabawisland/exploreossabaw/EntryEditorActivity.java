package org.ossabawisland.exploreossabaw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.ossabawisland.exploreossabaw.data.JournalEntryItem;
import org.ossabawisland.exploreossabaw.data.StockData;
import org.ossabawisland.exploreossabaw.photo.PhotoData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The Entry Editor is where the user views and edits the photos and text 
 * for the select journal entry. Additionally the stock photo and text are 
 * displayed but are uneditable
 * @author CJ Jordan
 */
public class EntryEditorActivity extends Activity {
	
	private JournalEntryItem entry;
	private LinearLayout myGallery;
	private PhotoData photos;
	private TextView picturesHint;
    private ArrayList<ViewHolder> viewHolders = new ArrayList<ViewHolder>();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int  FULLSCRN_ACTIVITY_REQ = 9004;
    private File imageFile;
    private final int THUMBNAIL_SIZE = 150;
    private int count;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entry_editor);
		//create back button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//find views
		myGallery = (LinearLayout)findViewById(R.id.mygallery);
		TextView titleText = (TextView)findViewById(R.id.titleText);
		TextView blurbText = (TextView)findViewById(R.id.blurbText);
		ImageView entryImageView = (ImageView)findViewById(R.id.entryImage);
		picturesHint = (TextView) findViewById(R.id.infoHint);
		
		photos = new PhotoData(this);
		Intent intent = this.getIntent();
		
		//get entry
		entry = new JournalEntryItem();
		entry.setKey(intent.getStringExtra("key"));
		entry.setText(intent.getStringExtra("text"));
		
		//get icon and blurb
		StockData stockInfo = StockData.getInstance();
		int drawableId = stockInfo.getImageId(entry.getKey());
		int blurbId = stockInfo.getBlurbId(entry.getKey());
		
		//fill views
		EditText et = (EditText) findViewById(R.id.entryText);
		et.setText(entry.getText());
		
		titleText.setText(entry.getKey());
		
		Options options = new Options();
		options.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeResource(
				getResources(), drawableId, options);
		
		entryImageView.setImageBitmap(bitmap);
		entryImageView.setContentDescription(entry.getKey());		
		blurbText.setText(getResources().getString(blurbId));
		
		refreshGallery();    
	}
	
	
	/**
	 * refreshes the displayed images in the photo gallery after any additions or deletions 
	 * by finding all files with the key associated with this journal entry in their name
	 */
	private void refreshGallery(){
		count = 0;
		myGallery.removeAllViews();
		picturesHint.setVisibility(View.VISIBLE);
		//get existing photos
		List<String> photoList  = photos.findAll();
			// If no photos exist, display a message explaining how to add pictures
		
		if(photoList.size() == 0) return;
		
        for(String x: photoList){	
        	File file = new File(x);
        	if(file.exists()){
        		//display pictures for this site
        		boolean hasPhotos = false;
        		if(file.toString().contains(entry.getKey().replace(' ', '_'))){
        			try
                    {
        				FileInputStream fis = new FileInputStream(file);
                        Bitmap imageBitmap = BitmapFactory.decodeStream(fis);          
                        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 
                        		(int)(THUMBNAIL_SIZE / 1.1) ,THUMBNAIL_SIZE, false);
                        setPic(imageBitmap, count, file.toString());
                        hasPhotos = true;
                        count++; 
                    }
                    catch(Exception ex) {   
                    }
        			if(hasPhotos) picturesHint.setVisibility(View.INVISIBLE);
	        	}else{
	        		// File not found
	        	}	
	        }
	     }
	}

	/**
	 * Sends the key and text to be saved in the journalFragment
	 * on Activity Result being returned
	 */
	private void saveAndFinish(){
		EditText et = (EditText) findViewById(R.id.entryText);
		String entryText = et.getText().toString();
		Intent intent = new Intent();
		intent.putExtra("key", entry.getKey());
		intent.putExtra("text", entryText);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	/**
	 * call saveAndFinish on added back button press or take pic on camera button
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home){
			saveAndFinish();
		}else if(item.getItemId()==R.id.action_pic){
			dispatchTakePictureIntent();
		}
		return false;
	}
	
	/**
	 * Also saveAndFinish if device back button is pressed
	 */
	@Override
	public void onBackPressed(){
		saveAndFinish();
	}
	
	/**
	 * Accesses the primary camera on the device
	 */
		public void dispatchTakePictureIntent() {
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    // Ensure that there's a camera activity to handle the intent
		    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
		        // Create the File where the photo should go
		        File photoFile = null;
		        try {
		           photoFile = createImageFile();
		           
		        } catch (IOException ex) {
		        }
		        // Continue only if the File was successfully created
		        if (photoFile != null) {
		        	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
		            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		        }
		    }

		}
		
		/**
		 * On successfully taking a photo, this method displays the thumbnail version
		 */
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    if ((requestCode == REQUEST_IMAGE_CAPTURE  && resultCode == RESULT_OK)) {
	        	if(imageFile.exists()){
	        		//display pictures for this site
	        		
	        			try
	                    {
	                        FileInputStream fis = new FileInputStream(imageFile);
	                        Bitmap imageBitmap = BitmapFactory.decodeStream(fis);   
	                        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
	                        setPic(imageBitmap, count, imageFile.toString());
	                        picturesHint.setVisibility(View.INVISIBLE);
	                        count++;
	                    }
	                    catch(Exception ex) {
	                        
	                    }
	        	}else{
	        		// File not found
	        	}	
	        }
		    
		}
		
		/**
		 * Creates a unique name for each photo by combining the
		 * key associated with this entry and a time stamp to ensure uniqueness
		 * @return imageFile - name of the picture file
		 * @throws IOException
		 */
		@SuppressLint("SimpleDateFormat")
		public File createImageFile() throws IOException {
			Locale locale = new Locale("en_US");
			Locale.setDefault(locale);
			String pattern = "yyyy-MM-dd_HH_mm_ss";
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			String timeStamp =  entry.getKey().replace(' ', '_') + formatter.format(new Date());
			 String imageFileName = "JPEG_" + timeStamp + "_";
			    String root = Environment.getExternalStorageDirectory().toString();
			    File myDir = new File(root + "/saved_images");    
			    myDir.mkdirs();
			    String fileName =
			        imageFileName + ".jpg" ;        /* suffix */
			  
			    
			    imageFile = new File (myDir, fileName);
			    photos.update(imageFile.toString());
		   
		    return imageFile;
		}
	
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.journal, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	


	/**
	 * This method creates an ImageView for each image and 
	 * displays it in the gallery. Additionally, it provides an 
	 * on click listener for each image that displays the image in full
	 * screen mode on click and a long click list
	 * @param bit: a Bitmap image to display
	 * @param count: The location of this image in the gallery
	 * @param filename: The filename for this image
	 */
	public void setPic(Bitmap bit, int count, String filename){
		ViewHolder iv = new ViewHolder();
		iv.imageview = new ImageView(this);
	    iv.imageview.setImageBitmap(bit);
	    iv.imageview.setPadding(10,10,10,10);
	    iv.imageview.setId(count);
	    iv.filename = filename; 
	    viewHolders.add(count, iv);
	    myGallery.addView(iv.imageview);
	    
	    //display images on click
        iv.imageview.setOnClickListener(new OnClickListener() {
           public void onClick(View v) {
               int s = v.getId();
               Intent intent = new Intent(getBaseContext(), PhotoFullscreenActivity.class);
				intent.putExtra("key",viewHolders.get(s).filename);
				startActivityForResult(intent, FULLSCRN_ACTIVITY_REQ);
           }
       });
        
        
        //delete images on long click
        View.OnLongClickListener listener = new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
            	final int position = v.getId();
            	
            	AlertDialog.Builder builder = new AlertDialog.Builder(EntryEditorActivity.this);
                builder.setMessage("Are you sure you want to delete this photo?")
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { 
                    	photos.remove(viewHolders.get(position).filename);
                    	viewHolders.remove(position);
                    	refreshGallery();
                    }
                 })
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { 
                        // do nothing
                    }
                 })
                 .show();
                return true;
            }
        };
        
        iv.imageview.setOnLongClickListener(listener);
	}
	
	/**
	 * data type to hold an imageView and fileName for each image in the gallery
	 * @author cj
	 *
	 */
	class ViewHolder {
		ImageView imageview;
		String filename;
	}
}
