package com.tecno.wira.foreventapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Bottom Navigation Bar ===========================
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_myevents:
                    Intent intentAdd = new Intent(MainActivity.this,MyEventActivity.class);
                    startActivity(intentAdd);
                    finish();
                    return true;
                case R.id.navigation_profile:
                    Intent intentProfile = new Intent(MainActivity.this,ProfileActivity.class);
                    startActivity(intentProfile);
                    finish();
                    return true;
            }
            return false;
        }
    };
    //End Bottom Navigation Bar =========================

    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    // digunakan pada saat logout
    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";

    String TAG_JENIS_EVENT="tag_jenis_event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //==============
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //===============

        TextView imgSport = (TextView) findViewById(R.id.imgbuttonSport);
        TextView imgLingkungan =(TextView) findViewById(R.id.imgbuttonlingkungan);
        TextView imgSeminar =(TextView) findViewById(R.id.imgbuttonSeminar);
        TextView imgSosial = (TextView) findViewById(R.id.imgbuttonSosial);
        imgSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(TAG_JENIS_EVENT, "sport");
                editor.commit();
                Intent i = new Intent(getApplicationContext(),EventsActivity.class);
                startActivity(i);

            }
        });
        imgLingkungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(TAG_JENIS_EVENT, "lingkungan");
                editor.commit();
                Intent i = new Intent(getApplicationContext(),EventsActivity.class);
                startActivity(i);

            }
        });
        imgSeminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(TAG_JENIS_EVENT, "seminar");
                editor.commit();
                Intent i = new Intent(getApplicationContext(),EventsActivity.class);
                startActivity(i);

            }
        });
        imgSosial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(TAG_JENIS_EVENT, "sosial");
                editor.commit();
                Intent i = new Intent(getApplicationContext(),EventsActivity.class);
                startActivity(i);

            }
        });

    }
}
