package com.example.rakshak;

public class Imageupload  {
    String name;
    String url;
    public Imageupload() {
        //empty constructor  needed
    }
    public Imageupload(String name,String url) {
        this.url=url;
        this.name=name;

    }

    public void setName(String name) {
        this.name=name;
    }
    public void setUrl(String url) {
        this.url=url;
    }

    public String getName() {

        return this.name;
    }
    public String getUrl() {
        return this.url;
    }







}
