package com.example.android.glass.glasstest;

public class DataManual {

    private double id;
    private String title;
    private String step;
    private String desc;
    private String url;

    public DataManual() {
    }

    public DataManual(double id, String title, String step, String desc, String url) {
        this.id = id;
        this.title = title;
        this.step = step;
        this.desc = desc;
        this.url = url;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
