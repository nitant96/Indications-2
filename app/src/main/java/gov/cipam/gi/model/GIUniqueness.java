package gov.cipam.gi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by NITANT SOOD on 19-01-2018.
 */

public class GIUniqueness implements Serializable {

    private String info;
    @JsonIgnore
    private String uid;

    public GIUniqueness(String info) {
        this.info = info;
    }

    public GIUniqueness() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
