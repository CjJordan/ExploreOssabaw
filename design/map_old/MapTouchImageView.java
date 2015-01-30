package org.ossabawisland.exploreossabaw;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MapTouchImageView extends TouchImageView {
	
	// TODO: offset values change on different screen sizes and oritentations
	// find a way to grab the top left corner of the image
	public final int xOffset = 35, yOffset = 210, touchThreshold = 20;
	// Point positions are the center of the black circle of the marker
	public PointF mainRoadPoint = new PointF(320 - xOffset, 736 - yOffset);

	public MapTouchImageView(Context context) {
        super(context);
    }

    public MapTouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    
//    @Override
//	protected void sharedConstructing(Context context) {
//    	super.sharedConstructing(context);
//    	setOnTouchListener(new MapTouchImageViewListener());
//    	
////		addPin();
////		PointF centerPoint = new PointF((float) viewWidth/2, (float) viewHeight/2);
////		pin.setPosition(curr.x, curr.y, centerPoint, centerFocus, saveScale);
//    }
    
    public class MapTouchImageViewListener extends TouchImageViewListener implements OnTouchListener {
    	@Override
        public boolean onTouch(View v, MotionEvent event) {
    		super.onTouch(v, event);
    		
    		if(event.getAction() == MotionEvent.ACTION_DOWN) {
	    		// Handle location touches here!
	    		
//	    		float x = event.getX(), y = event.getY();
//	    		PointF point = getDrawablePointFromTouchPoint(x, y);
//	    		
//	    		// Main Road point
//	    		if(isMarkerTouched(point, mainRoadPoint)) {
//	    			MainRoadDialog dialog = new MainRoadDialog(getContext());
//	        		dialog.setCanceledOnTouchOutside(true);
//	        		dialog.show();
//	    		}
    		}
    		
    		return true;
    	}
	}
    
    public boolean isMarkerTouched(PointF point, PointF marker) {
    	Log.v(null, "Point touched: " + point.x + ", " + point.y);
    	Log.v(null, "Mark location: " + marker.x + ", " + marker.y);
    	
    	return Math.abs(point.x - marker.x) < touchThreshold && 
    			Math.abs(point.y - marker.y) < touchThreshold;
    }

}
