package com.samsung.inifile.bhb;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.Toast;

import android.widget.SearchView;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private int navItem;
    private HomeFragment home;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final String TAG = "HomeFragment";

    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private Boolean mLocationPermissionsGranted = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if (item.getItemId() == R.id.navigation_home && navItem != 0) {
                resetStack();
                navItem = 0;
                home = new HomeFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_fragment, home).commit();
                return true;
            }
            else if (item.getItemId() == R.id.navigation_feed && navItem != 1) {
                resetStack();
                navItem = 1;
                FeedFragment feedFragment = new FeedFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_fragment, feedFragment).addToBackStack(null).commit();
                return true;
            }
            else if (item.getItemId() == R.id.navigation_scoop && navItem != 2) {
                resetStack();
                navItem = 2;
                dispatchTakePictureIntent();
                return true;
            }
            else if (item.getItemId() == R.id.navigation_rank && navItem != 3) {
                resetStack();
                navItem = 3;
                return true;
            }
            else if (item.getItemId() == R.id.navigation_profile && navItem != 4) {
                resetStack();
                navItem = 4;
                ProfileFragment profile = new ProfileFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_fragment, profile).addToBackStack(null).commit();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapsInitializer.initialize(this);
        initMarkers();

        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this,
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }
            else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Intent intent = new Intent(MainActivity.this, PhotoDetailsActivity.class);
            intent.putExtra(BundleKeys.IMAGE_BITMAP_KEY, imageBitmap);
            startActivity(intent);
            //mImageView.setImageBitmap(imageBitmap);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            finish();
                            System.exit(0);
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    public void initMap() {
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        HomeFragment home = new HomeFragment();
        fragmentManager = getSupportFragmentManager();
        resetStack();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, home).commitAllowingStateLoss();
    }

    public void resetStack() {
        int count = fragmentManager.getBackStackEntryCount();
        for(int i = 0; i < count; ++i) {
            fragmentManager.popBackStackImmediate();
        }
    }

    public boolean getLocationPermissionsGranted() {
        return mLocationPermissionsGranted;
    }

    private void initMarkers() {

        MarkerOptions options = new MarkerOptions()
                .position(new LatLng(14.562939, 120.995189))
                .title(getString(R.string.blockage))
                .icon(BitmapDescriptorFactory.defaultMarker(340))
                .snippet("Click this to check out the post");

        DummyDB.markers.add(options);

        options = new MarkerOptions()
                .position(new LatLng(14.562702, 120.995359))
                .title(getString(R.string.minor))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                .snippet("Click this to check out the post");

        DummyDB.markers.add(options);

        options = new MarkerOptions()
                .position(new LatLng(14.562628, 120.995137))
                .title(getString(R.string.major))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                .snippet("Click this to check out the post");

        DummyDB.markers.add(options);

        options = new MarkerOptions()
                .position(new LatLng(14.562453, 120.994777))
                .title(getString(R.string.rally))
                .icon(BitmapDescriptorFactory.defaultMarker(231))
                .snippet("Click this to check out the post");

        DummyDB.markers.add(options);

        options = new MarkerOptions()
                .position(new LatLng(14.562980, 120.995027))
                .title(getString(R.string.gutter))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .snippet("Click this to check out the post");

        DummyDB.markers.add(options);

        options = new MarkerOptions()
                .position(new LatLng(14.563061, 120.994938))
                .title(getString(R.string.knee))
                .icon(BitmapDescriptorFactory.defaultMarker(15))
                .snippet("Click this to check out the post");

        DummyDB.markers.add(options);

        options = new MarkerOptions()
                .position(new LatLng(14.562856, 120.995643))
                .title(getString(R.string.waist))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .snippet("Click this to check out the post");

        DummyDB.markers.add(options);

        options = new MarkerOptions()
                .position(new LatLng(14.562933, 120.995816))
                .title(getString(R.string.chest))
                .icon(BitmapDescriptorFactory.defaultMarker(11))
                .snippet("Click this to check out the post");

        DummyDB.markers.add(options);

        options = new MarkerOptions()
                .position(new LatLng(14.562830, 120.995024))
                .title(getString(R.string.neck))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .snippet("Click this to check out the post");

        DummyDB.markers.add(options);

        options = new MarkerOptions()
                .position(new LatLng(14.562493, 120.995247))
                .title(getString(R.string.blockage))
                .icon(BitmapDescriptorFactory.defaultMarker(340))
                .snippet("Click this to check out the post");

        DummyDB.markers.add(options);

        Log.d(TAG, "initMarkers: " + DummyDB.markers.size());
    }

    public void goToFeed(FeedFragment feedFragment) {
        resetStack();
        navItem = 1;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, feedFragment).addToBackStack(null).commit();
    }
}