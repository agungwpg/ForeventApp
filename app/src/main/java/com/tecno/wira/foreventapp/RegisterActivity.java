package com.tecno.wira.foreventapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
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

public class RegisterActivity extends AppCompatActivity {

    //--- koneksi
    String tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;
    int success;
    ConnectivityManager conMgr;
    private String url = Server.url+"register.php";

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    //-----

    String hasilEncodeGambar;


    private static int RESULT_LOAD_IMAGE = 1;
    // deklarasi variabel untuk di get dari form
    Button btnGallery,btnRegister;
    EditText namalengkap,username,password,email,tgllahir,alamat,pekerjaan;
    ImageView fotoprofil;
    DatePicker datePicker;
    TextView loginLink;
    // deklarasi variabel untuk menampung hasil dari form
    String GetNamaLengkap, GetUsername, GetPassword, GetEmail,Gettgllahir,GetAlamat,GetPekerjaan;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //---- pengecekan koneksi
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

        // ================= deklarasi variable
//        btnGallery = (Button)findViewById(R.id.btnPilihFoto);
        loginLink = (TextView)findViewById(R.id.link_login);
        btnRegister =(Button) findViewById(R.id.btnRegister);
        fotoprofil =(ImageView)findViewById(R.id.btnPilihFoto);
        namalengkap =(EditText)findViewById(R.id.txtnamaregister);
        username = (EditText) findViewById(R.id.txtusernameregister);
        password=(EditText) findViewById(R.id.txtpasswordregister);
        email =(EditText) findViewById(R.id.txtemailregister);
        datePicker= (DatePicker) findViewById(R.id.tgllhirregister);
        alamat=(EditText) findViewById(R.id.txtalamatregister);
        pekerjaan=(EditText) findViewById(R.id.txtpekerjaanregister);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        fotoprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //==================== meng Get Data
                // GetFotoProfil = encodeFotoProfiltoString().toString(); // sudah di encode
                GetNamaLengkap = namalengkap.getText().toString();
                GetUsername= username.getText().toString();
                GetPassword= password.getText().toString();
                GetEmail = email.getText().toString();
                int   day  = datePicker.getDayOfMonth();
                int   month= datePicker.getMonth() + 1;
                int   year = datePicker.getYear();
                Gettgllahir=String.valueOf(year).toString()+"-"+String.valueOf(month).toString()+"-"+String.valueOf(day).toString();
                GetAlamat= alamat.getText().toString();
                GetPekerjaan=pekerjaan.getText().toString();
                //nama,username,password,email,tgl_lahir,alamat,pekerjaan,role,gambar
                if(cekInputKosong()==1)// return 1 berarti tidak ada yang kosong
                {

                    if (conMgr.getActiveNetworkInfo() != null&& conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected())
                    {
                        checkRegister(GetNamaLengkap,GetUsername,GetPassword,GetEmail,Gettgllahir,GetAlamat,GetPekerjaan,encodeFotoProfiltoString());

                    } else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }



                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Silahkan Isi Data yang masih kosong",Toast.LENGTH_SHORT).show();
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

            ImageView imageView = (ImageView) findViewById(R.id.btnPilihFoto);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            fotoprofil.setImageBitmap(bmp);
            // set ukuran dan
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 300);
            layoutParams.setMargins(220, 60, 0,0);
            fotoprofil.setLayoutParams(layoutParams);

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

    // encode fungsi
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
    // decode fungsi
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private String encodeFotoProfiltoString()
    {
        fotoprofil.buildDrawingCache();
        Bitmap bitmap = fotoprofil.getDrawingCache();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        String img_str = Base64.encodeToString(image, 0);
        hasilEncodeGambar=img_str;

        fotoprofil.setImageBitmap(null);
        return hasilEncodeGambar;
    }
    private int cekInputKosong()
    {
        GetNamaLengkap = namalengkap.getText().toString();
        GetUsername= username.getText().toString();
        GetPassword= password.getText().toString();
        GetEmail = email.getText().toString();
        int   day  = datePicker.getDayOfMonth();
        int   month= datePicker.getMonth() + 1;
        int   year = datePicker.getYear();
        Gettgllahir=String.valueOf(year).toString()+"-"+String.valueOf(month).toString()+"-"+String.valueOf(day).toString();
        GetAlamat= alamat.getText().toString();
        GetPekerjaan=pekerjaan.getText().toString();

        int status = 1;

        if(GetNamaLengkap.toString().equalsIgnoreCase(""))
        {
            status=0;
        }
        if(GetUsername.toString().equalsIgnoreCase(""))
        {
            status=0;
        }
        if(GetPassword.toString().equalsIgnoreCase(""))
        {
            status=0;
        }
        if(GetEmail.toString().equalsIgnoreCase(""))
        {
            status=0;
        }
        if(Gettgllahir.toString().equalsIgnoreCase(""));
        {
//            status=0;
        }
        if(GetAlamat.equalsIgnoreCase(""))
        {
            status=0;
        }
        if(GetPekerjaan.equalsIgnoreCase(""))
        {
            status=0;
        }
        return status;
    }
    //----------------------------------------------------------------------------


    private void checkRegister(final String nama2, final String username2,final  String  password2,final String email2,final String tgl_lahir2,final String alamat2,final String pekerjaan2,final String gambar2) {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Register ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {


                        Log.e("Successfully register!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();


                        // Memanggil main activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

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
                Log.e(TAG, "Register Error: " + error.getMessage());
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
                params.put("password", password2);
                params.put("email", email2);
                params.put("tgl_lahir", tgl_lahir2);
                params.put("alamat", alamat2);
                params.put("pekerjaan", pekerjaan2);
                params.put("gambar", gambar2);


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




    //----------------------------------------------------------------------------

}
