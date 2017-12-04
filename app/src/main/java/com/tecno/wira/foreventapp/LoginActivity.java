package com.tecno.wira.foreventapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    Button  btn_login;
    TextView btn_register;
    EditText txt_username, txt_password;
    Intent intent;

    int success;

    ConnectivityManager conMgr;

    private String url = Server.url+"login.php";

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    // untuk share preference pengguna
    public final static  String TAG_USERNAME = "username";
    public final static  String TAG_ID = "id";
    public final static  String TAG_NAMA="nama";
    public final static  String TAG_EMAIL="email";
    public final static  String TAG_TGL_LAHIR="tgl_lahir";
    public final static  String TAG_ALAMAT="alamat";
    public final static  String TAG_PEKERJAAN="pekerjaan";
    public final static  String TAG_FOTOPROFIL="gambar";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, username;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        //injection View
        btn_login = (Button) findViewById(R.id.btnLogin);
        btn_register = (TextView) findViewById(R.id.btnRegister);
        txt_username = (EditText) findViewById(R.id.txtUsernameLogin);
        txt_password = (EditText) findViewById(R.id.txtPasswordLogin);

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);

        // Check session
        if (session) {
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_USERNAME, username);
            finish();
            startActivity(intent);
        }
        // End Check

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                String username = txt_username.getText().toString();
                String password = txt_password.getText().toString();

                // mengecek kolom yang kosong
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null&& conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected())
                    {
                        checkLogin(username, password);

                    } else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                finish();
                startActivity(intent);
            }
        });



    }

    private void checkLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        String id = jObj.getString(TAG_ID);
                        String nama = jObj.getString(TAG_NAMA);
                        String username = jObj.getString(TAG_USERNAME);
                        String email = jObj.getString(TAG_EMAIL);
                        String tgllahir = jObj.getString(TAG_TGL_LAHIR);
                        String alamat = jObj.getString(TAG_ALAMAT);
                        String pekerjaan = jObj.getString(TAG_PEKERJAAN);
                        String fotoprofil = jObj.getString(TAG_FOTOPROFIL);

                        Log.e("Succesfully Login!", jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(TAG_ID, id);
                        editor.putString(TAG_USERNAME, username);
                        editor.putString(TAG_NAMA, nama);
                        editor.putString(TAG_EMAIL, email);
                        editor.putString(TAG_TGL_LAHIR, tgllahir);
                        editor.putString(TAG_ALAMAT, alamat);
                        editor.putString(TAG_PEKERJAAN, pekerjaan);
                        editor.putString(TAG_FOTOPROFIL, fotoprofil);
                        editor.commit();

                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                        intent.putExtra(TAG_ID, id);
                        intent.putExtra(TAG_USERNAME, username);
                        intent.putExtra(TAG_NAMA, nama);
                        intent.putExtra(TAG_EMAIL, email);
                        intent.putExtra(TAG_TGL_LAHIR, tgllahir);
                        intent.putExtra(TAG_ALAMAT, alamat);
                        intent.putExtra(TAG_PEKERJAAN, pekerjaan);
                        intent.putExtra(TAG_FOTOPROFIL, fotoprofil);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext()
                                , jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
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
}
