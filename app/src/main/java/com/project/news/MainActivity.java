package com.project.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    ArrayList<RVModel> rvlist = new ArrayList<>();
    RVAdapter adapter;
    RVModel rvModel;
    RelativeLayout pbparent;
    //String value="No";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvnews);
        pbparent = findViewById(R.id.pbp);

        OkHttpClient client = new OkHttpClient();
       /* Map<String, String> params = new HashMap<String, String>();
        params.put("apiKey", "d29d58aab88d4ea0b04ddb245a230068");*/

        Request request = new Request.Builder()
                .url("https://newsapi.org/v2/sources?apiKey=d29d58aab88d4ea0b04ddb245a230068")
                .get()
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Log.e("result", "in failure" + e.getMessage().toString());

            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                String respo = response.body().string();
                Log.e("result", "res" + respo);
                try {
                    JSONObject jsonObject = new JSONObject(respo);
                    String status = jsonObject.getString("status");
                    if (!status.equalsIgnoreCase("OK")) {
                        String message = jsonObject.getString("message");
                        Toast.makeText(MainActivity.this, "Something going wrong" + message, Toast.LENGTH_LONG).show();
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("sources");
                        Log.e("length", "" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            String name = jsonObject1.getString("name");
                            String description = jsonObject1.getString("description");
                            String url = jsonObject1.getString("url");
                            String category = jsonObject1.getString("category");
                            String language = jsonObject1.getString("language");
                            String country = jsonObject1.getString("country");
                            rvModel = new RVModel(id, name, description, url, category, language, country);
                            rvlist.add(rvModel);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error", e.getMessage());
                }
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (rvlist.size() == 0) {
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show());
                            runOnUiThread(() -> pbparent.setVisibility(View.GONE));
                        } else {
                            runOnUiThread(() -> setuprv());
                            runOnUiThread(() -> pbparent.setVisibility(View.GONE));
                        }
                    }
                });
            }
        }, 3000);


    }
     public boolean checkconnectivity() {
         boolean connected = false;
         ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
         if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).

                 getState() == NetworkInfo.State.CONNECTED ||
                 connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).

                         getState() == NetworkInfo.State.CONNECTED) {
             //we are connected to a network
             connected = true;
             return connected;
         } else {
             connected = false;
             return connected;
         }
         //return connected;
     }
    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }


    private void setuprv() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new RVAdapter(this,rvlist);
        recyclerView.setAdapter(adapter);
    }
}