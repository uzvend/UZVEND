package com.example.uzvend.Models;

import com.example.uzvend.Constants.Constants;

public class BannerModel {

    String banner_img;
    String id;

    public BannerModel(String banner_img, String id) {
        this.banner_img = Constants.img_root_url+banner_img;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getBanner_img() {
        return banner_img;
    }

    public void setBanner_img(String banner_img) {
        this.banner_img = banner_img;
    }
}
