package org.ossabawisland.exploreossabaw;

import java.util.LinkedList;

import org.ossabawisland.exploreossabaw.data.JournalEntryItem;
import org.ossabawisland.exploreossabaw.data.StockData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

/**
 * The map fragment is where the user is provided with a helpful map of the island from
 * which they can explore various locations.
 * @author Matt Antonelli
 */
public class MapFragment extends Fragment {
	
	private GoogleMap map;
	private SupportMapFragment fragment;
	private JournalFragment journal;
	
	private LinkedList<Marker> markers = new LinkedList<Marker>();
	
	private Options options;
	
	private float minZoom;
	double minLat, minLng, maxLng, maxLat;
	private static final int EDITOR_ACTIVITY_REQ = 9001;
	
	// Bounding LatLng coords of the island, used to keep the user from
	// straying outside of the island
	private final LatLngBounds ossabawBounds = 
			new LatLngBounds(new LatLng(31.717644345713886, -81.15340143442154), 
					new LatLng(31.868493476485177, -81.02770507335663));
	
	/**
	 * View is created with a special touchable wrapper that is used to intercept
	 * touch events and keep the map in bounds
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the original view
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		// Create a touchable wrapper and add the view to it
	    TouchableWrapper touchView = new TouchableWrapper(getActivity());
	    touchView.addView(view);
	    journal = MainActivity.getJournal();
	    // Return the touchable view
	    return touchView;
	}
	
	/**
	 * When the activity is created, the map is initialized
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		options = new Options();
		options.inPurgeable = true;
		
		// Get the map fragment
		FragmentManager manager = getActivity().getSupportFragmentManager();
		fragment = (SupportMapFragment) manager.findFragmentById(R.id.map);
		
		// If the fragment was not initialized properly, initialize it
		if (fragment == null) {
			fragment = SupportMapFragment.newInstance();
			manager.beginTransaction().replace(R.id.map, fragment).commit();
		}
	}
	
	/**
	 * Initialize the map again on resume if isn't already initialized
	 */
	@Override
	public void onResume() {
		super.onResume();
		
		if(map == null) {
			initializeMap();
		}
		
		moveCameraInBounds();
	}

	/**
	 * Sets up the initial state of the map
	 */
	private void initializeMap() {
		// Get the map from the Google Map fragment
		map = fragment.getMap();
		
		// Wait for the map to be fully loaded, then center the camera on Ossabaw
		map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
		    @Override
		    public void onMapLoaded() {
		    	 map.moveCamera(CameraUpdateFactory.newLatLngBounds(ossabawBounds, 0));
		    	 minZoom = map.getCameraPosition().zoom;
		    	 
		    	 LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
		    	 minLat = bounds.southwest.latitude;
		    	 maxLat = bounds.northeast.latitude;
		    	 minLng = bounds.southwest.longitude;
		    	 maxLng = bounds.northeast.longitude;
		    	 
		    	 map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		    }
		});
		
		Bitmap mapImage = BitmapFactory.decodeResource(
				getResources(), R.drawable.map, options);
		
		GroundOverlayOptions ossabawMap = new GroundOverlayOptions()
			.image(BitmapDescriptorFactory.fromBitmap(mapImage))
			.positionFromBounds(ossabawBounds);
		
		map.addGroundOverlay(ossabawMap);
		
		GroundOverlay ossabawOverlay = map.addGroundOverlay(ossabawMap);
		ossabawOverlay.remove();
		
		// Set up the markers and listen for clicks
		addMarkers();
		
		// Set the custom info window for the markers
		map.setInfoWindowAdapter(new InfoAdapter());
		
		// Add listeners for the map, markers, and info windows
		map.setOnMapClickListener(new MapListener());
		map.setOnMarkerClickListener(new MarkerListener());
		map.setOnInfoWindowClickListener(new InfoWindowListener());
	}
	
	/**
	 * Adds the markers for the important locations on the island
	 */
	private void addMarkers() {
		// TODO: Consider grabbing position and title info from StockData
		
		// Club house
		Marker clubHouse = map.addMarker(new MarkerOptions()
			.position(new LatLng(31.836855, -81.089317))
			.title(getResources().getString(R.string.club_house_title)));
		
		markers.add(clubHouse);
		
		// Boarding house		
		Marker boardingHouse = map.addMarker(new MarkerOptions()
			.position(new LatLng(31.837551, -81.091422))
			.title(getResources().getString(R.string.boarding_house_title)));
		
		markers.add(boardingHouse);
		
		// Slave tabbies
		Marker slaveTabbies = map.addMarker(new MarkerOptions()
			.position(new LatLng(31.839120, -81.092040))
			.title(getResources().getString(R.string.slave_tabbies_title)));
		
		markers.add(slaveTabbies);
		
		// Middle place
		Marker middlePlace = map.addMarker(new MarkerOptions()
			.position(new LatLng(31.804060, -81.106411))
			.title(getResources().getString(R.string.middle_place_title)));
		
		markers.add(middlePlace);
		
		// Main road
		Marker mainRoad = map.addMarker(new MarkerOptions()
				.position(new LatLng(31.822372, -81.101054))
				.title(getResources().getString(R.string.main_road_title)));
		
		markers.add(mainRoad);
		
		// Middle beach
		Marker middleBeach = map.addMarker(new MarkerOptions()
			.position(new LatLng(31.770374, -81.082640))
			.title(getResources().getString(R.string.middle_beach_title)));
		
		markers.add(middleBeach);
		
		// South beach
		Marker southBeach = map.addMarker(new MarkerOptions()
			.position(new LatLng(31.743185, -81.114693))
			.title(getResources().getString(R.string.south_beach_title)));
		
		markers.add(southBeach);
		
		// Alligators
		Marker alligators = map.addMarker(new MarkerOptions()
			.position(new LatLng(31.771652, -81.116160))
			.title(getResources().getString(R.string.alligators_title)));
		
		markers.add(alligators);		
	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == EDITOR_ACTIVITY_REQ && resultCode == Activity.RESULT_OK){
			JournalEntryItem entry = new JournalEntryItem();
			entry.setKey(data.getStringExtra("key"));
			entry.setText(data.getStringExtra("text"));
			journal.dataSource.update(entry);
		}
	}
	
	/**
	 * Moves the camera back in the bounds of the island
	 */
	public void moveCameraInBounds() {
		CameraPosition position = map.getCameraPosition();
		CameraPosition newPosition = checkBounds(position);
		if(position != newPosition) {
			CameraUpdate update = CameraUpdateFactory.newCameraPosition(newPosition);
            map.animateCamera(update, 400, null);
		}
	}
	
	/**
	 * Ensures that the user does not venture outside of the bounds of the Ossabaw map
	 */
	private CameraPosition checkBounds(CameraPosition position) {
        VisibleRegion region = map.getProjection().getVisibleRegion();
        
        float zoom = 0;
        if(position.zoom < minZoom) zoom = minZoom;

        LatLng correction = getLatLngCorrection(region.latLngBounds);
        
        if(zoom != 0 || correction.latitude != 0 || correction.longitude != 0) {
        	CameraPosition newPosition;
        	if(zoom != 0) {
        		// Offset by 0.01 fixes a weird bug that disables double tapping after putting camera in bounds
        		newPosition = new CameraPosition(ossabawBounds.getCenter(), zoom + 0.01f, position.tilt, position.bearing);
        	} else {
        		double lat = position.target.latitude + correction.latitude;
                double lng = position.target.longitude + correction.longitude;
                newPosition = new CameraPosition(new LatLng(lat, lng), position.zoom, position.tilt, position.bearing);
        	}
            
        	return newPosition;
        }
        
        return position;
	}
	
	/**
	 * @return a location that is within the bounds of the map
	 */
	private LatLng getLatLngCorrection(LatLngBounds cameraBounds) {
        double latitude = 0, longitude = 0;
        if(cameraBounds.southwest.latitude < minLat) {
            latitude = minLat - cameraBounds.southwest.latitude;
        }
        if(cameraBounds.southwest.longitude < minLng) {
            longitude = minLng - cameraBounds.southwest.longitude;
        }
        if(cameraBounds.northeast.latitude > maxLat) {
            latitude = maxLat - cameraBounds.northeast.latitude;
        }
        if(cameraBounds.northeast.longitude > maxLng) {
            longitude = maxLng - cameraBounds.northeast.longitude;
        }
        return new LatLng(latitude, longitude);
    }
	
	/**
	 * Listener that will move the map in bounds when the map is clicked,
	 * specifically used for checking bounds after an info window is closed
	 */
	private class MapListener implements OnMapClickListener {
		@Override
		public void onMapClick(LatLng position) {
			moveCameraInBounds();
		}	
	}

	/**
	 * Custom marker listener that prevents map centering but still shows
	 * the info window for the marker, in order to prevent scrolling out
	 * of the bounds of the map
	 */
	private class MarkerListener implements OnMarkerClickListener {
		@Override
		public boolean onMarkerClick(Marker marker) {
			marker.showInfoWindow();
			return false;
		}
	}
	
	/**
	 * Opens the location activity corresponding with the clicked marker and
	 * closes the info window
	 */
	private class InfoWindowListener implements OnInfoWindowClickListener {
		@Override
		public void onInfoWindowClick(Marker marker) {
			marker.hideInfoWindow();
			journal.openEntry(marker.getTitle());
		}		
	}
	
	/**
	 * Custom info window for displaying attraction information
	 */
	public class InfoAdapter implements InfoWindowAdapter {
		
		private final View view;
		
		public InfoAdapter() {
			view = getActivity().getLayoutInflater().inflate(R.layout.activity_info_window, null);
		}
		
		@Override
		public View getInfoContents(final Marker marker) {
			String mTitle = marker.getTitle();
			
			ImageView imageView = (ImageView) view.findViewById(R.id.imageViewInfo);
			Bitmap bitmap = BitmapFactory.decodeResource(
					getResources(), StockData.getInstance().getIconId(mTitle), options);
			
			imageView.setImageBitmap(bitmap);
			imageView.setContentDescription(mTitle);
			
			TextView title = (TextView) view.findViewById(R.id.textViewInfoTitle);
			title.setText(mTitle);
			
			return view;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}
	}
	
	/**
	 * Used to intercept touch events on the map in order to keep the map in bounds
	 * when the user scrolls/zooms
	 */
	private class TouchableWrapper extends FrameLayout {
		
		// When true, the map will not automatically return to bounds
		private boolean isInfoWindowShown = false;
		
		public TouchableWrapper(Context context) {
			super(context);
		}
	
		@Override
		public boolean dispatchTouchEvent(MotionEvent event) {
			super.dispatchTouchEvent(event);
			
			if(event.getAction() == MotionEvent.ACTION_UP) {
				checkInfoWindows(); // Check for open info windows
				if(!isInfoWindowShown) moveCameraInBounds(); // If none open, move camera in bounds		
			}
			
			return true;
		}
		

		/**
		 * Sets the state of whether or not an info window is open.
		 */
		private void checkInfoWindows() {
			for(Marker marker : markers) {
				if(marker.isInfoWindowShown()) {
					isInfoWindowShown = true;
					return;
				}
			}
			isInfoWindowShown = false;
		}
		
	}
	
}
