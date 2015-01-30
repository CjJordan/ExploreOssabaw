package org.ossabawisland.exploreossabaw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;


/**
 * 
 * @author CJ Jordan
 */
public class PhotoFullscreenActivity extends Activity{
	
	private ImageView iv;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);
		//get back button
		getActionBar().setDisplayHomeAsUpEnabled(true);
	
		Intent intent = this.getIntent();
		iv = (ImageView)findViewById(R.id.imageView1);
		String filename = intent.getStringExtra("key");
		File file = new File(filename);
		 FileInputStream fis;
		try {
			//display fullscreen image
			fis = new FileInputStream(file);
			Bitmap imageBitmap = BitmapFactory.decodeStream(fis);
			iv.setImageBitmap(imageBitmap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
         
		
		
	}
	
	//save and finish on back or take pic on camera button
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home){
			saveAndFinish();
		}
		return false;
	}
		
		
	private void saveAndFinish(){
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}
	
}

