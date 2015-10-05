package com.neelesh.cube26;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView src_spinner;
    private AutoCompleteTextView dest_spinner;
    private Button queryButton;
//    private static final int CODE = 0;
//    private static final int SRC = 1;
//    private static final int DEST = 2;
//    private String src;
//    private String dest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        src_spinner = (AutoCompleteTextView) findViewById(R.id.src_spinner);
        dest_spinner = (AutoCompleteTextView) findViewById(R.id.dest_spinner);

        String station_list[];

        RetrieveStations retrieveStations = new RetrieveStations();
        retrieveStations.execute();

        queryButton = (Button) findViewById(R.id.queryButton);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, TrainListActivity.class);
                Log.wtf("on click", src_spinner.getText().toString());
                Log.wtf("on click", dest_spinner.getText().toString());
//                intent.putExtra("src", "GWALIOR");
//                intent.putExtra("dest", "BHUBANESWAR");
                intent.putExtra("src", src_spinner.getText().toString());
                intent.putExtra("dest", dest_spinner.getText().toString());
                startActivity(intent);

                Toast toast = new Toast(getApplicationContext());
                toast.makeText(getApplicationContext(),"Invalid query",Toast.LENGTH_LONG);

            }
        });
    }

    public class RetrieveStations extends AsyncTask <String, Void, String>{
        private String response = null;
        public final static int GET = 1;
        // query code

        private URL url;

        public RetrieveStations() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.wtf("1","diInback");
            try {
                url = new URL("https://cube26-1337.0x10.info/stations");

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
            Log.wtf("2","after exec");
            if(response == null) {
                response = "THERE WAS AN ERROR";
            } else {
//                Log.wtf("response: ", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> list = new ArrayList<String>();
                    for(int i = 0; i < jsonArray.length(); i++){
                        list.add(jsonArray.getString(i));
                    }
//                    String[] arr = new String[3];
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            list);

                    src_spinner.setAdapter(adapter);
                    dest_spinner.setAdapter(adapter);
                } catch (JSONException e) {
                    // Appropriate error handling code
                }
            }
        }
    }
}
