package com.tecno.wira.foreventapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tecno.wira.foreventapp.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TambahEventActivity extends AppCompatActivity {

    String tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;
    int success;
    ConnectivityManager conMgr;
    private String url = Server.url+"tambahevent.php";

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    //--- get gambar dan set gambar
    String hasilEncodeGambar;
    private static int RESULT_LOAD_IMAGE = 1;

    //==============
    // mendeklarasikan variable untuk digunakan sebagai penampung nilai dari form
    ImageView gambarevent,btngetgambar;
    Button btntambahevent;
    EditText nama,biaya,stok,deskripsi;
    DatePicker tglmulai,tglakhir;
    Spinner jenis,statusaktif;
    ///------
    String getnama,getbiaya,gettglmulai,gettglakhir,getstok,getdeskripsi,getidpengguna,getjenis,getstatusaktif;


    // pendeklarasian sahre preference digunakan untuk menampung id pengguna
    SharedPreferences sharedpreferences;
    public final static String TAG_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_event);


        //---- pengecekan koneksi
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else Toast.makeText(getApplicationContext(), "No Internet Connection",
                    Toast.LENGTH_LONG).show();
        }

        gambarevent = (ImageView) findViewById(R.id.btnPilihFototambahevent);
        btngetgambar= (ImageView) findViewById(R.id.btnPilihFototambahevent);
        nama = (EditText) findViewById(R.id.txtnamatambahevent);
        biaya = (EditText) findViewById(R.id.txtBiayaTambahEvent);
        tglmulai =(DatePicker) findViewById(R.id.datePickertglmulaitambahevent);
        tglakhir =(DatePicker) findViewById(R.id.datePickertglakhirtambahevent);
        stok = (EditText) findViewById(R.id.txtstoktambahevent);
        deskripsi =(EditText) findViewById(R.id.txtdeskripsitambahevent);
        jenis = (Spinner) findViewById(R.id.spinnerJenisTambahevent);
        statusaktif =(Spinner) findViewById(R.id.spinnerStatusAktifTambahevent);
        btntambahevent =(Button) findViewById(R.id.btnTambahEvent);

        btngetgambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        btntambahevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //===================== code untuk menambahkan event dibawah
                // ******************** menget data dari variabel form ke variable penampung
                getnama = nama.getText().toString(); // nama event
                getbiaya = biaya.getText().toString(); // biaya event
                // tgl mulaiiiiiii
                //----------------------------------
                int   day1  = tglmulai.getDayOfMonth();
                int   month1= tglmulai.getMonth() + 1;
                int   year1 = tglmulai.getYear();
                gettglmulai=String.valueOf(year1).toString()+"-"+String.valueOf(month1).toString()+"-"+String.valueOf(day1).toString();
                //----------------------------------
                // tgl akhirrrrr
                //----------------------------------
                int   day2  = tglakhir.getDayOfMonth();
                int   month2= tglakhir.getMonth() + 1;
                int   year2 = tglakhir.getYear();
                gettglakhir=String.valueOf(year2).toString()+"-"+String.valueOf(month2).toString()+"-"+String.valueOf(day2).toString();
                //----------------------------------
                getstok = stok.getText().toString(); // stok event
                getdeskripsi = deskripsi.getText().toString(); // deskripsi event
                //---------------- get id pengguna dari share prefence
                sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
                getidpengguna = sharedpreferences.getString(TAG_ID,""); // id pengguna yang memasukan event
                //---------------------
                getjenis = jenis.getSelectedItem().toString();
                getstatusaktif = statusaktif.getSelectedItem().toString();
                //**************************************************************************************

                if (conMgr.getActiveNetworkInfo() != null&& conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected())
                {
                    TambahEvent(getnama,getbiaya,gettglmulai,gettglakhir,getstok,encodeFotoeventtoString(),getdeskripsi,getidpengguna,getjenis,getstatusaktif);

                } else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    //---- mengambil gambar dari galllery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.btnPilihFototambahevent);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            gambarevent.setImageBitmap(bmp);
            // set ukuran dan
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(300, 300);
            layoutParams.setMargins(220, 60, 0,0);
            gambarevent.setLayoutParams(layoutParams);

        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    private String encodeFotoeventtoString()
    {
        gambarevent.buildDrawingCache();
        Bitmap bitmap = gambarevent.getDrawingCache();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        String img_str = Base64.encodeToString(image, 0);
        hasilEncodeGambar=img_str;

        gambarevent.setImageBitmap(null);
        return hasilEncodeGambar;
    }

    //================================================= code untuk menambahkan data event ke server ada di bawah =================

    private void TambahEvent(final String nama2, final String biaya2,final  String  tglmulai2,final String tglakhir2,final String stok2,final String gambar2,final String deskripsi2,final String idpengguna2,final String jenis2,final String statusaktif2) {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Tambah Event ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Tambah event Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {


                        Log.e("Successfully Tambah event!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        // Memanggil main activity
                        Intent i = new Intent(getApplicationContext(),MyEventActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "tambah event Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama", nama2);
                params.put("biaya", biaya2);
                params.put("tgl_mulai", tglmulai2);
                params.put("tgl_akhir", tglakhir2);
                params.put("stok", stok2);
                params.put("gambar", gambar2);
                params.put("deskripsi", deskripsi2);
                params.put("id_pengguna", idpengguna2);
                params.put("jenis", jenis2);
                params.put("status_aktif", statusaktif2);

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
