package com.azimi.ramtin.imgur;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

        //Hiding the toolbar's title.
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

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

        setTextViewText((TextView) findViewById(R.id.description),
                photos.get(position).getDescription());
        setTextViewText((TextView) findViewById(R.id.upvotes),
                photos.get(position).getUpvotes());
        setTextViewText((TextView) findViewById(R.id.downvotes),
                photos.get(position).getDownvotes());
        setTextViewText((TextView) findViewById(R.id.score),
                photos.get(position).getScore());


    }


    public void setTextViewText(TextView txtview, String text){

        txtview.setText(text);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
