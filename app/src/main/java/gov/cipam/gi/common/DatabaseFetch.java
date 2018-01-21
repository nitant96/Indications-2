package gov.cipam.gi.common;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.content.Context;

import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.Categories;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.model.States;

import static gov.cipam.gi.activities.ProductListActivity.subGIList;
import static gov.cipam.gi.activities.ProductListActivity.value;
import static gov.cipam.gi.fragments.HomePageFragment.mDisplayCategoryList;
import static gov.cipam.gi.fragments.HomePageFragment.mDisplayStateList;

/**
 * Created by karan on 1/10/2018.
 */

public class DatabaseFetch {

    private Database databaseInstance;
    private SQLiteDatabase database;

        public void populateDisplayListFromDB(Context context) {

            mDisplayStateList.clear();
            mDisplayCategoryList.clear();

            databaseInstance = new Database(context);
            database=databaseInstance.getReadableDatabase();

            Cursor categoryCursor=database.query(Database.GI_CATEGORY_TABLE,null,null,null,null,null,null,null);
            while(categoryCursor.moveToNext()){
                String name=categoryCursor.getString(categoryCursor.getColumnIndex(Database.GI_CATEGORY_NAME));
                String dpurl=categoryCursor.getString(categoryCursor.getColumnIndex(Database.GI_CATEGORY_DP_URL));

                Categories oneCategory=new Categories(name,dpurl);
                mDisplayCategoryList.add(oneCategory);
            }

            Cursor stateCursor=database.query(Database.GI_STATE_TABLE,null,null,null,null,null,null,null);
            while(stateCursor.moveToNext()){
                String name=stateCursor.getString(stateCursor.getColumnIndex(Database.GI_STATE_NAME));
                String dpurl=stateCursor.getString(stateCursor.getColumnIndex(Database.GI_STATE_DP_URL));

                States oneState=new States(name,dpurl);
                mDisplayStateList.add(oneState);
            }
        }

    private void fetchGIFromDB() {
        subGIList.clear();
        Cursor cursor;
        String[] s={value};
//        if(type.equals("state")){
//            cursor=database.query(Database.GI_PRODUCT_TABLE,null,Database.GI_PRODUCT_STATE+"=?",s,null,null,null);
//        }
//        else{
//            cursor=database.query(Database.GI_PRODUCT_TABLE,null,Database.GI_PRODUCT_CATEGORY+"=?",s,null,null,null);
//        }

        cursor=database.query(Database.GI_PRODUCT_TABLE,null,null,null,null,null,null);

        while (cursor.moveToNext()){
            String name,detail,category,state,dpurl,uid,history,description;

            name=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_NAME));
            detail=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DETAIL));
            category=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_CATEGORY));
            state=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_STATE));
            dpurl=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DP_URL));
            uid=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_UID));

            history=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_HISTORY));
            description=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DESCRIPTION));

            Product oneGI = new Product(name,dpurl,detail,category,state,description,history,uid);

            subGIList.add(oneGI);
        }
        cursor.close();
    }
}

