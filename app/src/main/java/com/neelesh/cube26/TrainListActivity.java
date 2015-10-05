package com.neelesh.cube26;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TrainListActivity extends AppCompatActivity {

    ListView listView;
    private String src, dest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);

        listView = (ListView) findViewById(R.id.listView);
        Intent intent = getIntent();

        src = intent.getStringExtra("src");
        dest=intent.getStringExtra("dest");
        src.replace(" ", "%20");
        dest.replace(" ", "%20");

        RetrieveTrains retrieveTrains = new RetrieveTrains();
        retrieveTrains.execute();
    }

    public class RetrieveTrains extends AsyncTask<String, Void, String> {
        private String response = null;
        public final static int GET = 1;
        // query code

        private URL url;

        public RetrieveTrains() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.wtf("2","DO IN BACKGGPEOGRJ");
            try {
                url = new URL("https://cube26-1337.0x10.info/trains?source="+src+"&destination"+dest);

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
            Log.wtf("2","after exectutoapei");
            if(response == null) {
                response = "THERE WAS AN ERROR";
            } else {
                Log.wtf("response: ", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<String> list = new ArrayList<String>();
                    for(int i = 0; i < jsonArray.length(); i++){
                        list.add(jsonArray.getString(i));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1,list);
                    listView.setAdapter(adapter);
                    //TrainListAdapter adapter = new TrainListAdapter(jsonArray, getApplicationContext());
                    //listView.setAdapter(adapter);

//                    CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),list);
//                    listView.setAdapter(customAdapter);

                } catch (JSONException e) {
                    // Appropriate error handling code
                }
            }
        }
    }

    private class CustomAdapter extends ArrayAdapter<String> {

        protected Context mContext;
        protected ArrayList<String> mItems;

        public CustomAdapter(Context context, ArrayList<String> items) {
            super(context, R.layout.list_item);
            mContext = context;
            mItems = items;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            System.out.println("enters");
            if(convertView == null){
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_item,parent,false);
            }
            // You'll need to use the mItems array to populate these...
            ((TextView) convertView.findViewById(R.id.train_no)).setText(mItems.get(position));

            return convertView;
        }    }
}
