package gov.cipam.gi.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import gov.cipam.gi.R;

/**
 * Created by karan on 12/27/2017.
 */

public class CommonUtils {

    private static final int PASSWORD_CHAR_LIMIT = 8;

    public static boolean isEmailValid(String email, Activity activity) {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(activity, "Email field cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            if (!email.matches("[@.com]")) {
                Toast.makeText(activity, "Email not valid", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    public static boolean isPasswordValid(String password, Activity activity) {

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(activity, "Password field cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            if (password.length() < PASSWORD_CHAR_LIMIT) {
                Toast.makeText(activity, "Password should contain atleast 8 characters", Toast.LENGTH_SHORT).show();
            }
            if (!password.matches("[!@#$%^&*()_+=?></]")) {
                Toast.makeText(activity, "Password should contain at least one special character", Toast.LENGTH_SHORT).show();
            }
            if (!password.matches("[a-zA-Z]")) {
                Toast.makeText(activity, "Password should contain at least one capital letter", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    public static void loadImage(final ImageView imageView, Bitmap mBitmap, Activity activity) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean image_download = sharedPreferences.getBoolean(Constants.KEY_DOWNLOAD_IMAGES, true);

        if (image_download) {
            imageView.setImageBitmap(mBitmap);
        } else {
            imageView.setImageResource(R.drawable.image_off);
        }
    }

        /*Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "((!)(@)(#)($)(%)(^)(&))";
        pattern = Pattern.compile(password);
        matcher = pattern.matcher(password);

        return matcher.matches();*/
}
