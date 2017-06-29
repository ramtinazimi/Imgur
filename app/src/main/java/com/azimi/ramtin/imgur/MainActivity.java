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
import android.text.Layout;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

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
    private static String TAG = "Ramtin";
    final List<Photo> photos = new ArrayList<>();
    MyRecyclerViewAdapter adapter;

    private Spinner spinnerGallerySelection;
    int numberOfColumns = 2;
    private RecyclerView.LayoutManager layout;
    private Spinner spinnerGalleryLayout;
    private int layoutCounter = 1;
    RecyclerView rv;


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


    //This is the listener for clicking an image.
    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(this, ImageDetailsActivity.class);
        startActivity(intent);

        /*
        LayoutInflater layoutInflater
                = (LayoutInflater)getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_window, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        //bigImages
        //ImageView bigImages = (ImageView)popupWindow.getContentView().findViewById(R.id.bigImages);
        //Glide.with(this)
            //    .load("https://i.imgur.com/" + photos.get(position).getId() + ".jpg")
        //.into(bigImages);

        //Title
        TextView title = (TextView)popupWindow.getContentView().findViewById(R.id.title);
        title.setText(photos.get(position).getTitle());

        //description
        TextView description = (TextView)popupWindow.getContentView().findViewById(R.id.description);
        description.setText(photos.get(position).getDescription());

        //upvotes
        TextView upvotes = (TextView)popupWindow.getContentView().findViewById(R.id.upvotes);
        upvotes.setText("Upvotes: "+photos.get(position).getUpvotes());


        //downvotes
        TextView downvotes = (TextView)popupWindow.getContentView().findViewById(R.id.downvotes);
        downvotes.setText("Downvotes: "+photos.get(position).getDownvotes());

        //score
        TextView score = (TextView)popupWindow.getContentView().findViewById(R.id.score);
        score.setText("Score: "+photos.get(position).getScore());

        Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
        btnDismiss.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        */
    }

    public void clickAbout(View view){

        Intent intent = new Intent(this, AboutPageActivity.class);
        startActivity(intent);
    }

    public void clickLayoutChanger(View view){


        ImageButton buttonLayoutChanger = (ImageButton)findViewById(R.id.buttonLayoutChanger);
        ImageView photo =  (ImageView) findViewById(R.id.photo);

        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = 180;
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);

        if(layoutCounter == 0){
            buttonLayoutChanger.setImageResource(R.drawable.ic_grid_view);
            photo.setLayoutParams(parms);
            layout = new GridLayoutManager(this, numberOfColumns);
            layoutCounter++;
        }else if(layoutCounter == 1){
            buttonLayoutChanger.setImageResource(R.drawable.ic_grid_staggered_view);
            photo.setLayoutParams(new LinearLayout.LayoutParams(width,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            layout = new StaggeredGridLayoutManager(2,1);
            layoutCounter++;
        }else if(layoutCounter == 2){
            buttonLayoutChanger.setImageResource(R.drawable.ic_list_view);
            photo.setLayoutParams(parms);
            layout = new LinearLayoutManager(this);
            layoutCounter = 0;
        }
        photo.requestLayout();
        rv.setLayoutManager(layout);
    }

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



                retrieveImagesfromPage(spinnerGallerySelection.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

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
                    Log.i(TAG, items.toString());
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

                    Log.i(TAG, photo.toString());
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


        return super.onOptionsItemSelected(item);

    }



}
