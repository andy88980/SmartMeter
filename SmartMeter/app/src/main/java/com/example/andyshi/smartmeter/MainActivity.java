package com.example.andyshi.smartmeter;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    String[] meterDatas;
    ListView meterList;
    ListAdapter adapterMeter;
    private RequestQueue requestQueue;
    Timer timer;
    TimerTask timerTask;
    int i = 0;
    private JsonObjectRequest meterRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        meterList = (ListView)findViewById(R.id.lv_meter);
        requestQueue = Volley.newRequestQueue(this);
        //final GetData getData = new GetData();
        getMeters();
    }

    public void getMeters(){
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction("getMeters");
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        meterRequest = new JsonObjectRequest(Request.Method.POST, "http://163.18.57.43/irmsmeter/rmsmeter.php", json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    Log.d("meters:",response.toString());
                    meterDatas = new String[response.getJSONArray("machine").length()];
                    for(int i=0;i<response.getJSONArray("machine").length();i++){
                        Log.d("machine res:",response.getJSONArray("machine").get(i).toString());
                        Log.d("irms res:",response.getJSONArray("irms").get(i).toString());
                        meterDatas[i] = response.getJSONArray("machine").get(i) + ":" + response.getJSONArray("irms").get(i);
                    }
                    adapterMeter = new MeterAdapter(MainActivity.this,meterDatas);
                    meterList.setAdapter(adapterMeter);
                }catch (JSONException e){
                    Log.d("error:",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley err:",error.getMessage());
            }
        });
        requestQueue.add(meterRequest);
    }

}
