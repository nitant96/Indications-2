package gov.cipam.gi.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Deepak on 11/18/2017.
 */

public class Database extends SQLiteOpenHelper {

    private static final String DB_NAME="brainStorm.db";
    private static final int DB_VER = 1;


    public static final String GI_PRODUCT="Products";
    public static final String GI_STATE="States";
    public static final String GI_CATEGORY="Categories";
    public static final String GI_SELLER="Sellers";
    public static final String GI_HISTORY="History";

    public static final String GI_PRODUCT_TABLE="gi_product_table";
    public static final String GI_PRODUCT_DETAIL="gi_product_detail";
    public static final String GI_PRODUCT_NAME="gi_product_name";
    public static final String GI_PRODUCT_STATE="gi_product_state";
    public static final String GI_PRODUCT_CATEGORY="gi_product_category";
    public static final String GI_PRODUCT_DP_URL="gi_product_url";
    public static final String GI_PRODUCT_UID="gi_product_uid";
    public static final String GI_PRODUCT_HISTORY="gi_product_history";
    public static final String GI_PRODUCT_DESCRIPTION="gi_product_description";
    //public static final String GI_PRODUCT_WEB_URL="gi_product_web_url";

    public static final String GI_STATE_TABLE="gi_state_table";
    public static final String GI_STATE_NAME="gi_state_name";
    public static final String GI_STATE_DP_URL="gi_state_dp_url";

    public static final String GI_CATEGORY_TABLE="gi_category_table";
    public static final String GI_CATEGORY_NAME="gi_category_name";
    public static final String GI_CATEGORY_DP_URL="gi_category_dpurl";

    public static final String GI_SELLER_TABLE="gi_seller_table";
    public static final String GI_SELLER_UID="gi_seller_uid";
    public static final String GI_SELLER_NAME="gi_seller_name";
    public static final String GI_SELLER_CONTACT="gi_seller_contact";
    public static final String GI_SELLER_ADDRESS="gi_seller_address";
    public static final String GI_SELLER_LAT="gi_seller_lat";
    public static final String GI_SELLER_LON="gi_seller_lon";

    public static final String GI_SEARCH_TABLE="gi_search_table";
    public static final String GI_SEARCH_NAME="gi_search_name";
    public static final String GI_SEARCH_ID="_id";
    public static final String GI_SEARCH_TYPE="gi_search_type";

    public static final String GI_UNIQUENESS_TABLE="gi_uniqueness_table";
    public static final String GI_UNIQUENESS_UID="gi_uniqueness_uid";
    public static final String GI_UNIQUENESS_VALUE="gi_uniqueness_value";

    public Database(Context context) {
        super(context,DB_NAME,null,DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableGIProducts="Create table "+GI_PRODUCT_TABLE+"( "+
                GI_PRODUCT_UID+" text, "+GI_PRODUCT_NAME+" text, "+
                GI_PRODUCT_HISTORY+" text, "+GI_PRODUCT_DESCRIPTION+" text, "+
                GI_PRODUCT_DETAIL+" text, "+GI_PRODUCT_DP_URL+" text, "+
                GI_PRODUCT_CATEGORY+" text,"+GI_PRODUCT_STATE+" text);";

        String createTableGIState="Create table "+GI_STATE_TABLE+"( "+
                GI_STATE_DP_URL+" text, "+GI_STATE_NAME+" text);";

        String createTableGICategory="Create table "+GI_CATEGORY_TABLE+"( "+
                GI_CATEGORY_NAME+" text,"+GI_CATEGORY_DP_URL+" text);";

        String createTableGISeller="Create table "+GI_SELLER_TABLE+"( "+
                GI_SELLER_UID+" text, "+GI_SELLER_NAME+" text, "+
                GI_SELLER_ADDRESS+" text, "+GI_SELLER_CONTACT+" text,"+
                GI_SELLER_LAT+" real, "+GI_SELLER_LON+" real);";

        String createTableGISearch="Create table "+GI_SEARCH_TABLE+"( "+
                GI_SEARCH_ID+" integer primary key autoincrement, "+GI_SEARCH_NAME+" text, "+
                GI_SEARCH_TYPE+" text);";

        String createTableGIUniqueness="Create table "+GI_UNIQUENESS_TABLE+"( "+
                GI_UNIQUENESS_UID+" text, "+GI_UNIQUENESS_VALUE+" text);";

        db.execSQL(createTableGICategory);
        db.execSQL(createTableGIProducts);
        db.execSQL(createTableGISeller);
        db.execSQL(createTableGIState);
        db.execSQL(createTableGISearch);
        db.execSQL(createTableGIUniqueness);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
