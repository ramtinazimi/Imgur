package com.azimi.ramtin.imgur;

/**
 * Created by Ramtin on 6/23/2017.
 */

public class Photo {

    private String id;
    private String title;

    public Photo(String id, String title){
        this.id = id;
        this.title = title;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
