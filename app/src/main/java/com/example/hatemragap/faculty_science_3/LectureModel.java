package com.example.hatemragap.faculty_science_3;

/**
 * Created by hatem on 8/22/2018.
 */

public class LectureModel {
    private String downloadLink;
    private String lecturename;
    private String size;
    private String date;
    private String uploder_name;



    public LectureModel() {
    }

    public LectureModel(String downloadLink, String lecturename, String size, String date, String uploder_name) {
        this.downloadLink = downloadLink;
        this.lecturename = lecturename;
        this.size = size;
        this.date = date;
        this.uploder_name = uploder_name;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getLecturename() {
        return lecturename;
    }

    public void setLecturename(String lecturename) {
        this.lecturename = lecturename;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUploder_name() {
        return uploder_name;
    }

    public void setUploder_name(String uploder_name) {
        this.uploder_name = uploder_name;
    }


}
