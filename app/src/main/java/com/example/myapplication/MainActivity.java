package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText user_field;
    private Button button;
    private TextView result_inf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field = findViewById(R.id.user_field);
        button = findViewById(R.id.button);
        result_inf = findViewById(R.id.result_inf);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_field.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.no_user,Toast.LENGTH_LONG).show();
                else {
                    String city = user_field.getText().toString();
                    String key = "2f99842643510a0f0378cd800be6bde7";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid="+ key + "&units=metric&lang=ru";

                    new GetUrlData().execute(url);
                }
            }
        });

    }
    private class GetUrlData extends AsyncTask<String,String,String>
    {
        protected void onPreExecute(){
            super.onPreExecute();
            result_inf.setText("Ожидайте...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection con = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                con = (HttpURLConnection) url.openConnection();

                InputStream stream = con.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String Line = "";

                while ((Line = reader.readLine())!= null)
                    buffer.append(Line).append("\n");

                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if (con != null)
                    con.disconnect();
                try {
                    if (reader!=null)
                        reader.close();
            }   catch (IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject obj = new JSONObject(result);
                result_inf.setText("Температура "  + obj.getJSONObject("main").getDouble("temp"));
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

    }
}