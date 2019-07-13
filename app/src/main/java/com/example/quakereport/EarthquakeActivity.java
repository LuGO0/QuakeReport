package com.example.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>> {

    public static final String url="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        TextView emptyQueryIndicator = findViewById(android.R.id.empty);
        emptyQueryIndicator.setVisibility(View.GONE);

        ConnectivityManager cm=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=cm.getActiveNetworkInfo();

        boolean isConnected=(activeNetwork!=null);
        if(!isConnected) {

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            emptyQueryIndicator.setText("NO CONNECTION");
            emptyQueryIndicator.setVisibility(View.VISIBLE);
        }
//        ListView earthquakeListView = (ListView) findViewById(R.id.list);
//
//        TextView noData = (TextView) findViewById(android.R.id.empty);
//        earthquakeListView.setEmptyView(noData);
//        noData.setVisibility(View.GONE);

        //View loadingIndicator = findViewById(R.id.loading_indicator);
        //loadingIndicator.setVisibility(View.VISIBLE);

        // View loadingIndicator = findViewById(R.id.loading_indicator);
        //loadingIndicator.setVisibility(View.VISIBLE);

        getSupportLoaderManager().initLoader(0,null,this).forceLoad();
    }

    //implementing interface loader manager


    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int i,Bundle bundle) {
        return new EarthQuakeLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> earthquakes) {


        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);


        if(earthquakes==null) {


            TextView emptyQueryIndicator = findViewById(android.R.id.empty);
            emptyQueryIndicator.setVisibility(View.VISIBLE);
            return;
        }

        ListView earthquakeListView = (ListView) findViewById(R.id.list);


        // Create a new adapter that takes the list of earthquakes as input
        final EarthquakeAdapter adapter = new EarthquakeAdapter(EarthquakeActivity.this, earthquakes);

        // Set the adapter 
        earthquakeListView.setAdapter(adapter);

        // Set an item click listener on the ListView
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                
                Earthquake currentEarthquake = adapter.getItem(position);

                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                startActivity(websiteIntent);
            }
        });
    }


    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Earthquake>> loader) {
        //do nothing i guess//
    }

    private class EarthQuakeAsyncTask extends AsyncTask<String,Void,ArrayList<Earthquake>>{
        @Override
        protected ArrayList<Earthquake> doInBackground(String... strings) {
            //creating a url object
            //cereating a netwotk requet
            //convert the input atream into string
            //parse the json
            //modify the json parsing function to obtain a list of earthquakes
            ArrayList<Earthquake> result=QueryUtils.fetchEarthquakeData(strings[0]);
            //add back ground thread
            //execute on background therad
            //add permission
            return  QueryUtils.fetchEarthquakeData(strings[0]);

        }


        @Override
        protected void onPostExecute(ArrayList<Earthquake> earthquakes) {
            //update the arrayList
            // alredy updated i guess
            // Get the list of earthquakes from {@link QueryUtils;

            ListView earthquakeListView = (ListView) findViewById(R.id.list);

            // Create a new adapter that takes the list of earthquakes as input
            final EarthquakeAdapter adapter = new EarthquakeAdapter(EarthquakeActivity.this, earthquakes);

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            earthquakeListView.setAdapter(adapter);

            // Set an item click listener on the ListView, which sends an intent to a web browser
            // to open a website with more information about the selected earthquake.
            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // Find the current earthquake that was clicked on
                    Earthquake currentEarthquake = adapter.getItem(position);

                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                    // Create a new intent to view the earthquake URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }
            });

        }
    }
}

