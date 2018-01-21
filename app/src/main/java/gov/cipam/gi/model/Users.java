package gov.cipam.gi.model;

import android.net.Uri;

/**
 * Created by Deepak on 11/18/2017.
 */

public class Users {

    private String name;
    private String email;
    private Uri uri;

    public Users(){

    }

    public void Users(String name,String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Uri getUri() {
        return uri;
    }

    public Uri setUri(Uri uri) {
        this.uri = uri;
        return uri;
    }
}
