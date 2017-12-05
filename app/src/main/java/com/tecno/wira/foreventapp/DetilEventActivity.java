package com.tecno.wira.foreventapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tecno.wira.foreventapp.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetilEventActivity extends AppCompatActivity {

    // con server
    ProgressDialog pDialog;
    private String urldetilevent = Server.url+"daftarevent.php";
    int success;
    String tag_json_obj = "json_obj_req";
    private static final String TAG = DetilEventActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    //--- tag untuk sp
    //--- tag dari php
    // sp dari detil event
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
    // sp dari pengguna
    public final static String TAG_ID = "id";
    // String penetu status di daftar apa tidak


    // share preference
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_event);
        // load data dari sharepreference event listview yang di klick
        sharedpreferences = getSharedPreferences(DetilEventActivity.my_shared_preferences, Context.MODE_PRIVATE);

        final String idpenggunaSp = sharedpreferences.getString(TAG_ID,"");

        final String ideventSp = sharedpreferences.getString(TAG_ID_DETILEVENT,"");
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
        ImageView imgevent = (ImageView) findViewById(R.id.imgdetilevent);
        TextView txtnama = (TextView) findViewById(R.id.txtnamaDetilevent);
        TextView txtbiaya = (TextView) findViewById(R.id.txtBiayaDetilevent);
        TextView txttglmulai = (TextView) findViewById(R.id.txttglmulaiDetilevent);
        TextView txttglakhir = (TextView) findViewById(R.id.txttglakhirDetilevent);
        TextView txtstok = (TextView) findViewById(R.id.txtStokDetilevent);
        TextView txtdeskripsi = (TextView) findViewById(R.id.txtDeskripsiDetilevent);
        TextView txtjenis = (TextView) findViewById(R.id.txtJenisEventDetilevent);
        TextView txtpenyelengara = (TextView) findViewById(R.id.txtNamaPembuatDetilevent);
        // pengesetan data detil event
        txtnama.setText(namaeventSp);
        txtbiaya.setText("Biaya : Rp"+biayaeventSp+",00");
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
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(500, 400);
            layoutParams.setMargins(40, 10, 0,0);
            imgevent.setLayoutParams(layoutParams);
        }

        //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
        Button btndaftarevent = (Button) findViewById(R.id.btnDaftarEvent);
        btndaftarevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daftarEvent(ideventSp,idpenggunaSp);

            }
        });

    }

    //*****************************************************************************************************************************
    //00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000

    private void daftarEvent(final String id_event2,final String id_peserta2) {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Mendaftar event...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST, urldetilevent, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "daftar event Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        //     Log.e("Successfully load detil event!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        //   Intent intent = new Intent(DetilEvent.this, DetilEvent.class);

                        // startActivity(intent);

                    } else if (success == 2){
                        Toast.makeText(getApplicationContext(),jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                    else if (success == 3){
                        Toast.makeText(getApplicationContext(),jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                    else
                    {Toast.makeText(getApplicationContext(),jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();}
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "daftar event Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_event", id_event2);
                params.put("id_peserta", id_peserta2);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq,tag_json_obj);
        //  AppController.getInstance().addToRequestQueue(strReq,tag_json_obj);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
