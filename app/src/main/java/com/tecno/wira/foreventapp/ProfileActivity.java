package com.tecno.wira.foreventapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {

    //Bottom Navigation Bar ===========================
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentHome = new Intent(ProfileActivity.this,MainActivity.class);
                    startActivity(intentHome);
                    finish();
                    return true;
                case R.id.navigation_myevents:
                    Intent intentProfile = new Intent(ProfileActivity.this,MyEventActivity.class);
                    startActivity(intentProfile);
                    finish();
                    return true;
                case R.id.navigation_profile:

                    return true;
            }
            return false;
        }
    };
    //End Bottom Navigation Bar =========================

    SharedPreferences sharedpreferences;
    // share preference pengguna
    public final static String TAG_USERNAME = "username";
    public final static String TAG_ID = "id";
    public final static String TAG_NAMA="nama";
    public final static  String TAG_EMAIL="email";
    public final static  String TAG_TGL_LAHIR="tgl_lahir";
    public final static  String TAG_ALAMAT="alamat";
    public final static  String TAG_PEKERJAAN="pekerjaan";
    public final static  String TAG_FOTOPROFIL="gambar";
    String usernameSp,namaSp,emailSp,tgllahirSp,alamatSp,pekerjaanSp,fotoprofilSp;
    ImageView imgfotoprofil;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //==============
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //===============

    }
}
