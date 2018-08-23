package com.example.hatemragap.faculty_science_3;

/**
 * Created by hatem on 8/23/2018.
 */

public class newAlgMoudle {

    private String downloadLink;
    private String name;
    private String size;

    public newAlgMoudle() {
    }

    public newAlgMoudle(String downloadLink, String name, String size) {
        this.downloadLink = downloadLink;
        this.name = name;
        this.size = size;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
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
