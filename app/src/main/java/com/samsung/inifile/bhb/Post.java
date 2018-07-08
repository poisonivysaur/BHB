package com.samsung.inifile.bhb;

import android.graphics.Bitmap;

public class Post {

    private String id, photourl, caption, name, rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Bitmap getProfpic() {
        return profpic;
    }

    public void setProfpic(Bitmap profpic) {
        this.profpic = profpic;
    }

    private Bitmap image, profpic;

    public Post() {}

    public Post(String caption){
        this.caption = caption;
    }

    public Post(String caption, Bitmap bitmap){
        this.caption = caption;
        this.image = bitmap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public Post(String id, String photourl){
        this.id = id;
        this.photourl = photourl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Bitmap getImaage() {
        return image;
    }

    public void setImaage(Bitmap imaage) {
        this.image = imaage;
    }
}
