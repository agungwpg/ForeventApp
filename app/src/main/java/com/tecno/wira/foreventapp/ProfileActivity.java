package com.tecno.wira.foreventapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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

        imgfotoprofil = (ImageView)findViewById(R.id.profilePicture);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        usernameSp = sharedpreferences.getString(TAG_USERNAME,"");
        namaSp = sharedpreferences.getString(TAG_NAMA,"");
        emailSp = sharedpreferences.getString(TAG_EMAIL,"");
        tgllahirSp = sharedpreferences.getString(TAG_TGL_LAHIR,"");
        alamatSp = sharedpreferences.getString(TAG_ALAMAT,"");
        pekerjaanSp = sharedpreferences.getString(TAG_PEKERJAAN,"");
        fotoprofilSp = sharedpreferences.getString(TAG_FOTOPROFIL,"");

        if(fotoprofilSp!=null)
        {
            // penggesetan gambar decode dari string to bitmap
            byte[] decodedBytes = Base64.decode(fotoprofilSp, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            imgfotoprofil.setImageBitmap(decodedBitmap);
            // set ukuran dan margin
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(180, 180);
            layoutParams.setMargins(20, 60, 0,0);
            imgfotoprofil.setLayoutParams(layoutParams);
        }

        //==============
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //===============
     }
}
