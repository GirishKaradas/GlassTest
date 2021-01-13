package com.example.android.glass.glasstest;

public class DataLyo {

    private int id;
    private String step;
    private String type;
    private String title;
    private String url;
    private String desc;

    public DataLyo() {
    }

    public DataLyo(int id, String step, String type, String title, String url, String desc) {
        this.id = id;
        this.step = step;
        this.type = type;
        this.title = title;
        this.url = url;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
