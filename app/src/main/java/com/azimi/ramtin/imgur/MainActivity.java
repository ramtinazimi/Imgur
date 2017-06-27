package com.azimi.ramtin.imgur;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.*;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient httpClient;
    private static String TAG = "Ramtin";
    final List<Photo> photos = new ArrayList<>();
    MyRecyclerViewAdapter adapter;
    private Button button1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Imgur");
        toolbar.setTitleTextColor(Color.WHITE);




        fetchData();


        //Building the request.
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/gallery/user/rising/0.json")
                .header("Authorization","Client-ID b4cf051f07b40a8")
                .header("RamtinAzimi","Imgur App")
                .build();


        //System.out.println("REQUEST!!!!!!!!!!!"+request);

        //Try to connect
        httpClient.newCall(request).enqueue(new Callback() {

            //When connection fails.
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "An error has occurred " + e);

            }


            //If connections was successful.
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject data;
                JSONArray items = null;
                try{
                    data = new JSONObject(response.body().string());
                    items = data.getJSONArray("data");
                    System.out.println("++++++++++++*****************"+items.getString(1));
                }catch(Exception e){
                    System.out.println("Error");
                }


                for(int i=0; i<items.length();i++) {
                    Photo photo = null;
                    try{
                        JSONObject item = items.getJSONObject(i);
                        String title = item.getString("title");
                        if(item.getBoolean("is_album")) {
                            photo = new Photo(item.getString("cover"),title) ;
                        } else {
                            photo = new Photo(item.getString("id"),title) ;
                        }
                    }catch (Exception e){
                        System.out.println("Error");
                    }


                    photos.add(photo); // Add photo to list
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        render(photos);
                    }
                });
            }
        });


    }

    private void render(final List<Photo> photos) {
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv_of_photos);


        int numberOfColumns = 2;
        rv.setLayoutManager(new GridLayoutManager(this,numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, photos);
        //adapter.setClickListener(this);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 16; // Gap of 16px
            }
        });


    }

    private void fetchData() {

        httpClient = new OkHttpClient.Builder().build();

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


        switch (item.getItemId()) {
            case R.id.action_about:
                Intent intent = new Intent(this, AboutPageActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_filter:
                Toast.makeText(this, "Filter clicked!",
                        Toast.LENGTH_LONG).show();

                return true;
            case R.id.action_view:

                Toast.makeText(this, "View clikced!",
                        Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



}
