package gov.cipam.gi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

/**
 * Created by Deepak on 11/18/2017.
 */

public class Categories {
    private String name,dpurl;
    @JsonIgnore
    ArrayList<Product> categoryProductList=new ArrayList<>();

    public Categories(){

    }

    public Categories(String name, String dpurl) {
        this.name = name;
        this.dpurl = dpurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDpurl() {
        return dpurl;
    }

    public void setDpurl(String dpurl) {
        this.dpurl = dpurl;
    }

    public ArrayList<Product> getCategoryProductList() {
        return categoryProductList;
    }

    public void setCategoryProductList(ArrayList<Product> categoryProductList) {
        this.categoryProductList = categoryProductList;
    }
}
