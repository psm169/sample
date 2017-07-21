package com.bignerdranch.android.jsontest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class Jsontest extends AppCompatActivity {


    private Button button;
    private TextView textView;
    private String url = "http://realbyte.co.kr/seockmin/memopadtext.json";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsontest);


        button = (Button)findViewById(R.id.json_view_button);
        textView = (TextView)findViewById(R.id.json_view_test);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try{
                    JSONObject jsonObject = getJSONObjectFromURL(url);
                    //
                    // 파싱 할 내용
                    //
                   // textView.setText(jsonObject.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        button.setEnabled(true);




    }


    public String loadJsonFile() {
        String json = null;
        try {
            InputStream is = getAssets().open(url);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }


    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url2 = new URL(urlString);
        try{

            urlConnection = (HttpURLConnection) url2.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */ );
            urlConnection.setConnectTimeout(15000 /* milliseconds */ );
            urlConnection.setDoOutput(true);
            urlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }



        BufferedReader br = new BufferedReader(new InputStreamReader(url2.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }


}
