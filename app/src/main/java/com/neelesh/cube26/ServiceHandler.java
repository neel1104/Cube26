package com.neelesh.cube26;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neelesh on 10/4/2015.
 */

    /*
    * 0 -- hits
    * 1 -- stations
    * 2 -- source only
    * 3 -- dest only
    * 4 -- source and dest
    */

public class ServiceHandler extends AsyncTask <String, Void, String>{
    static String response = null;
    public final static int GET = 1;
        // query code
    private static final int HITS = 0;
    private static final int STATIONS = 1;
    private static final int SRC = 2;
    private static final int DEST = 3;
    private static final int BOTH = 4;

    private URL url;
    private Activity activity;

    public ServiceHandler(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            String query = queryType(params);
//            Log.wtf("url: ",R.string.api_url + query);
//            url = new URL(R.string.api_url + query);
            url = new URL("https://cube26-1337.0x10.info/" + query);
//            url = new URL("https://cube26-1337.0x10.info/trains?source=BHUBANESWAR&destination=VISAKHAPATNAM");
//            url = new URL("https://cube26-1337.0x10.info/hits");
//            url = new URL("https://cube26-1337.0x10.info/stations");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
//                Log.wtf("what we got", stringBuilder.toString());
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        } else {
            Log.wtf("response: ", response);
            try {
                JSONArray jsonArray = new JSONArray(response);
                List<String> list = new ArrayList<String>();
                for(int i = 0; i < jsonArray.length(); i++){
                    list.add(jsonArray.getString(i));
                }
                Log.wtf("activity1 ", activity.toString());
                Log.wtf("activityTrain ", TrainListActivity.class.toString());
                // Log.wtf("list length: ", String.valueOf(list.size()));
                if(TrainListActivity.class.toString() == activity.toString()) {
                    Log.wtf("if statement", "success");
                }
            } catch (JSONException e) {
                // Appropriate error handling code
            }
        }
    }

    private String queryType(String[] params){
        // Log.wtf("params", params.toString());
        int code = Integer.parseInt(params[0].toString());
        String query = "";
        switch(code)
        {
            //
            case HITS:
                query += "hits";
                break;
            case STATIONS:
                query += "stations";
                break;
            case SRC:
                query += "trains?source=" + params[1];
                break;
            case DEST:
                query += "trains?destination=" + params[2];
                break;
            case BOTH:
                query += "trains?source=" + params[1] + "&destination=" + params[2];
                break;
            default:
                break;
        }
        Log.wtf("querytype: ", query);
        return  query;
    }
}
