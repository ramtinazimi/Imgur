package com.azimi.ramtin.imgur;


import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;


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
public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private OkHttpClient httpClient;
    private static String TAG = "DEBUG";
    final static List<Photo> photos = new ArrayList<>();
    MyRecyclerViewAdapter adapter;

    private Spinner spinnerGallerySelection;
    int numberOfColumns = 2;
    private RecyclerView.LayoutManager layout;
    private int layoutCounter = 1;
    RecyclerView rv;

    private static final String galleryHot = "https://api.imgur.com/3/gallery/hot/rising/0.json";
    private static final String galleryUser = "https://api.imgur.com/3/gallery/user/rising/0.json";
    private static final String galleryTop = "https://api.imgur.com/3/gallery/top/rising/0.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setting up the layout and the toolbar.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //Hiding the toolbar's title.
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //Setting the spinner on the toolbar.
        spinnerGallerySelection = (Spinner) findViewById(R.id.spinner_nav);
        spinnerGallerySelection.setPrompt("Gallery");

        retrieveImagesfromPage(galleryHot);

        addItemsToSpinner();

    }



    //*******************************************************************************
    //********************************CLICK LISTENERS********************************
    //**********************************START****************************************

    //Click listener for an image.
    @Override
    public void onImageClick(View view, int position) {

        Intent intent = new Intent(this, ImageDetailsActivity.class);
        intent.putExtra("photo_position", position);
        startActivity(intent);

    }

    //Click listener for the "about"-button (symbolized by an i at the far right corner.)
    public void clickAbout(View view){

        Intent intent = new Intent(this, AboutPageActivity.class);
        startActivity(intent);
    }

    //Click listener for changing the layout view.
    //Options for layout view: 1. GridView 2. Staggered Grid View 3. ListView
    public void clickLayoutChanger(View view){


        ImageButton buttonLayoutChanger = (ImageButton)findViewById(R.id.buttonLayoutChanger);
        ImageView photo =  (ImageView) findViewById(R.id.photo);



        if(layoutCounter == 0){
            //GRID LAYOUT
            buttonLayoutChanger.setImageResource(R.drawable.ic_grid_view);
            //retrieveImagesfromPage(galleryHot);
            //photo.forceLayout();
            //photo.setLayoutParams(new LinearLayout.LayoutParams(
             //       RelativeLayout.LayoutParams.MATCH_PARENT,180));

            layout = new GridLayoutManager(this, numberOfColumns);
            layoutCounter++;
        }else if(layoutCounter == 1){
            //STAGGERED GRID LAYOUT
            buttonLayoutChanger.setImageResource(R.drawable.ic_grid_staggered_view);
            //retrieveImagesfromPage(galleryHot);
            //photo.forceLayout();
            //photo.setLayoutParams(new LinearLayout.LayoutParams(
            //        RelativeLayout.LayoutParams.MATCH_PARENT,
            //        RelativeLayout.LayoutParams.WRAP_CONTENT));

            layout = new StaggeredGridLayoutManager(2,1);
            layoutCounter++;
        }else if(layoutCounter == 2){
            //LIST LAYOUT
            //buttonLayoutChanger.setImageResource(R.drawable.ic_list_view);
            //retrieveImagesfromPage(galleryHot);
            //photo.forceLayout();
            //photo.setLayoutParams(new LinearLayout.LayoutParams(
            //        RelativeLayout.LayoutParams.MATCH_PARENT,180));

            layout = new LinearLayoutManager(this);
            layoutCounter = 0;
        }

        photo.requestLayout();

        rv.setLayoutManager(layout);

    }

    //Click listener for the sort button.
    public void clickSort(View view){
        //Creating the instance of PopupMenu
        ImageButton buttonSort = (ImageButton) findViewById(R.id.buttonSort);


        PopupMenu popupSort = new PopupMenu(MainActivity.this, buttonSort);
        //Inflating the Popup using xml file
        popupSort.getMenuInflater()
                .inflate(R.menu.popup_menu_sort, popupSort.getMenu());

        //registering popup with OnMenuItemClickListener
        popupSort.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(
                        MainActivity.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();
                return true;
            }
        });

        popupSort.show(); //showing popup menu

    }

    //Click Listener for toggle button.
    public void clickViral(View view){

        ToggleButton viralCheckBox = (ToggleButton) findViewById(R.id.viralToggleButton);

        if(viralCheckBox.isChecked()){
            Toast.makeText(this, "Include viral images.",
                    Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(
                    MainActivity.this,
                    "Exclude viral images.",
                    Toast.LENGTH_LONG);
        }

    }

    //Click Listener for window button.
    public void clickDate(View view){
        //Creating the instance of PopupMenu
        ImageButton buttonDate = (ImageButton) findViewById(R.id.buttonDate);
        PopupMenu popupDate = new PopupMenu(MainActivity.this, buttonDate);
        //Inflating the Popup using xml file
        popupDate.getMenuInflater()
                .inflate(R.menu.popup_menu_date_range, popupDate.getMenu());

        //registering popup with OnMenuItemClickListener
        popupDate.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(
                        MainActivity.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();
                return true;
            }
        });

        popupDate.show(); //showing popup menu
    }

    //add items into spinner
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


                //Log.i(TAG, spinnerGallerySelection.getSelectedItem().toString());
                if(position == 0){
                    retrieveImagesfromPage(galleryTop);
                }else if(position == 1){
                    retrieveImagesfromPage(galleryUser);
                }else if(position == 2){
                    retrieveImagesfromPage(galleryTop);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    //*******************************************************************************
    //********************************CLICK LISTENERS********************************
    //**********************************END******************************************

    private void render(final List<Photo> photos) {

        rv = (RecyclerView)findViewById(R.id.rv_of_photos);
        layout = new GridLayoutManager(this, numberOfColumns);
        rv.setLayoutManager(layout);
        adapter = new MyRecyclerViewAdapter(this, photos);
        adapter.setClickListener(this);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 16; // Gap of 16px
            }
        });

    }

    private void retrieveImagesfromPage(String gallerySelection){

        String s = "https://api.imgur.com/3/gallery/"+gallerySelection+"/rising/0.json";
        Log.i(TAG, s);

        httpClient = new OkHttpClient.Builder().build();
        //Building the request.
        Request request = new Request.Builder()
                .url(gallerySelection)
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
                    //Log.i(TAG, items.toString());
                }catch(Exception e){
                    System.out.println("Error");
                }


                for(int i=0; i<items.length();i++) {
                    Photo photo = null;
                    try{
                        JSONObject item = items.getJSONObject(i);

                        String title = item.getString("title");
                        String description = item.getString("description");
                        String upvotes = item.getString("ups");
                        String downvotes = item.getString("downs");
                        String score = item.getString("score");


                        if(item.getBoolean("is_album")) {
                            photo = new Photo(item.getString("cover"),title, description, upvotes, downvotes, score) ;
                        } else {
                            photo = new Photo(item.getString("id"),title, description, upvotes, downvotes, score);
                        }
                    }catch (Exception e){
                        System.out.println("Error");
                    }


                    //Log.i(TAG, photo.toString());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }







}
