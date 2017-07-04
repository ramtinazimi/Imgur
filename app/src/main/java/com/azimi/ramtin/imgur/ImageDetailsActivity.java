package com.azimi.ramtin.imgur;

import android.content.Intent;
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
import android.widget.Toast;

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


        final String sharedText = photos.get(position).getTitle() + " " +
                "https://imgur.com/gallery/"+photos.get(position).getId();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharedText);
                //startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));

                //Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                //whatsappIntent.setType("text/plain");
                //whatsappIntent.setPackage("com.whatsapp");
                //whatsappIntent.putExtra(Intent.EXTRA_TEXT, sharedText);
                try {
                    //startActivity(whatsappIntent);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ImageDetailsActivity.this, "Whatsapp has not been installed!",
                            Toast.LENGTH_LONG).show();
                };
            }
        });

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
