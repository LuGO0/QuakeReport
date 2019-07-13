package com.example.quakereport;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

public class EarthQuakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>> {

    public EarthQuakeLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {
        ArrayList<Earthquake> result=QueryUtils.fetchEarthquakeData(EarthquakeActivity.url);
        //add back ground thread
        //execute on background therad
        //add permission
        return  QueryUtils.fetchEarthquakeData(EarthquakeActivity.url);
    }


}
