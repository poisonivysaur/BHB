package com.samsung.inifile.bhb;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
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
        // Inflate the layout for this fragment
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
        help = help.replace("RED", "<font color='#FF0000'>RED</font><br>");
        help = help.replace("ORANGE", "<font color='#FF5D00'>ORANGE</font><br>");
        help = help.replace("YELLOW", "<font color='#FFF600'>YELLOW</font><br>");
        help = help.replace("PINK", "<font color='#FFA0F3'>PINK</font><br>");
        help = help.replace("PURPLE", "<font color='#4F0F47'>PURPLE</font><br>");
        help = help.replace("BLUE", "<font color='#0000FF'>BLUE</font>");
        new AlertDialog.Builder(getContext())
                .setTitle("LEGEND")
                .setMessage(Html.fromHtml(help))
                .setPositiveButton("OK", null)
                .create().show();
    }
}
