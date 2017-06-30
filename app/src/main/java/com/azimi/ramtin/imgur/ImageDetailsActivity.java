package com.azimi.ramtin.imgur;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import static com.azimi.ramtin.imgur.MainActivity.photos;

public class ImageDetailsActivity extends AppCompatActivity {

    private static String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        int position = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("photo_position");
        }

        //Title
        TextView title = (TextView)findViewById(R.id.title);
        title.setText(photos.get(position).getTitle());

        ImageView bigimages = (ImageView) findViewById(R.id.bigImages);
        //Image
        Glide.with(this)
                .load("https://i.imgur.com/" + photos.get(position).getId() + ".jpg")
                .into(bigimages);

        //description
        TextView description = (TextView)findViewById(R.id.description);
        description.setText(photos.get(position).getDescription());

        //upvotes
        TextView upvotes = (TextView)findViewById(R.id.upvotes);
        upvotes.setText(photos.get(position).getUpvotes());


        //downvotes
        TextView downvotes = (TextView)findViewById(R.id.downvotes);
        downvotes.setText(photos.get(position).getDownvotes());

        //score
        TextView score = (TextView)findViewById(R.id.score);
        score.setText(photos.get(position).getScore()+ " Scores");



    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
