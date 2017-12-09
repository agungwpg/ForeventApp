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

public class UpdateEventActivity extends AppCompatActivity {

    // connn data base
    String tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;
    int success;
    ConnectivityManager conMgr;
    private String url = Server.url+"editevent.php";

    private static final String TAG = UpdateProfileActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    // share preferenes
    ///=--------------------------------------------------
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    // untuk share preference event

    public final static String TAG_ID_DETILEVENT = "id_detilevent";
    public final static String TAG_NAMA_DETILEVENT = "nama_detilevent";
    public final static String TAG_BIAYA_DETILEVENT = "biaya_detil";
    public final static String TAG_TGLMULAI_DETILEVENT="tgl_mulai_detilevent";
    public final static  String TAG_TGLAKHIR_DETILEVENT="tgl_akhir_detilevent";
    public final static  String TAG_STOK_DETILEVENT="stok_detilevent";
    public final static  String TAG_GAMBAR_DETILEENT="gambar_detilevent";
    public final static  String TAG_DESKRIPSI_DETILEVENT="deskripsi_detilevent";
    public final static  String TAG_JENIS_DETILEVENT="jenis_detilevent";
    public final static  String TAG_STATUS_AKTIF_DETILEVENT="statusaktif_detilevent";

    /// ()(&(&(&(*&(&(*&**^&^*^ ---> harus di load status aktif ketika load detil sehingga disini bisa terbaca staus aktifnya !!!!!!


    ImageView imgfotoevent;
    String hasilEncodeGambar;
    // get gambar
    private static int RESULT_LOAD_IMAGE = 1;
    ///------
    //deklarasi variable
    EditText namaevent;
    EditText biayaevent;
    DatePicker tglmulaievent;
    DatePicker tglakhirevent;
    EditText stokevent;
    EditText deskripsievent;
    Spinner jenisevent;
    Spinner statusaktifevent;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_event);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        final String  ideventSp =  sharedpreferences.getString(TAG_ID_DETILEVENT,"");
        String namaeventSp = sharedpreferences.getString(TAG_NAMA_DETILEVENT,"");
        String biayaeventSp = sharedpreferences.getString(TAG_BIAYA_DETILEVENT,"");
        String tglmulaieventSp = sharedpreferences.getString(TAG_TGLMULAI_DETILEVENT,"");
        String tglakhireventSp = sharedpreferences.getString(TAG_TGLAKHIR_DETILEVENT,"");
        String stokeventSp = sharedpreferences.getString(TAG_STOK_DETILEVENT,"");
        String gambareventSp = sharedpreferences.getString(TAG_GAMBAR_DETILEENT,"");
        String deskripsieventSp = sharedpreferences.getString(TAG_DESKRIPSI_DETILEVENT,"");
        String jeniseventSp = sharedpreferences.getString(TAG_JENIS_DETILEVENT,"");
        String statusaktifSp = sharedpreferences.getString(TAG_STATUS_AKTIF_DETILEVENT,"");
        // 1996-04-24
        String tahun1="",bulan1="",hari1="",tahun2="",bulan2="",hari2="";
        tahun1 = tglmulaieventSp.substring(0,4);
        bulan1 = tglmulaieventSp.substring(5,7);
        hari1 = tglmulaieventSp.substring(8,10);

        tahun2 = tglakhireventSp.substring(0,4);
        bulan2 = tglakhireventSp.substring(5,7);
        hari2 = tglakhireventSp.substring(8,10);

        // deklarasi variable
        ImageView btnAmbilGambar = (ImageView) findViewById(R.id.btnPilihFototambahevent);
        Button btnUpdateEvent = (Button) findViewById(R.id.btnTambahEvent);
        //---
        imgfotoevent = (ImageView) findViewById(R.id.btnPilihFototambahevent);
        namaevent=(EditText) findViewById(R.id.txtnamatambahevent);
        biayaevent = (EditText) findViewById(R.id.txtBiayaTambahEvent);
        tglmulaievent =(DatePicker) findViewById(R.id.datePickertglmulaitambahevent);
        tglakhirevent =(DatePicker) findViewById(R.id.datePickertglakhirtambahevent);
        stokevent = (EditText) findViewById(R.id.txtstoktambahevent);
        deskripsievent = (EditText) findViewById(R.id.txtdeskripsitambahevent);
        jenisevent = (Spinner) findViewById(R.id.spinnerJenisTambahevent);
        statusaktifevent = (Spinner) findViewById(R.id.spinnerStatusAktifTambahevent);

        btnUpdateEvent.setText("Update Event");


        namaevent.setText(namaeventSp);
        biayaevent.setText(biayaeventSp);
        tglmulaievent.init(Integer.parseInt(tahun1),Integer.parseInt(bulan1)-1,Integer.parseInt(hari1),null);
        tglakhirevent.init(Integer.parseInt(tahun2),Integer.parseInt(bulan2)-1,Integer.parseInt(hari2),null);
        stokevent.setText(stokeventSp);
        deskripsievent.setText(deskripsieventSp);

        int indexJenisevent;
        if(jeniseventSp.equalsIgnoreCase("sport"))
        {
            indexJenisevent = 0;
            jenisevent.setSelection(indexJenisevent);
        }
        if(jeniseventSp.equalsIgnoreCase("lingkungan"))
        {
            indexJenisevent = 1;
            jenisevent.setSelection(indexJenisevent);
        }
        if(jeniseventSp.equalsIgnoreCase("sosial"))
        {
            indexJenisevent = 2;
            jenisevent.setSelection(indexJenisevent);
        }
        if(jeniseventSp.equalsIgnoreCase("seminar"))
        {
            indexJenisevent = 3;
            jenisevent.setSelection(indexJenisevent);
        }

        //--
        int indexstatusaktifevent;
        if(statusaktifSp.equalsIgnoreCase("aktif"))
        {
            indexstatusaktifevent = 0;
            statusaktifevent.setSelection(indexstatusaktifevent);
        }
        if(statusaktifSp.equalsIgnoreCase("tidak aktif"))
        {
            indexstatusaktifevent =1;
            statusaktifevent.setSelection(indexstatusaktifevent);
        }

        if(gambareventSp!=null)
        {
            // penggesetan gambar decode dari string to bitmap
            byte[] decodedBytes = Base64.decode(gambareventSp, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            imgfotoevent.setImageBitmap(decodedBitmap);
            // set ukuran dan margin
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(300, 300);
            layoutParams.setMargins(220, 60, 0,0);
            imgfotoevent.setLayoutParams(layoutParams);
        }
        //=============================== pengecekan koneksi
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }
        btnAmbilGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        btnUpdateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // menambil data dari form layout
                String getNama = namaevent.getText().toString();
                String getBiaya= biayaevent.getText().toString();
                String getTglmulai = MengambilTgl_mulai();
                String getTglakhir = MengambilTgl_akhir();
                String getStokk = stokevent.getText().toString();
                String getGambar  = encodeFotoProfiltoString();
                String getDeskripsi = deskripsievent.getText().toString();
                String getJenis = jenisevent.getSelectedItem().toString();
                String getStatusAktif = statusaktifevent.getSelectedItem().toString();

                updateDataEvent(ideventSp,getNama,getBiaya,getTglmulai,getTglakhir,getStokk,getGambar,getDeskripsi,getJenis,getStatusAktif);

            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

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
            imgfotoevent.setImageBitmap(bmp);
            // set ukuran dan
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(300, 300);
            layoutParams.setMargins(220, 60, 0, 0);
            imgfotoevent.setLayoutParams(layoutParams);

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
    private String encodeFotoProfiltoString()
    {
        imgfotoevent.buildDrawingCache();
        Bitmap bitmap = imgfotoevent.getDrawingCache();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        String img_str = Base64.encodeToString(image, 0);
        hasilEncodeGambar=img_str;

        imgfotoevent.setImageBitmap(null);
        return hasilEncodeGambar;
    }
    private void updateDataEvent(final String id2,final String nama2,final String biaya2,final String tgl_mulai2,final String tgl_akhir2,final String stok2,final String gambar2,final String deskripsi2,final String jenis2,final String statusaktif2) {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Mengubah Data Event ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Update Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {


                        Log.e("Successfully Update!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        //----
                        // data yang sudah di ambil dari inputan parameter, dimasukan ke dalam sharepreference
                        SharedPreferences.Editor editor =  getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE).edit();

                        editor.putString(TAG_ID_DETILEVENT, id2);
                        editor.putString(TAG_NAMA_DETILEVENT, nama2);
                        editor.putString(TAG_BIAYA_DETILEVENT, biaya2);
                        editor.putString(TAG_TGLMULAI_DETILEVENT, tgl_mulai2);
                        editor.putString(TAG_TGLAKHIR_DETILEVENT,tgl_akhir2);
                        editor.putString(TAG_STOK_DETILEVENT,stok2);
                        editor.putString(TAG_GAMBAR_DETILEENT,gambar2);
                        editor.putString(TAG_DESKRIPSI_DETILEVENT,deskripsi2);
                        editor.putString(TAG_JENIS_DETILEVENT,jenis2);
                        editor.putString(TAG_STATUS_AKTIF_DETILEVENT,statusaktif2);
                        editor.commit();
                        //----

                        // Memanggil main activity
                        Intent intent = new Intent(UpdateEventActivity.this, DetilMyEventActivity.class);
                        finish();
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                    //*/
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id2);
                params.put("nama", nama2);
                params.put("biaya", biaya2);
                params.put("tgl_mulai", tgl_mulai2);
                params.put("tgl_akhir", tgl_akhir2);
                params.put("stok", stok2);
                params.put("gambar", gambar2);
                params.put("deskripsi", deskripsi2);
                params.put("jenis", jenis2);
                params.put("status_aktif", statusaktif2);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq,tag_json_obj);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    // mengambil tanggal dari datepicker
    private String MengambilTgl_mulai()
    {
        int   day  = tglmulaievent.getDayOfMonth();
        int   month= tglmulaievent.getMonth()+1;
        int   year = tglmulaievent.getYear();
        String tglmulai = null;
        //******* pengecekan format digit tgl dan bulan harus 10
        if(month<10 || day<10)
        {
            if(month<10&&day<10)
            {
                tglmulai=String.valueOf(year).toString()+"-0"+String.valueOf(month).toString()+"-0"+String.valueOf(day).toString();
            }
            else
            if(month<10)
            {
                tglmulai=String.valueOf(year).toString()+"-0"+String.valueOf(month).toString()+"-"+String.valueOf(day).toString();
            }
            else
            if(day<10)
            {
                tglmulai=String.valueOf(year).toString()+"-"+String.valueOf(month).toString()+"-0"+String.valueOf(day).toString();
            }
        }
        else
        {
            tglmulai=String.valueOf(year).toString()+"-"+String.valueOf(month).toString()+"-"+String.valueOf(day).toString();
        }
        return tglmulai;
    }

    private String MengambilTgl_akhir()
    {
        int   day  = tglakhirevent.getDayOfMonth();
        int   month= tglakhirevent.getMonth()+1;
        int   year = tglakhirevent.getYear();
        String tgl= null;
        //******* pengecekan format digit tgl dan bulan harus 10
        if(month<10 || day<10)
        {
            if(month<10&&day<10)
            {
                tgl=String.valueOf(year).toString()+"-0"+String.valueOf(month).toString()+"-0"+String.valueOf(day).toString();
            }
            else
            if(month<10)
            {
                tgl=String.valueOf(year).toString()+"-0"+String.valueOf(month).toString()+"-"+String.valueOf(day).toString();
            }
            else
            if(day<10)
            {
                tgl=String.valueOf(year).toString()+"-"+String.valueOf(month).toString()+"-0"+String.valueOf(day).toString();
            }
        }
        else
        {
            tgl=String.valueOf(year).toString()+"-"+String.valueOf(month).toString()+"-"+String.valueOf(day).toString();
        }
        return tgl;
    }

}
