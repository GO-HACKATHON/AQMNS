package com.example.icol.navast;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by icol on 25/03/2017.
 */
public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    JSONObject jsonobject;
    JSONArray jsonarray;
    ArrayList<Double> nodelatitude;
    ArrayList<Double> nodelongitude;
    ArrayList<Integer> nodeid;
    ArrayList<String> nodename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new DownloadJSON().execute();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MapsActivity.class);
                i.putExtra("nolat", nodelatitude);
                i.putExtra("nolong", nodelongitude);
                i.putExtra("noid", nodeid);
                i.putExtra("noname", nodename);
                if(nodelatitude!=null){
                    startActivity(i); // menghubungkan activity splashscren ke main activity dengan intent
                }
                else {

                }

                //jeda selesai Splashscreen
                this.finish();
            }

            private void finish() {
                // TODO Auto-generated method stub

            }


        }, SPLASH_TIME_OUT);
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            nodelatitude = new ArrayList<Double>();
            nodelongitude = new ArrayList<Double>();
            nodeid = new ArrayList<Integer>();
            nodename = new ArrayList<String>();
            jsonobject = JSONParser.getJSONfromURL("http://10.17.10.171/hackathon/parsing_db.php");

            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("node");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    nodelatitude.add(jsonobject.getDouble("latitude"));
                    nodelongitude.add(jsonobject.getDouble("longitude"));
                    nodeid.add(jsonobject.getInt("node_id"));
                    nodename.add(jsonobject.getString("node_name"));

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

        }
    }
}