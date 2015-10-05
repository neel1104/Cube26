package com.neelesh.cube26;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.zip.Inflater;

/**
 * Created by neelesh on 10/4/2015.
 */
public class TrainListAdapter extends BaseAdapter {

    //private JSONArray jsonArray;
    private ArrayList<String> trainNo, trainName, stationName, sourceStationName, destinationStationName, arrivalTime, departureTime;
    private int size;
    private Context context;

    public TrainListAdapter(JSONArray jsonArray, Context context) throws JSONException {
        Log.wtf("enter", "adapter constructor");
        int i=0;
        size=0;
        this.context = context;
        //this.jsonArray = jsonArr;
        JSONObject trainDetails = (JSONObject) jsonArray.get(0);
        String prev="";
        String via = "";
        // intitalize
        trainNo = new ArrayList<>();
        trainName = new ArrayList<>();
        sourceStationName = new ArrayList<>();
        destinationStationName = new ArrayList<>();
        arrivalTime  = new ArrayList<>();
        departureTime = new ArrayList<>();
        stationName =  new ArrayList<>();

        Log.wtf("done", "intialising");
        while(trainDetails != null){
            trainDetails = jsonArray.getJSONObject(i);
            String cur = trainDetails.getString("trainNo");
            if(!prev.equals(cur)){
//                Log.wtf("enter", "if prev not equals");
                trainNo.add(size, trainDetails.getString("trainName"));
//                Log.wtf("added", "train no");
                sourceStationName.add(size, trainDetails.getString("sourceStationName"));
                destinationStationName.add(size, trainDetails.getString("destinationStationName"));
                arrivalTime.add(size, trainDetails.getString("arrivalTime"));
                departureTime.add(size, trainDetails.getString("departureTime"));
                // via !!
                prev = cur;
                via = "";
                size++;
            } else {
//                Log.wtf("enter", "if prev quals");
                via += " " + trainDetails.getString("stationName");
            }
            ++i;
            Log.wtf("size", size+"");
        }

        Log.wtf("trainnumbers", trainNo.toString());
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{

        public TextView train_no_tv;
        public TextView train_name_tv;
        public TextView journey_time_tv;
        public TextView srcStation_tv;
        public TextView destStation_tv;
        public TextView arrivalTime_tv;
        public TextView departureTime_tv;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Log.wtf(""+ position, ""+trainNo);
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent);
            holder.train_name_tv = (TextView) convertView.findViewById(R.id.train_name);
            holder.train_no_tv = (TextView) convertView.findViewById(R.id.train_no);
            holder.srcStation_tv = (TextView) convertView.findViewById(R.id.src_station);
            holder.destStation_tv = (TextView) convertView.findViewById(R.id.dest_station);
            holder.arrivalTime_tv = (TextView) convertView.findViewById(R.id.arrival_time);
            holder.departureTime_tv = (TextView) convertView.findViewById(R.id.departure_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.train_name_tv.setText(trainName.get(position));
        holder.train_no_tv.setText(trainNo.get(position));
        holder.srcStation_tv.setText(sourceStationName.get(position));
        holder.destStation_tv.setText(destinationStationName.get(position));
        holder.arrivalTime_tv.setText(arrivalTime.get(position));
        holder.departureTime_tv.setText(departureTime.get(position));
        return convertView;
    }
}
