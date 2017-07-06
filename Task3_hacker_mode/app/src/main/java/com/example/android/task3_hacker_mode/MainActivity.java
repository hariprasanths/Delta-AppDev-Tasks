package com.example.android.task3_hacker_mode;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.task3_hacker_mode.Data.PokeContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


public class MainActivity extends AppCompatActivity  {

    TextView resultTextView;
    EditText searchInputBox;
    Button searchButton;
    String pokemonName;
    ProgressBar progressBar;
    ImageView imageView;
    String typesName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        resultTextView = (TextView) findViewById(R.id.result_textview);
        searchButton = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.image_view);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInputBox = (EditText) findViewById(R.id.search_inputbox);
                pokemonName = searchInputBox.getText().toString();
                if (pokemonName.equals(""))
                    Toast.makeText(getApplicationContext(), "Enter a valid name!", Toast.LENGTH_SHORT).show();
                else {
                    PokemonAsyncTask task = new PokemonAsyncTask();
                    task.execute();
                    searchInputBox.getText().clear();
                }
            }
        });

    }

    void displayResults(String name, int height, int weight, String sprite, String type) {
        String result = "Name: " + name + "\nHeight: " + height + " feet"+ "\nWeight: " + weight + " lbs"+ "\nTypes: " + type;
        resultTextView.setText(result);
        Glide.with(getApplicationContext()).load(sprite).override(500, 500).into(imageView);
        imageView.setVisibility(View.VISIBLE);
    }

    void displayResults(String msg) {
        resultTextView.setText(msg);
    }

    void insertPokeIntoDb(String name,String imageurl)
    {
        ContentValues values = new ContentValues();
        values.put(PokeContract.PokeEntry.COLUMN_POKE_NAME,name);
        values.put(PokeContract.PokeEntry.COLUMN_POKE_IMAGE,imageurl);
        getContentResolver().insert(PokeContract.CONTENT_URI,values);

    }



    private class PokemonAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            resultTextView.setText("");
            imageView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder output = new StringBuilder();
            String jsonResponse = "";
            try {
                url = new URL("http://pokeapi.co/api/v2/pokemon/" + pokemonName);
            } catch (MalformedURLException e) {
                return null;
            }

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream(), Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                if (line != null)
                    output.append(line);
                reader.close();
                jsonResponse = output.toString();

            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            String name = "";
            int height = 0;
            int weight = 0;
            String sprite = "";
            typesName = "";
            progressBar.setVisibility(View.GONE);
            if (response == null) {
                response = "Error loading Info!!";
                displayResults(response);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    name = jsonObject.optString("name");
                    height = jsonObject.optInt("height");
                    weight = jsonObject.optInt("weight");
                    JSONObject sprites = jsonObject.getJSONObject("sprites");
                    sprite = sprites.getString("front_default");
                    JSONArray typeArray = jsonObject.getJSONArray("types");
                    for (int i = 0; i < typeArray.length(); i++) {
                        JSONObject typeArrayJSONObject = typeArray.getJSONObject(i);
                        JSONObject typeJSONObject = typeArrayJSONObject.getJSONObject("type");
                        if (i == typeArray.length() - 1)
                            typesName += typeJSONObject.optString("name") + "\n             ";
                        else typesName += typeJSONObject.optString("name") + ",\n             ";
                    }

                } catch (JSONException e) {

                }
                displayResults(name, height, weight, sprite, typesName);
                insertPokeIntoDb(name,sprite);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.history_button:
                Intent intent = new Intent(MainActivity.this,History.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
