package com.samsung.inifile.bhb;

public class Post {

    private String id, photourl;

    public Post() {}

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
}
