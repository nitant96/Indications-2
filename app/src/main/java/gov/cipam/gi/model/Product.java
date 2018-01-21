package gov.cipam.gi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Deepak on 11/18/2017.
 */

public class Product implements Serializable {

    private String name,dpurl,detail,category,state,description,history;
    @JsonIgnore
    private String uid;

    @JsonIgnore
    private ArrayList<Seller> seller;

    @JsonIgnore
    private  ArrayList<GIUniqueness> uniqueness;

    public Product(){

    }


    public Product(String name, String dpurl, String detail, String category, String state, String description, String history, String uid) {
        this.name = name;
        this.dpurl = dpurl;
        this.detail = detail;
        this.category = category;
        this.state = state;
        this.description = description;
        this.history = history;
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<Seller> getSeller() {
        return seller;
    }

    public void setSeller(ArrayList<Seller> seller) {
        this.seller = seller;
    }

    public ArrayList<GIUniqueness> getUniqueness() {
        return uniqueness;
    }

    public void setUniqueness(ArrayList<GIUniqueness> uniqueness) {
        this.uniqueness = uniqueness;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
