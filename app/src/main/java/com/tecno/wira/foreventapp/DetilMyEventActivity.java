package com.tecno.wira.foreventapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetilMyEventActivity extends AppCompatActivity {

    //--- tag untuk sp
    // share preference
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    public final static String TAG_ID_DETILEVENT = "id_detilevent";

    public final static String TAG_NAMA_DETILEVENT = "nama_detilevent";
    public final static String TAG_BIAYA_DETILEVENT = "biaya_detil";
    public final static String TAG_TGLMULAI_DETILEVENT="tgl_mulai_detilevent";
    public final static  String TAG_TGLAKHIR_DETILEVENT="tgl_akhir_detilevent";
    public final static  String TAG_STOK_DETILEVENT="stok_detilevent";
    public final static  String TAG_GAMBAR_DETILEENT="gambar_detilevent";
    public final static  String TAG_DESKRIPSI_DETILEVENT="deskripsi_detilevent";
    public final static  String TAG_JENIS_DETILEVENT="jenis_detilevent";
    public final static  String TAG_PENYELENGARAD_ETILEVENT="penyelengara_detilevent";

    public final String TAG_JENIS_DAFTAR="tag_jenis_daftar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_my_event);

        sharedpreferences = getSharedPreferences(DetilEventActivity.my_shared_preferences, Context.MODE_PRIVATE);
        String  ideventSp =  sharedpreferences.getString(TAG_ID_DETILEVENT,"");
        String namaeventSp = sharedpreferences.getString(TAG_NAMA_DETILEVENT,"");
        String biayaeventSp = sharedpreferences.getString(TAG_BIAYA_DETILEVENT,"");
        String tglmulaieventSp = sharedpreferences.getString(TAG_TGLMULAI_DETILEVENT,"");
        String tglakhireventSp = sharedpreferences.getString(TAG_TGLAKHIR_DETILEVENT,"");
        String stokeventSp = sharedpreferences.getString(TAG_STOK_DETILEVENT,"");
        String gambareventSp = sharedpreferences.getString(TAG_GAMBAR_DETILEENT,"");
        String deskripsieventSp = sharedpreferences.getString(TAG_DESKRIPSI_DETILEVENT,"");
        String jeniseventSp = sharedpreferences.getString(TAG_JENIS_DETILEVENT,"");
        String penyelenggaraeventSp = sharedpreferences.getString(TAG_PENYELENGARAD_ETILEVENT,"");
        // deklarasi variabel
        ImageView imgevent = (ImageView) findViewById(R.id.imgdetilMyevent);
        TextView txtnama = (TextView) findViewById(R.id.txtnamaDetilMyevent);
        TextView txtbiaya = (TextView) findViewById(R.id.txtBiayaDetilMyevent);
        TextView txttglmulai = (TextView) findViewById(R.id.txttglmulaiDetilMyevent);
        TextView txttglakhir = (TextView) findViewById(R.id.txttglakhirDetilMyevent);
        TextView txtstok = (TextView) findViewById(R.id.txtStokDetilMyevent);
        TextView txtdeskripsi = (TextView) findViewById(R.id.txtDeskripsiDetilMyevent);
        TextView txtjenis = (TextView) findViewById(R.id.txtJenisEventDetilMyevent);
        TextView txtpenyelengara = (TextView) findViewById(R.id.txtNamaPembuatDetilMyevent);


        Button btnLIhatPendaftarButton = (Button) findViewById(R.id.btnlihatpendaftarMYevent);
        TextView btnlihatPermintaandaftar = (TextView) findViewById(R.id.btnlihatpermintaandaftarMyevent);
        Button btneditevent =(Button)findViewById(R.id.btnMengupdateEvent);

        txtnama.setText(namaeventSp);
        txtbiaya.setText("Biaya Event : "+biayaeventSp+",00");
        txttglmulai.setText("Tanggal mulai : "+tglmulaieventSp);
        txttglakhir.setText("Tanggal akhir event : "+tglakhireventSp);
        txtstok.setText("Kapasitas Peserta Event : "+stokeventSp);
        txtdeskripsi.setText("Desrkipsi Event : "+ deskripsieventSp);
        txtjenis.setText("Jenis Event : "+jeniseventSp);
        txtpenyelengara.setText("Penyelenggara Event : "+penyelenggaraeventSp);
        if(imgevent!=null)
        {
            // penggesetan gambar decode dari string to bitmap
            byte[] decodedBytes = Base64.decode(gambareventSp, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            imgevent.setImageBitmap(decodedBitmap);
            // set ukuran dan margin
           // RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(500, 400);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(500,400);
            layoutParams.setMargins(40, 10, 0,0);
            imgevent.setLayoutParams(layoutParams);
        }

        // btn dibawah digunakan untuk mengedit
        btneditevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),UpdateEventActivity.class);
                startActivity(i);
            }
        });

        btnLIhatPendaftarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),PendaftarMyEventActivity.class);

                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(TAG_JENIS_DAFTAR, "sudah di terima");
                editor.commit();
                startActivity(i);
            }
        });
        btnlihatPermintaandaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),PendaftarMyEventActivity.class);
                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(TAG_JENIS_DAFTAR, "belum di terima");
                editor.commit();

                startActivity(i);
            }
        });




    }
}
