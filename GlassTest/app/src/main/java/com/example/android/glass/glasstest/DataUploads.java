package com.example.android.glass.glasstest;

public class DataUploads {

    private String id;
    private String url;

    public DataUploads() {
    }

    public DataUploads(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
