package com.tecno.wira.foreventapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tecno.wira.foreventapp.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.icu.lang.UCharacter.toUpperCase;

public class EventsActivity extends AppCompatActivity {

    // con server
    ProgressDialog pDialog;
    private String urldetilevent = Server.url+"showdetilevent.php";
    int success;
    String tag_json_obj = "json_obj_req";
    private static final String TAG = DetilEventActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

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

    ///
    SharedPreferences sharedpreferences;
    String TAG_JENIS_EVENT = "tag_jenis_event";
    //--
    ListView listView;



    // Server Http URL
    String HTTP_URL = "";

    // String to hold complete JSON response object.
    String FinalJSonObject ;
    List<Subject> CustomSubjectNamesList;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        // Assign ID's to ListView.
        listView = (ListView) findViewById(R.id.listView1);
        progressBar = (ProgressBar)findViewById(R.id.ProgressBar1);
        // read sp dari home event
        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        String jeniseventsp = sharedpreferences.getString(TAG_JENIS_EVENT,"");
        // set title jenis event
        TextView txttitleJenisevent = (TextView) findViewById(R.id.txttitleJenisevent);
        txttitleJenisevent.setText(toUpperCase(jeniseventsp));

        // load data event sesuai dengan jenis yang di klick
        loaddata(jeniseventsp);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                loadDetilevent(CustomSubjectNamesList.get(i).Subject_ID);

            }
        });
    }

    private void loaddata(String jenis)
    {
        //Showing progress bar just after button click.
        progressBar.setVisibility(View.VISIBLE);

        // Creating StringRequest and set the JSON server URL in here.
        if(jenis.equalsIgnoreCase("sport"))
        {
            HTTP_URL = Server.url+"showeventsport.php";
        }
        else
        if(jenis.equalsIgnoreCase("lingkungan"))
        {
            HTTP_URL = Server.url+"showeventlingkungan.php";
        }
        else
        if(jenis.equalsIgnoreCase("sosial"))
        {
            HTTP_URL = Server.url+"showeventsosial.php";
        }
        else
        if(jenis.equalsIgnoreCase("seminar"))
        {
            HTTP_URL = Server.url+"showeventseminar.php";
        }

        //--
        StringRequest stringRequest = new StringRequest(HTTP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // After done Loading store JSON response in FinalJSonObject string variable.
                        FinalJSonObject = response ;

                        // Calling method to parse JSON object.
                        new ParseJSonDataClass(EventsActivity.this).execute();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Showing error message if something goes wrong.
                        Toast.makeText(EventsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

        // Creating String Request Object.
        RequestQueue requestQueue = Volley.newRequestQueue(EventsActivity.this);

        // Passing String request into RequestQueue.
        requestQueue.add(stringRequest);
    }



    //------------------------------------------------------------------------------------------------
    // Creating method to parse JSON object.
    private class ParseJSonDataClass extends AsyncTask<Void, Void, Void> {

        public Context context;

        // Creating List of Subject class.
        // List<Subject> CustomSubjectNamesList;

        public ParseJSonDataClass(Context context) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                // Checking whether FinalJSonObject is not equals to null.
                if (FinalJSonObject != null) {

                    // Creating and setting up JSON array as null.
                    JSONArray jsonArray = null;
                    try {

                        // Adding JSON response object into JSON array.
                        jsonArray = new JSONArray(FinalJSonObject);

                        // Creating JSON Object.
                        JSONObject jsonObject;

                        // Creating Subject class object.
                        Subject subject;

                        // Defining CustomSubjectNamesList AS Array List.
                        CustomSubjectNamesList = new ArrayList<Subject>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            subject = new Subject();

                            jsonObject = jsonArray.getJSONObject(i);
                            //Storing ID into subject list.
                            subject.Subject_ID = jsonObject.getString("id");
                            //Storing Subject name in subject list.
                            subject.Subject_Name = jsonObject.getString("nama");
                            subject.Subject_gambar=jsonObject.getString("gambar");
                            // Adding subject list object into CustomSubjectNamesList.
                            CustomSubjectNamesList.add(subject);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            // After all done loading set complete CustomSubjectNamesList with application context to ListView adapter.
            ListViewAdapter adapter = new ListViewAdapter(CustomSubjectNamesList, context);

            // Setting up all data into ListView.
            listView.setAdapter(adapter);

            // Hiding progress bar after all JSON loading done.
            progressBar.setVisibility(View.GONE);

        }
    }

    ///------------------------------------------------------------------------------------
    //------ get data dari server detil event

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

                        Intent intent = new Intent(EventsActivity.this, DetilEventActivity.class);
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
                params.put("id", idEvent);
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
