package com.azimi.ramtin.imgur;

/**
 * Created by Ramtin on 6/23/2017.
 */

public class Photo {

    private String id;
    private String title;
    private String description;
    private String upvotes;
    private String downvotes;
    private String score;


    public Photo(String id, String title, String description, String upvotes,
                 String downvotes, String score){
        this.id = id;
        setTitle(title);
        setDescription(description);
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.score = score;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description.trim().equals("null")){
            this.description = "";
        }else{
            this.description = description;
        }

    }

    public String getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(String upvotes) {
        this.upvotes = upvotes;
    }

    public String getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(String downvotes) {
        this.downvotes = downvotes;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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
        if(title.trim().equals("null")){
            this.title = "No title";
        }else{
            this.title = title;
        }

    }

    public String toString(){

        return "ID: "+getId()+"\n"+
                "Title: "+getTitle()+"\n"+
                "Description: "+getDescription()+"\n"+
                "Upvotes: "+getUpvotes()+"\n"+
                "Downvotes: "+getDownvotes()+"\n"+
                "Score: "+getScore()+"\n";
    }
}
