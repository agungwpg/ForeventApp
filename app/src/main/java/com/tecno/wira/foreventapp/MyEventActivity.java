package com.tecno.wira.foreventapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tecno.wira.foreventapp.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyEventActivity extends AppCompatActivity {

    //Bottom Navigation Bar ===========================
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentHome = new Intent(MyEventActivity.this,MainActivity.class);
                    startActivity(intentHome);
                    finish();
                    return true;
                case R.id.navigation_myevents:

                    return true;
                case R.id.navigation_profile:
                    Intent intentProfile = new Intent(MyEventActivity.this,ProfileActivity.class);
                    startActivity(intentProfile);
                    finish();
                    return true;
            }
            return false;
        }
    };
    //End Bottom Navigation Bar =========================

    ProgressDialog pDialog;
    int success;

    ConnectivityManager conMgr;
    private static final String TAG = MyEventActivity.class.getSimpleName();
    private String url = Server.url+"showeventbyidpengguna.php";
    private String urldetilevent = Server.url+"showdetilevent.php";

    String tag_json_obj = "json_obj_req";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    List<Subject> CustomSubjectNamesList;
    ListView listViewmyevent;
    ProgressBar progressBar;

    //--- tag dari php digunkan pada load detil event
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




    ///=--------------------------------------------------
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    // share preference pengguna
    public final static String TAG_USERNAME = "username";
    public final static String TAG_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);

        //==============
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //===============

        listViewmyevent = (ListView) findViewById(R.id.listviewMyevent);
        progressBar = (ProgressBar)findViewById(R.id.ProgressBarMyevent);

        //---
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
        //--- menget id pengguna dari sharepreference
        sharedpreferences = getSharedPreferences(DetilEventActivity.my_shared_preferences, Context.MODE_PRIVATE);
        String idPenggunaSp = sharedpreferences.getString(TAG_ID,"");
        // dan kemudian meload data berdasarkan id pengguna ke dalam listview
        loadDataMyevent(idPenggunaSp);

        listViewmyevent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loadDetilevent(CustomSubjectNamesList.get(i).Subject_ID );

            }
        });

        Button btnAddEvent = (Button)findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),TambahEventActivity.class);
                startActivity(i);
            }
        });
    }



    public void loadDataMyevent(final String idpengguna2) {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Load data ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Load Response: " + response.toString());
                hideDialog();

                try {
                    String FinalJsonjObj = response;
                    //  success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (FinalJsonjObj!=null) {
                        //String id = jObj.getString(TAG_ID);
                        JSONArray jsonArray ;

                        jsonArray = new JSONArray(FinalJsonjObj); // hasil response ke dlam jeon array

                        JSONObject jsonObject;

                        // Creating Subject class object.
                        Subject subject;
                        CustomSubjectNamesList = new ArrayList<Subject>();

                        //---
                        for (int i = 0; i < jsonArray.length(); i++) {
                            subject = new Subject();
                            jsonObject = jsonArray.getJSONObject(i);
                            //Storing ID into subject list.
                            subject.Subject_ID = jsonObject.getString("id");
                            //Storing Subject name in subject list.
                            subject.Subject_Name = jsonObject.getString("nama");
                            // Adding subject list object into CustomSubjectNamesList.
                            subject.Subject_gambar = jsonObject.getString("gambar");
                            CustomSubjectNamesList.add(subject);
                            //     Toast.makeText(getApplicationContext(), subject.Subject_Name = jsonObject.getString("nama"),Toast.LENGTH_LONG).show();

                        }


                        Log.e("Successfully load myevent!", FinalJsonjObj.toString());
                        //   Toast.makeText(getApplicationContext(),CustomSubjectNamesList.get(1).Subject_Name ,Toast.LENGTH_LONG).show();
                        loadmyeventkeadapter();

                        //    Toast.makeText(getApplicationContext(), FinalJsonjObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();



                    } else {
                        //Toast.makeText(getApplicationContext(),
                        //      jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
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
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_pengguna", idpengguna2);


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

    //--- meload ke adapter listview
    private  void loadmyeventkeadapter()
    {
        // After all done loading set complete CustomSubjectNamesList with application context to ListView adapter.
        ListViewAdapter adapter = new ListViewAdapter(CustomSubjectNamesList, getApplicationContext());

        // Setting up all data into ListView.
        listViewmyevent.setAdapter(adapter);

        // Hiding progress bar after all JSON loading done.
        progressBar.setVisibility(View.GONE);
    }

    /// **************************** load detil event klick
    private void loadDetilevent(final String idEvent) {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("load data detil event ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST, urldetilevent, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "event detil Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        String ideventjs = jObj.getString("iddetilevent");
                        String namaeventjs = jObj.getString("namadetilevent");
                        String biayaeventjs=jObj.getString("biayadetilevent");
                        String tglmulaieventjs = jObj.getString("tgl_mulai_detilevent");
                        String tglakhireventjs = jObj.getString("tgl_akhir_detilevent");
                        String stokeventjs= jObj.getString("stok_detilevent");
                        String gambareventjs= jObj.getString("gambar_detilevent");
                        String deskripsieventjs= jObj.getString("deskripsi_detilevent");
                        String jeniseventjs=jObj.getString("jenis_detilevent");
                        String penyelengaraeventjs=jObj.getString("penyelengara_detilevent");


                        // data yang sudah di ambil dari TAG php json, dimasukan ke dalam sharepreference
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(TAG_ID_DETILEVENT, ideventjs);
                        editor.putString(TAG_NAMA_DETILEVENT, namaeventjs);
                        editor.putString(TAG_BIAYA_DETILEVENT, biayaeventjs);
                        editor.putString(TAG_TGLMULAI_DETILEVENT, tglmulaieventjs);
                        editor.putString(TAG_TGLAKHIR_DETILEVENT,tglakhireventjs);
                        editor.putString(TAG_STOK_DETILEVENT,stokeventjs);
                        editor.putString(TAG_GAMBAR_DETILEENT,gambareventjs);
                        editor.putString(TAG_DESKRIPSI_DETILEVENT,deskripsieventjs);
                        editor.putString(TAG_JENIS_DETILEVENT,jeniseventjs);
                        editor.putString(TAG_PENYELENGARAD_ETILEVENT,penyelengaraeventjs);
                        editor.commit();


                        Log.e("Successfully load detil event!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MyEventActivity.this, DetilMyEventActivity.class);

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
                Log.e(TAG, "load detil Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idEvent);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq,tag_json_obj);
        //  AppController.getInstance().addToRequestQueue(strReq,tag_json_obj);
    }

}
