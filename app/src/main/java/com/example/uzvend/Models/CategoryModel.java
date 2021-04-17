package com.example.uzvend.Models;

import com.example.uzvend.Constants.Constants;

public class CategoryModel {

    public CategoryModel(){
        ///empty constructor///
    }
    private String cat_img;
    private String cat_title;
    private String id;

    public CategoryModel(String id, String cat_img, String cat_title) {
        this.id=id;
        this.cat_img = Constants.img_root_url+cat_img;
        this.cat_title = cat_title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_img() {
        return cat_img;
    }

    public void setCat_img(String cat_img) {
        this.cat_img = cat_img;
    }

    public String getCat_title() {
        return cat_title;
    }

    public void setCat_title(String cat_title) {
        this.cat_title = cat_title;
    }
}


