package com.example.hatemragap.faculty_science_3;

/**
 * Created by hatem on 8/22/2018.
 */

public class LectureModel {
    private String url;
    private String name;
    private String size;

    public LectureModel() {
    }

    public LectureModel(String url, String name, String size) {
        this.url = url;
        this.name = name;
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


}
