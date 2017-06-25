package com.azimi.ramtin.imgur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class DisplayAboutPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_about_page);


        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);




        //getActionBar().setDisplayHomeAsUpEnabled(true);

        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0");

        Element devTime = new Element();
        devTime.setTitle("Development time: 6 days");

        Element author = new Element();
        author.setTitle("Author name: rt.azimi@gmail.com");


        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.imgur_comp)
                .setDescription("This is a simple application retrieving images images" +
                        "from the imgur website and diplaying them.")
                .addGroup("App Info")
                .addItem(author)
                .addItem(versionElement)
                .addItem(devTime)
                .addGroup("Connect with us")
                .addEmail("rt.azimi@gmail.com")
                .addGitHub("ramtinazimi")
                .create();

        setContentView(aboutPage);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
