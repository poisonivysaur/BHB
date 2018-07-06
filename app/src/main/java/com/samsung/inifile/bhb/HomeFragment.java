package com.samsung.inifile.bhb;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private FloatingActionButton infoFab;

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


        return view;
    }

    public void legendDialog() {
        String help = "RED - Neck & above deep flood (5ft)" +
                        "ORANGE - Waist deep flood (3ft)" +
                        "YELLOW - Gutter deep flood (1ft)" +
                        "PINK - Road construction/blockage" +
                        "PURPLE - Accidents" +
                        "BLUE - Rally/Parade/Procession/Festival";
        help = help.replace("RED - Neck & above deep flood (5ft)", "<font color='#FF0000'>RED</font> - Neck & above deep flood (5ft)<br>");
        help = help.replace("ORANGE - Waist deep flood (3ft)", "<font color='#FF5D00'>ORANGE</font> - Waist deep flood (3ft)<br>");
        help = help.replace("YELLOW - Gutter deep flood (1ft)", "<font color='#FFF600'>YELLOW</font> - Gutter deep flood (1ft)<br>");
        help = help.replace("PINK - Road construction/blockage", "<font color='#FFA0F3'>PINK</font> - Road construction/blockage<br>");
        help = help.replace("PURPLE - Accidents", "<font color='#800080'>PURPLE</font> - Accidents<br>");
        help = help.replace("BLUE", "<font color='#0000FF'>BLUE</font>");
        new AlertDialog.Builder(getContext())
                .setTitle("LEGEND")
                .setMessage(Html.fromHtml(help))
                .setPositiveButton("OK", null)
                .create().show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
