package com.example.android.glass.glasstest;

public class DataRecord {

    private String id;
    private String time;
    private String url;

    public DataRecord() {
    }

    public DataRecord(String id, String time, String url) {
        this.id = id;
        this.time = time;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
