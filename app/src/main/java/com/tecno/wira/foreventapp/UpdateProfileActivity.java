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
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wira on 12/6/17.
 */


public class UpdateProfileActivity  extends AppCompatActivity {

    // connn data base
    String tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;
    int success;
    ConnectivityManager conMgr;
    private String url = Server.url+"editprofil.php";

    private static final String TAG = UpdateProfileActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    // share preferenes
    SharedPreferences sharedpreferences;
    // untuk share preference pengguna
    public final static String TAG_ID = "id";
    public final static String TAG_USERNAME = "username";
    public final static String TAG_NAMA="nama";
    public final static  String TAG_EMAIL="email";
    public final static  String TAG_TGL_LAHIR="tgl_lahir";
    public final static  String TAG_ALAMAT="alamat";
    public final static  String TAG_PEKERJAAN="pekerjaan";
    public final static  String TAG_FOTOPROFIL="gambar";
    String idSp,usernameSp,namaSp,emailSp,tgllahirSp,alamatSp,pekerjaanSp,fotoprofilSp;
    ImageView imgfotoprofil,btnAmbilGambar,btnUpdateProfil;
    String hasilEncodeGambar;
    // get gambar
    private static int RESULT_LOAD_IMAGE = 1;
    ///------
    //deklarasi variable
    TextView namaprofil;
    TextView usernameProfil;
    TextView emailprofil;
    TextView alamatprofil;
    TextView pekerjaanprofil;
    DatePicker tgllahirprpfil;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // get dari share preference
        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        idSp = sharedpreferences.getString(TAG_ID,"");
        usernameSp = sharedpreferences.getString(TAG_USERNAME,"");
        namaSp = sharedpreferences.getString(TAG_NAMA,"");
        emailSp = sharedpreferences.getString(TAG_EMAIL,"");
        alamatSp = sharedpreferences.getString(TAG_ALAMAT,"");
        pekerjaanSp = sharedpreferences.getString(TAG_PEKERJAAN,"");
        fotoprofilSp = sharedpreferences.getString(TAG_FOTOPROFIL,"");
        tgllahirSp = sharedpreferences.getString(TAG_TGL_LAHIR,"");

        // 1996-04-24
        String tahun="",bulan="",hari="";
        tahun = tgllahirSp.substring(0,4);
        bulan = tgllahirSp.substring(5,7);
        hari = tgllahirSp.substring(8,10);
        // deklarasi variable
        EditText password = (EditText)findViewById(R.id.txtpasswordregister);
        password.setVisibility(View.GONE);
        ImageView btnAmbilGambar = (ImageView) findViewById(R.id.btnPilihFoto);
        Button btnUpdateProfil = (Button) findViewById(R.id.btnRegister);
        btnUpdateProfil.setText("Update Profil");
        TextView linklogin = (TextView)findViewById(R.id.link_login);
        linklogin.setVisibility(View.GONE);
        imgfotoprofil = (ImageView) findViewById(R.id.btnPilihFoto);
        namaprofil=(EditText) findViewById(R.id.txtnamaregister);
        usernameProfil =(EditText) findViewById(R.id.txtusernameregister);
        emailprofil=(EditText) findViewById(R.id.txtemailregister);
        alamatprofil=(EditText) findViewById(R.id.txtalamatregister);
        pekerjaanprofil=(EditText) findViewById(R.id.txtpekerjaanregister);
        tgllahirprpfil = (DatePicker) findViewById(R.id.tgllhirregister);

        tgllahirprpfil.init(Integer.parseInt(tahun),Integer.parseInt(bulan)-1,Integer.parseInt(hari),null);
        namaprofil.setText(namaSp);
        usernameProfil.setText(usernameSp);
        emailprofil.setText(emailSp);
        alamatprofil.setText(alamatSp);
        pekerjaanprofil.setText(pekerjaanSp);

        if(fotoprofilSp!=null)
        {
            // penggesetan gambar decode dari string to bitmap
            byte[] decodedBytes = Base64.decode(fotoprofilSp,Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            imgfotoprofil.setImageBitmap(decodedBitmap);
            // set ukuran dan margin
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 300);
            layoutParams.setMargins(220, 60, 0,0);
            imgfotoprofil.setLayoutParams(layoutParams);
        }
        //===============================

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

        btnUpdateProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                String GetNamaLengkap = namaprofil.getText().toString();
                String GetUsername= usernameProfil.getText().toString();
                String GetEmail = emailprofil.getText().toString();
                int   day  = tgllahirprpfil.getDayOfMonth();
                int   month= tgllahirprpfil.getMonth() + 1;
                int   year = tgllahirprpfil.getYear();
                String Gettgllahir=String.valueOf(year).toString()+"-"+String.valueOf(month).toString()+"-"+String.valueOf(day).toString();
                String GetAlamat= alamatprofil.getText().toString();
                String GetPekerjaan=pekerjaanprofil.getText().toString();
                //---


                if (conMgr.getActiveNetworkInfo() != null&& conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected())
                {
                    updateDataProfil(GetNamaLengkap,GetUsername,GetEmail,Gettgllahir,GetAlamat,GetPekerjaan,encodeFotoProfiltoString(),idSp);

                } else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }


            }
        });
    }
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

            ImageView imageView = (ImageView) findViewById(R.id.btnPilihFoto);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            imgfotoprofil.setImageBitmap(bmp);
            // set ukuran dan
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 300);
            layoutParams.setMargins(220, 60, 0,0);
            imgfotoprofil.setLayoutParams(layoutParams);

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
        imgfotoprofil.buildDrawingCache();
        Bitmap bitmap = imgfotoprofil.getDrawingCache();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        String img_str = Base64.encodeToString(image, 0);
        hasilEncodeGambar=img_str;

        imgfotoprofil.setImageBitmap(null);
        return hasilEncodeGambar;
    }
    ////--------------- update data pengguna
    private void updateDataProfil(final String nama2, final String username2,final String email2,final String tgl_lahir2,final String alamat2,final String pekerjaan2,final String gambar2,final String id2) {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Mengubah Data Pengguna ...");
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
                        //
                        String GetNamaLengkap = namaprofil.getText().toString();
                        String GetUsername= usernameProfil.getText().toString();
                        String GetEmail = emailprofil.getText().toString();
                        int   day  = tgllahirprpfil.getDayOfMonth();
                        int   month= tgllahirprpfil.getMonth()+1;
                        int   year = tgllahirprpfil.getYear();
                        String Gettgllahir = null;
                        //******* pengecekan format digit tgl dan bulan harus 10
                        if(month<10 || day<10)
                        {
                            if(month<10&&day<10)
                            {
                                Gettgllahir=String.valueOf(year).toString()+"-0"+String.valueOf(month).toString()+"-0"+String.valueOf(day).toString();
                            }
                            else
                            if(month<10)
                            {
                                Gettgllahir=String.valueOf(year).toString()+"-0"+String.valueOf(month).toString()+"-"+String.valueOf(day).toString();
                            }
                            else
                            if(day<10)
                            {
                                Gettgllahir=String.valueOf(year).toString()+"-"+String.valueOf(month).toString()+"-0"+String.valueOf(day).toString();
                            }
                        }
                        else
                        {
                            Gettgllahir=String.valueOf(year).toString()+"-"+String.valueOf(month).toString()+"-"+String.valueOf(day).toString();
                        }
                        String GetAlamat= alamatprofil.getText().toString();
                        String GetPekerjaan=pekerjaanprofil.getText().toString();
                        //---
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(TAG_USERNAME, GetUsername);
                        editor.putString(TAG_NAMA,GetNamaLengkap);
                        editor.putString(TAG_EMAIL,GetEmail);
                        editor.putString(TAG_TGL_LAHIR,Gettgllahir);
                        editor.putString(TAG_ALAMAT,GetAlamat);
                        editor.putString(TAG_PEKERJAAN,GetPekerjaan);
                        editor.putString(TAG_FOTOPROFIL,encodeFotoProfiltoString());
                        editor.commit();
                        //----

                        // Memanggil main activity
                        Intent intent = new Intent(UpdateProfileActivity.this, ProfileActivity.class);

                        finish();
                        startActivity(intent);

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
                params.put("nama", nama2);
                params.put("username", username2);
                params.put("email", email2);
                params.put("tgl_lahir", tgl_lahir2);
                params.put("alamat", alamat2);
                params.put("pekerjaan", pekerjaan2);
                params.put("gambar", gambar2);
                params.put("id", id2);


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
