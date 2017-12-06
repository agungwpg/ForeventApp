package com.tecno.wira.foreventapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class PendaftarMyEventActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    int success;

    ConnectivityManager conMgr;
    private static final String TAG = MyEventActivity.class.getSimpleName();
    private String url = Server.url+"showeventbyideventdanstatusterima.php";
    private String urlkonfirmasi = Server.url+"konfirmasipendaftaranevent.php";

    String tag_json_obj = "json_obj_req";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    List<Subject> CustomSubjectNamesList;
    ListView listViewPendaftar;
    ProgressBar progressBar;


    /// sharepreference
    // share preference
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    public final static String TAG_ID_DETILEVENT = "id_detilevent";
    public final static  String TAG_STOK_DETILEVENT="stok_detilevent";

    public final String TAG_JENIS_DAFTAR="tag_jenis_daftar";

    String ideventSp;

    //// dialog confirmation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendaftar_my_event);

        sharedpreferences = getSharedPreferences(PendaftarMyEventActivity.my_shared_preferences, Context.MODE_PRIVATE);
        ideventSp =  sharedpreferences.getString(TAG_ID_DETILEVENT,"");
        // ---------------------- deklarasi variabel
        listViewPendaftar = (ListView) findViewById(R.id.listviewPemintaPendaftarMyevent);
        progressBar = (ProgressBar)findViewById(R.id.ProgressBarPendaftarMyevent);
        TextView titleDaftar = (TextView) findViewById(R.id.titlePendaftaranmyevent);
        //-----

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

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        String jenisdaftarSp = sharedpreferences.getString(TAG_JENIS_DAFTAR,"");

        loadDataPendaftarMyEvent(ideventSp,jenisdaftarSp);

        if(jenisdaftarSp.equalsIgnoreCase("sudah di terima"))
        {

            titleDaftar.setText("Daftar Anggota Yang Mengikuti  ");
            listViewPendaftar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int idpeserta, long l) {

                    //---------------------------------- alert dailog
                    AlertDialog.Builder x  = new AlertDialog.Builder(PendaftarMyEventActivity.this);

                    x.setTitle("Konfirmasi ");

                    x.setMessage("Menghapus Pendaftar Ini : "+ CustomSubjectNamesList.get(idpeserta).Subject_Name+" ?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    konfirmasiPendaftarMyEvent("hapus",ideventSp,CustomSubjectNamesList.get(idpeserta).Subject_ID);
                                    loadafterkonfirmasiPendaftaran(idpeserta);// menghapus peserta dari list temp
                                }
                            }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    }).show();


                    //-----------------------------------
                }
            });
        }

        //------------- mau konfirmasi untuk mengikuti
        if(jenisdaftarSp.equalsIgnoreCase("belum di terima"))
        {

            titleDaftar.setText("Daftar Permintaan Anggota  " );
            listViewPendaftar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int idpeserta, long l) {

                    //---------------------------------- alert dailog
                    AlertDialog.Builder x  = new AlertDialog.Builder(PendaftarMyEventActivity.this);

                    x.setTitle("Konfirmasi ");

                    x.setMessage("Menerima Pendaftar Ini : "+ CustomSubjectNamesList.get(idpeserta).Subject_Name+" ?").setPositiveButton("Terima", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            konfirmasiPendaftarMyEvent("terima",ideventSp,CustomSubjectNamesList.get(idpeserta).Subject_ID);
                            loadafterkonfirmasiPendaftaran(idpeserta);
                        }
                    }).setNegativeButton("Tolak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            konfirmasiPendaftarMyEvent("tolak",ideventSp,CustomSubjectNamesList.get(idpeserta).Subject_ID);

                        }
                    }).show();


                    //-----------------------------------
                }
            });
        }
    }

    //----- fungsi dibawah digunakan untuk meload data pendaftar ke dalam listview pendaftar
    private void loadDataPendaftarMyEvent(final String idEvent2, final String statusTerima2) {

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
                        loadPendaftarkeadapter();

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
                params.put("id_event", idEvent2);
                params.put("status_terima", statusTerima2);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq,tag_json_obj);

    }
    //************************************************************************************************************************
    //-------------------------------------- konfirmasi penerimaan/penolakan/penghapusan
    //******************************************************************************************************************

    private void konfirmasiPendaftarMyEvent(final String status3,final String idEvent3, final String idpeserta3) {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Load data ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST, urlkonfirmasi, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Load Response: " + response.toString());
                hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if(success==5) // 5 kode sukses mengjapus dan mengupdate + 1 stok kembali
                    {
                        String stokeventjs= jObj.getString("stok");

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(TAG_STOK_DETILEVENT,stokeventjs);
                        editor.commit();

                    }

                    Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "konfirmasi  Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("status", status3);
                params.put("id_event", idEvent3);
                params.put("id_peserta", idpeserta3);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq,tag_json_obj);
        //  AppController.getInstance().addToRequestQueue(strReq,tag_json_obj);
    }

    //------------------------------------------------------------------------

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    //--- meload ke adapter listview
    private  void loadPendaftarkeadapter()
    {
        // After all done loading set complete CustomSubjectNamesList with application context to ListView adapter.
        ListViewAdapter adapter = new ListViewAdapter(CustomSubjectNamesList, getApplicationContext());

        // Setting up all data into ListView.
        listViewPendaftar.setAdapter(adapter);

        // Hiding progress bar after all JSON loading done.
        progressBar.setVisibility(View.GONE);
    }

    private  void loadafterkonfirmasiPendaftaran(int xx) // digunakan untuk menghapus pengguna dari listview
    {
        // After all done loading set complete CustomSubjectNamesList with application context to ListView adapter.
        ListViewAdapter adapter = new ListViewAdapter(CustomSubjectNamesList, getApplicationContext());
        adapter.TempSubjectList.remove(xx);
        // Setting up all data into ListView.
        listViewPendaftar.setAdapter(adapter);

        // Hiding progress bar after all JSON loading done.
        progressBar.setVisibility(View.GONE);
    }
}
