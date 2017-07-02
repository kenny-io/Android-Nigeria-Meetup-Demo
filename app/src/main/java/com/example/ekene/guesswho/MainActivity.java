package com.example.ekene.guesswho;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            URL url;
            String result = "";
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]); //converts the String api to data type URL

                urlConnection = (HttpURLConnection) url.openConnection(); // opens up the api in the urlConnection

                InputStream in = urlConnection.getInputStream(); // Creates a stream to store the contents of the urlConnection

                InputStreamReader reader = new InputStreamReader(in); // reads through the contents of the stream

                int data = reader.read(); // data variable stores what the reader is reading

                // Use a while loop to equate each current character being read to data, append current to result and continue reading data.

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }

                return result;

            } catch (Exception e) {

                e.printStackTrace();

            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            /* processing the Json data returned to the console (result)
                first convert the result to a JSONObject and store it in a String variable
                (contentInfo)
             */
            try {
                JSONObject jsonObject = new JSONObject(result);
                String contentInfo = jsonObject.getString("items"); // here we open up the "items" array in our console to access it's parts and store them to contentInfo
                Log.i("Content Info", contentInfo);

                /* convert the contentInfo to a JSONArray so we can extract the desired individual bits*/

                JSONArray jsonArray = new JSONArray(contentInfo);

                for (int i = 0; i <jsonArray.length(); i++){

                    JSONObject jsonPart = jsonArray.getJSONObject(i);

                    //Log the parts of the contentInfo you're interested in to the console

                    Log.i("login", jsonPart.getString("login"));
                    Log.i("avatar_url", jsonPart.getString("avatar_url"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Call the DownloadTask and execute the task

        DownloadTask task = new DownloadTask();
        task.execute("https://api.github.com/search/users?q=location:lagos+language:java");

    }
}
