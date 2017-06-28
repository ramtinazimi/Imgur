package com.azimi.ramtin.imgur;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.*;

/*
Code regarding connection to Imgur Website from Ashraff Hathibelagai.
Source: http://progur.com/2016/11/create-imgur-client-android.html
 */
public class MainActivity extends AppCompatActivity {

    private OkHttpClient httpClient;
    private static String TAG = "Ramtin";
    final List<Photo> photos = new ArrayList<>();
    MyRecyclerViewAdapter adapter;

    private Spinner spinnerGallerySelection;
    int numberOfColumns = 2;
    private RecyclerView.LayoutManager layout;
    private Spinner spinnerGalleryLayout;
    private int layoutCounter = 1;
    RecyclerView rv;
    PopupMenu popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Imgur");
        toolbar.setTitleTextColor(Color.WHITE);


        spinnerGallerySelection = (Spinner) findViewById(R.id.spinner_nav);
        spinnerGallerySelection.setPrompt("Gallery");


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        addItemsToSpinner();


    }

    public void clickAbout(View view){

        Intent intent = new Intent(this, AboutPageActivity.class);
        startActivity(intent);
    }

    public void clickLayoutChanger(View view){

        ImageButton buttonLayoutChanger = (ImageButton)findViewById(R.id.buttonLayoutChanger);


        if(layoutCounter == 0){
            buttonLayoutChanger.setImageResource(R.drawable.ic_grid_view);
            layout = new GridLayoutManager(this, numberOfColumns);
            layoutCounter++;
        }else if(layoutCounter == 1){
            buttonLayoutChanger.setImageResource(R.drawable.ic_grid_staggered_view);
            layout = new StaggeredGridLayoutManager(2,1);
            layoutCounter++;
        }else if(layoutCounter == 2){
            buttonLayoutChanger.setImageResource(R.drawable.ic_list_view);
            layout = new LinearLayoutManager(this);
            layoutCounter = 0;
        }

        rv.setLayoutManager(layout);
    }

    public void clickSort(View view){
        //Creating the instance of PopupMenu
        ImageButton button1 = (ImageButton) findViewById(R.id.buttonSort);
        popup = new PopupMenu(MainActivity.this, button1);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(
                        MainActivity.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();
                return true;
            }
        });

        popup.show(); //showing popup menu
    }
    // add items into spinner dynamically
    public void addItemsToSpinner() {


        ArrayList<String> list = new ArrayList<String>();
        list.add("hot");
        list.add("top");
        list.add("user");
        CustomSpinnerAdapter spinAdapter = new CustomSpinnerAdapter(
                getApplicationContext(), list);

        spinnerGallerySelection.setAdapter(spinAdapter);
        spinnerGallerySelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String item = adapter.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(getApplicationContext(), "Selected  : " + item,
                        Toast.LENGTH_LONG).show();

                if(id == 2){
                    popup.getMenu().add("rising");
                    popup.show();
                }else{

                }
                retrieveImagesfromPage(spinnerGallerySelection.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        /*
        ArrayList<String> listLayoutSelection = new ArrayList<String>();
        listLayoutSelection.add("list");
        listLayoutSelection.add("grid");
        listLayoutSelection.add("staggerd grid");

        CustomSpinnerAdapter spinLayoutAdapter = new CustomSpinnerAdapter(
                getApplicationContext(), listLayoutSelection);

        spinnerGalleryLayout.setAdapter(spinLayoutAdapter);
        spinnerGalleryLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String item = adapter.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(getApplicationContext(), "Selected  : " + item,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });

        */

    }

    private void render(final List<Photo> photos) {

        rv = (RecyclerView)findViewById(R.id.rv_of_photos);
        layout = new GridLayoutManager(this, numberOfColumns);
        rv.setLayoutManager(layout);
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

    private void retrieveImagesfromPage(String gallerySelection){
        fetchData();




        //Building the request.
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/gallery/"+gallerySelection)
                .header("Authorization","Client-ID b4cf051f07b40a8")
                .header("RamtinAzimi","Imgur App")
                .build();


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


        /*
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

        */

        return super.onOptionsItemSelected(item);

    }



}
