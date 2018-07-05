package com.samsung.inifile.bhb;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toolbar;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private int navItem;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            resetStack();
            if (item.getItemId() == R.id.navigation_home && navItem != 0) {
                navItem = 0;
                HomeFragment home = new HomeFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_fragment, home).commit();
                return true;
            }
            else if (item.getItemId() == R.id.navigation_feed && navItem != 1) {
                navItem = 1;
                return true;
            }
            else if (item.getItemId() == R.id.navigation_post && navItem != 2) {
                navItem = 2;
                return true;
            }
            else if (item.getItemId() == R.id.navigation_rank && navItem != 3) {
                navItem = 3;
                return true;
            }
            else if (item.getItemId() == R.id.navigation_profile && navItem != 4) {
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
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        HomeFragment home = new HomeFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, home).commit();
    }

    public void resetStack() {
        int count = fragmentManager.getBackStackEntryCount();
        for(int i = 0; i < count; ++i) {
            fragmentManager.popBackStackImmediate();
        }
    }
}
