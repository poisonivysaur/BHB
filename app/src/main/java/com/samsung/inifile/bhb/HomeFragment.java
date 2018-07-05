package com.samsung.inifile.bhb;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

public class HomeFragment extends Fragment {

    private FloatingActionButton infoFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("BHB");

        View view = inflater.inflate(R.layout.fragment_home, container, false);

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
}
