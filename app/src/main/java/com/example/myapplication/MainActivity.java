package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    // Will contain the raw JSON response as a string.
    String data = null;

    ProgressBar progressbar;
    ImageView iv1, iv2, iv3;
    TextView name1, id1, name2, id2, name3, id3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        new JasonRequest().execute();

    }
    private void findViews(){
        progressbar=findViewById(R.id.progressbar);
        iv1 = findViewById(R.id.iv1);
        name1=findViewById(R.id.name1);
        id1=findViewById(R.id.id1);
        iv2=findViewById(R.id.iv2);
        name2=findViewById(R.id.name2);
        id2=findViewById(R.id.id2);
        iv3=findViewById(R.id.iv3);
        name3=findViewById(R.id.name3);
        id3=findViewById(R.id.id3);

    }

    private class JasonRequest extends AsyncTask<Void, Void ,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.myjson.com/bins/zjv68");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    data = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    data = null;
                }
                data = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                data = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressbar.setVisibility(View.GONE);
            //Read JSON
            try {
                JSONObject returnData = new JSONObject(data);
                JSONArray studentArray = returnData.getJSONArray("students");

                JSONObject student1=studentArray.getJSONObject(0);
                String Student1Name=student1.getString("studentName");
                String Student1Id=student1.getString("studentID");
                String Student1Photo=student1.getString("studentPhoto");
                name1.setText(Student1Name);
                id1.setText(Student1Id);
                Picasso.get().load(Student1Photo).into(iv1);

                JSONObject student2=studentArray.getJSONObject(1);
                String Student2Name=student2.getString("studentName");
                String Student2Id=student2.getString("studentID");
                String Student2Photo=student2.getString("studentPhoto");
                name2.setText(Student2Name);
                id2.setText(Student2Id);
                Picasso.get().load(Student2Photo).into(iv2);

                JSONObject student3=studentArray.getJSONObject(2);
                String Student3Name=student3.getString("studentName");
                String Student3Id=student3.getString("studentID");
                String Student3Photo=student3.getString("studentPhoto");
                name3.setText(Student3Name);
                id3.setText(Student3Id);
                Picasso.get().load(Student3Photo).into(iv3);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



    }





}
