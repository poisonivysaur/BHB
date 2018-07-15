package com.samsung.inifile.bhb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment implements OnMapReadyCallback/*,
        GoogleApiClient.OnConnectionFailedListener*/{

    private SupportMapFragment mapFragment;
    private FloatingActionButton infoFab;
    private FloatingActionButton curLocFab;

    private static final String TAG = "HomeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("BHB");

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        infoFab = view.findViewById(R.id.fab_info);
        infoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                legendDialog();
            }
        });

        curLocFab = view.findViewById(R.id.fab_cur_loc);
        curLocFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });

        searchText = view.findViewById(R.id.search_text);

        return view;
    }

    public void legendDialog() {
        AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view = factory.inflate(R.layout.modal, null);
        alertadd.setTitle("Legend");
        alertadd.setView(view);
        alertadd.setPositiveButton("OK", null);

        alertadd.show();
    }

    private GoogleMap mMap;
    private String lastId = "";

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (((MainActivity) getActivity()).getLocationPermissionsGranted()) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    String id = marker.getId();

                    if (lastId.equals(id)) {
                        marker.hideInfoWindow();
                        lastId = "";
                    }
                    else {
                        marker.showInfoWindow();
                        lastId = id;
                    }

                    return true;
                }
            });

            DummyDB.markerIds.clear();
            for (int i = 0; i < DummyDB.markers.size(); i ++) {
                DummyDB.markerIds.add(mMap.addMarker(DummyDB.markers.get(i)).getId());
            }

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker arg0) {
                    int i = 0;

                    while (i < DummyDB.markerIds.size() && !arg0.getId().equals(DummyDB.markerIds.get(i))) {
                        i ++;
                    }

                    if (i < DummyDB.markerIds.size()) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("index", i);

                        FeedFragment feedFragment = new FeedFragment();
                        feedFragment.setArguments(bundle);

                        ((MainActivity) getActivity()).goToFeed(feedFragment);
                    }
                }
            });DummyDB.markerIds.clear();
            for (int i = 0; i < DummyDB.markers.size(); i ++) {
                DummyDB.markerIds.add(mMap.addMarker(DummyDB.markers.get(i)).getId());
            }

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker arg0) {
                    int i = 0;

                    while (i < DummyDB.markerIds.size() && !arg0.getId().equals(DummyDB.markerIds.get(i))) {
                        i ++;
                    }

                    if (i < DummyDB.markerIds.size()) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("index", i);

                        FeedFragment feedFragment = new FeedFragment();
                        feedFragment.setArguments(bundle);

                        ((MainActivity) getActivity()).goToFeed(feedFragment);
                    }
                }
            });

            searchInit();
        }

        //HIGHLIGHT ROUTES

        DummyDB.origin.add(new LatLng(14.569961, 120.991618));
        DummyDB.destination.add(new LatLng(14.563340, 120.994836));
        DummyDB.origin.add(new LatLng(14.562520, 120.994916));
        DummyDB.destination.add(new LatLng(14.566186, 121.002415));
        DummyDB.origin.add(new LatLng(14.568562, 120.990237));
        DummyDB.destination.add(new LatLng(14.564896, 120.990306));

        List<Integer> floodColors = new ArrayList<>();
        floodColors.add(getResources().getColor(R.color.red));
        floodColors.add(getResources().getColor(R.color.red_orange));
        floodColors.add(getResources().getColor(R.color.orange));
        floodColors.add(getResources().getColor(R.color.yellow_orange));
        floodColors.add(getResources().getColor(R.color.yellow));

        for (int ctr = 0; ctr < DummyDB.origin.size() && ctr < DummyDB.destination.size(); ctr ++) {
            LatLng origin = DummyDB.origin.get(ctr);

            LatLng destination = DummyDB.destination.get(ctr);

            List<LatLng> path = new ArrayList();

            //Execute Directions API request
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey("AIzaSyBrPt88vvoPDDn_imh-RzCXl5Ha2F2LYig")
                    .build();
            DirectionsApiRequest req = DirectionsApi.getDirections(context, origin.toString().substring(10).substring(0, origin.toString().substring(10).length() - 1), destination.toString().substring(10).substring(0, destination.toString().substring(10).length() - 1));
            try {
                DirectionsResult res = req.await();

                //Loop through legs and steps to get encoded polylines of each step
                if (res.routes != null && res.routes.length > 0) {
                    DirectionsRoute route = res.routes[0];

                    if (route.legs != null) {
                        for (int i = 0; i < route.legs.length; i++) {
                            DirectionsLeg leg = route.legs[i];
                            if (leg.steps != null) {
                                for (int j = 0; j < leg.steps.length; j++) {
                                    DirectionsStep step = leg.steps[j];
                                    if (step.steps != null && step.steps.length > 0) {
                                        for (int k = 0; k < step.steps.length; k++) {
                                            DirectionsStep step1 = step.steps[k];
                                            EncodedPolyline points1 = step1.polyline;
                                            if (points1 != null) {
                                                //Decode polyline and add points to list of route coordinates
                                                List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                for (com.google.maps.model.LatLng coord1 : coords1) {
                                                    path.add(new LatLng(coord1.lat, coord1.lng));
                                                    //Log.d("mamamo", "mamamo");
                                                }
                                            }
                                        }
                                    } else {
                                        EncodedPolyline points = step.polyline;
                                        if (points != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords = points.decodePath();
                                            for (com.google.maps.model.LatLng coord : coords) {
                                                path.add(new LatLng(coord.lat, coord.lng));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.getLocalizedMessage());
                //Log.d("mamamo", "mamamo");
            }

            //Draw the polyline
            Random rand = new Random();
            if (path.size() > 0) {
                PolylineOptions opts = new PolylineOptions().addAll(path).color(floodColors.get(rand.nextInt(floodColors.size()))).width(10);
                mMap.addPolyline(opts);
            }
        }
    }

    //CURRENT LOCATION
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try{
            if(((MainActivity) getActivity()).getLocationPermissionsGranted()){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            moveCamera(latLng, DEFAULT_ZOOM);
                            try{

                                MarkerOptions options = new MarkerOptions()
                                        .position(latLng)
                                        .title("YOU ARE HERE")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mMarker = mMap.addMarker(options);

                            }catch (NullPointerException e){
                                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage() );
                            }


                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private Marker mMarker;
    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        //mMap.clear();
    }

    //TEMPORARY SEARCH LOCATION
    private EditText searchText;

    public void searchInit() {
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == event.ACTION_DOWN
                        || event.getAction() == event.KEYCODE_ENTER) {

                    geoLocate();
                }
                return false;
            }
        });
    }

    //SEARCH LOCATION
    /*private AutoCompleteTextView searchText;
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    public void searchInit() {

        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(getContext(), mGoogleApiClient, LAT_LNG_BOUNDS, null);

        searchText.setAdapter(placeAutoCompleteAdapter);

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == event.ACTION_DOWN
                        || event.getAction() == event.KEYCODE_ENTER) {

                    //EXECUTE METHOD  FOR SEARCHING
                    geoLocate();
                }
                return false;
            }
        });
    }*/

    public void geoLocate(){
        String searchString = searchText.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());

        List<Address> addressList = new ArrayList<>();
        try {
            addressList = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if(addressList.size() > 0) {
            Address address = addressList.get(0);
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM);
        }
    }

    /*@Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }*/
}
