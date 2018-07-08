package com.samsung.inifile.bhb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class PhotoDetailsActivity extends AppCompatActivity{

    private EditText caption;
    private ImageView image;
    private TextView location;

    private Switch blockage, minorAccident, majorAccident, rally;
    private RadioGroup floodLevel;

    Bitmap imageBitmap;

    private static final String TAG = "PhotoDetailsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        caption = (EditText) findViewById(R.id.caption_input);
        image = (ImageView) findViewById(R.id.image);
        location = (TextView) findViewById(R.id.location);

        blockage = findViewById(R.id.blockage);
        minorAccident = findViewById(R.id.minor_accidents);
        majorAccident = findViewById(R.id.major_accidents);
        rally = findViewById(R.id.rally);

        floodLevel = findViewById(R.id.flood_level);

        //String strBitmap = getIntent().getExtras().getString(BundleKeys.IMAGE_BITMAP_KEY, "asdf");
        Bundle extras = getIntent().getExtras();
        imageBitmap = (Bitmap) extras.get(BundleKeys.IMAGE_BITMAP_KEY);
        image.setImageBitmap(imageBitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_photo_menu, menu);
        return true;
    }

    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    //private GoogleMap mMap;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            try{
                //if(getApplicationContext().getLocationPermissionsGranted()){

                    final Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful() && task.getResult() != null){
                                //Log.d(TAG, "onComplete: found location!");
                                Location currentLocation = (Location) task.getResult();

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(PhotoDetailsActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    Log.d(TAG, "onComplete: " + currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                                DummyDB.postList.add(new Post(caption.getText().toString() + "\n" + address, imageBitmap));

                                /*moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM);*/

                                moveCamera(new LatLng(14.564638, 120.993175),
                                        DEFAULT_ZOOM);

                            }else{
                                Log.d(TAG, "onComplete: current location is null");
                                Toast.makeText(PhotoDetailsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                    Toast.makeText(this, "Posted successfully!", Toast.LENGTH_SHORT).show();
                //}
            }catch (SecurityException e){
                Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
            }


            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO check if caption is empty or not
        if(caption.getText().toString().length() > 0){
            showDeleteConfirmationDialog();
        }
        else
            super.onBackPressed();
    }

    /**
     * Prompt the user to confirm that they want to discard draft.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to discard your changes?");
        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Discard" button, so delete the draft.

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the media.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private Marker mMarker;

    private void moveCamera(LatLng latLng, float zoom){
        //Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        //mMap.clear();

        try{

            MarkerOptions options = new MarkerOptions()
                    .position(latLng);

            if (blockage.isChecked()) {
                options.title(blockage.getText().toString()).icon(BitmapDescriptorFactory.defaultMarker(340));
            } else if (minorAccident.isChecked()) {
                options.title(minorAccident.getText().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            } else if (majorAccident.isChecked()) {
                options.title(majorAccident.getText().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            } else if (rally.isChecked()) {
                options.title(rally.getText().toString()).icon(BitmapDescriptorFactory.defaultMarker(231));
            } else {
                int selectedId = floodLevel.getCheckedRadioButtonId();

                RadioButton level = findViewById(selectedId);
                options.title(level.getText().toString());

                switch (selectedId) {
                    case 0: options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            break;
                    case 1: options.icon(BitmapDescriptorFactory.defaultMarker(11));
                            break;
                    case 2: options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                            break;
                    case 3: options.icon(BitmapDescriptorFactory.defaultMarker(15));
                            break;
                    case 4: options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                            break;
                }
            }

            DummyDB.markers.add(options);

        }catch (NullPointerException e){
            //Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage() );
        }

    }
}
