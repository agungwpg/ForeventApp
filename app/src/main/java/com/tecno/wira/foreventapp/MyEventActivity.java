package com.tecno.wira.foreventapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MyEventActivity extends AppCompatActivity {

    //Bottom Navigation Bar ===========================
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentHome = new Intent(MyEventActivity.this,MainActivity.class);
                    startActivity(intentHome);
                    finish();
                    return true;
                case R.id.navigation_myevents:

                    return true;
                case R.id.navigation_profile:
                    Intent intentProfile = new Intent(MyEventActivity.this,ProfileActivity.class);
                    startActivity(intentProfile);
                    finish();
                    return true;
            }
            return false;
        }
    };
    //End Bottom Navigation Bar =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);

        //==============
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //===============

    }
}
