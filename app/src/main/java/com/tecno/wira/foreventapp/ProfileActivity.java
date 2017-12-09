package com.tecno.wira.foreventapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    public final static String TAG_EMAIL="email";
    public final static String TAG_TGL_LAHIR="tgl_lahir";
    public final static String TAG_ALAMAT="alamat";
    public final static String TAG_PEKERJAAN="pekerjaan";
    public final static String TAG_FOTOPROFIL="gambar";
    String usernameSp,namaSp,emailSp,tgllahirSp,alamatSp,pekerjaanSp,fotoprofilSp;
    Button btneditProfile, btnlogout;
    ImageView imgfotoprofil;
    // TextView unameprofil, namaprofiltab,emailprofiltab,tgllahirtab,alamattab,pekerjaantab;

//    TextView unameprofil = (TextView)findViewById(R.id.txtusernameprofil);
//    TextView namaprofiltab = (TextView)findViewById(R.id.txtnamalengkapprofiltab);
//    TextView emailprofiltab = (TextView)findViewById(R.id.txtemailprofiltab);
//    TextView tgllahirtab = (TextView)findViewById(R.id.txttglahirprofiltab);
//    TextView alamattab = (TextView)findViewById(R.id.txtalamatprofiltab);
//    TextView pekerjaantab = (TextView)findViewById(R.id.txtpekerjaanprofiltab);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        btneditProfile = (Button)findViewById(R.id.btneditprofil);
        btnlogout = (Button)findViewById(R.id.btnlogout);
        imgfotoprofil = (ImageView)findViewById(R.id.profilePicture);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        usernameSp = sharedpreferences.getString(TAG_USERNAME,"");
        namaSp = sharedpreferences.getString(TAG_NAMA,"");
        emailSp = sharedpreferences.getString(TAG_EMAIL,"");
        tgllahirSp = sharedpreferences.getString(TAG_TGL_LAHIR,"");
        alamatSp = sharedpreferences.getString(TAG_ALAMAT,"");
        pekerjaanSp = sharedpreferences.getString(TAG_PEKERJAAN,"");
        fotoprofilSp = sharedpreferences.getString(TAG_FOTOPROFIL,"");


        TextView usernameProfil =(TextView) findViewById(R.id.txtusernameprofil);
        TextView namaprofil=(TextView) findViewById(R.id.txtnamalengkapprofil);
        TextView emailprofil=(TextView) findViewById(R.id.txtemailprofil);
        TextView tgllahirprofil=(TextView) findViewById(R.id.txttglahirprofil);
        TextView alamatprofil=(TextView) findViewById(R.id.txtalamatprofil);
        TextView pekerjaanprofil=(TextView) findViewById(R.id.txtpekerjaanprofil);

        usernameProfil.setText(usernameSp);
        namaprofil.setText(namaSp);
        emailprofil.setText(emailSp);
        tgllahirprofil.setText(tgllahirSp);
        alamatprofil.setText(alamatSp);
        pekerjaanprofil.setText(pekerjaanSp);


        if(fotoprofilSp!=null)
        {
            // penggesetan gambar decode dari string to bitmap
            byte[] decodedBytes = Base64.decode(fotoprofilSp, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            imgfotoprofil.setImageBitmap(decodedBitmap);
            // set ukuran dan margin
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(450, 450);
            //layoutParams.setMargins(20, 60, 0,0);
            imgfotoprofil.setLayoutParams(layoutParams);
        }


        //==============
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //===============

        btneditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),UpdateProfileActivity.class);
                startActivity(i);
            }
        });
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Logout
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(LoginActivity.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_USERNAME, null);
                editor.commit();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });


     }
}
