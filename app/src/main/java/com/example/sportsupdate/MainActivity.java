package com.example.sportsupdate;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    public static ListView newsListView;
    public static NewsAdapter newsListAdapter;
    public static final String queryUrl="https://content.guardianapis.com/search?section=sport&api-key=17bd95f2-6010-41d7-8c41-5d3582eb15d6";
    private TextView emptyState;
    public static ArrayList<NewsEntry> newsEntriesList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progressBar);
        emptyState = findViewById(R.id.empty_state);
        emptyState.setVisibility(View.GONE);
        newsListView = findViewById(R.id.news_listview);
        newsListView.setEmptyView(emptyState);
        newsListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsEntry clickedNewsEntry=newsEntriesList.get(position);
                Uri newsUri=Uri.parse(clickedNewsEntry.getItemUrl());
                Intent newsIntent=new Intent(Intent.ACTION_VIEW,newsUri);
                startActivity(newsIntent);
            }
        });
        ConnectivityManager cm= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=cm.getActiveNetworkInfo();
        if(activeNetwork!=null && activeNetwork.isConnectedOrConnecting()){
        new NewsTask().execute(queryUrl);
        }else {
            emptyState.setText("Poor network connection!!...pls check your connection and try again");
            progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class NewsTask extends AsyncTask<String, Void, String> {
        URL url=null;
        HttpURLConnection urlConnection = null;
        String jsonResponse="";
        //private StringBuilder builder;
        private StringBuffer buffer;
        private Reader reader;


        @Override
        protected String doInBackground(String... strings) {
            try {
                CreateUrlFromString(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                 getJSONResponse(url);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("connection error",e.toString());
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute (String s){
            emptyState.setText("Sorry no sport news presently");
            if(s==""){
                return;
            }
            try {
                extractFromJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            newsListAdapter = new NewsAdapter(MainActivity.this,0, newsEntriesList);
            newsListView.setAdapter(newsListAdapter);
            progressBar.setVisibility(View.GONE);
        }
        private void CreateUrlFromString(String string) throws MalformedURLException {
            url=new URL(string);
        }
        private void getJSONResponse(URL url) throws IOException {
            if(url==null){return ;}
            int bufferSize=1024;
            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setRequestProperty("connection", "close");
           // System.setProperty("http.keepAlive","false");
            urlConnection.connect();
            //builder = new StringBuilder();
           // int rescode=urlConnection.getResponseCode();
            if (urlConnection.getResponseCode() == 200) {
                InputStream inputStream = urlConnection.getInputStream();
                urlConnection.disconnect();
               // InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                /*BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    reader.readLine();
                }
                inputStreamReader.close();
                reader.close();
                Scanner s=new Scanner(inputStream).useDelimiter("\\A");
                jsonResponse=s.hasNext()?s.next():"";
                inputStream.close();*/

                reader = new InputStreamReader(inputStream, "UTF-8");
                char[] rawBuffer = new char[bufferSize];
                int charsRead;
                buffer = new StringBuffer();
                while ((charsRead = reader.read(rawBuffer,0,rawBuffer.length))>0){
                    buffer.append(rawBuffer, 0, charsRead);
                }
                jsonResponse=buffer.toString();
            }
        }

        private  void extractFromJson (String jsonResponse)  throws JSONException {

            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response=root.getJSONObject("response");
            JSONArray newsEntryArray = response.getJSONArray("results");
            //ArrayList<NewsEntry> newsList = new ArrayList<>();
            String newsEntryTitle;
            String newsEntrySection;
            String newsEntryPubdate;
            String newsEntryUrl;
            newsEntriesList=new ArrayList<>();
            for (int i = 0; i < newsEntryArray.length(); i++) {
                JSONObject newsEntry = newsEntryArray.getJSONObject(i);
                newsEntryTitle = newsEntry.getString("id");
                newsEntrySection = newsEntry.getString("sectionName");
                newsEntryPubdate = newsEntry.getString("webPublicationDate");
                newsEntryUrl = newsEntry.getString("webUrl");
                newsEntriesList.add(new NewsEntry(newsEntryTitle, newsEntrySection, newsEntryPubdate, newsEntryUrl));
            }
        }

    }

}
