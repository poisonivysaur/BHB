package com.samsung.inifile.bhb;

import android.graphics.Bitmap;

public class Post {

    private String id, photourl, caption;
    private Bitmap imaage;

    public Post() {}

    public Post(String caption){
        this.caption = caption;
    }

    public Post(String caption, Bitmap bitmap){
        this.caption = caption;
        this.imaage = bitmap;
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
        return imaage;
    }

    public void setImaage(Bitmap imaage) {
        this.imaage = imaage;
    }
}
