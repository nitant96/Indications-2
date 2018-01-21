package gov.cipam.gi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by karan on 11/26/2017.
 */

public class Seller implements Serializable {

    private String name,address,contact;
    private double lon,lat;
    @JsonIgnore
    private String uid;

    public Seller(){

    }

    public Seller(String name, String address, String contact, double lon, double lat) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.lon = lon;
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getaddress() {
        return address;
    }

    public void setaddress(String address) {
        this.address = address;
    }

    public String getcontact() {
        return contact;
    }

    public void setcontact(String contact) {
        this.contact = contact;
    }

    public double getlon() {
        return lon;
    }

    public void setlon(double lon) {
        this.lon = lon;
    }

    public double getlat() {
        return lat;
    }

    public void setlat(double lat) {
        this.lat = lat;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
