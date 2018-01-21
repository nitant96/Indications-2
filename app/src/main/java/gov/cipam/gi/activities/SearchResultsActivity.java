package gov.cipam.gi.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gov.cipam.gi.R;
import gov.cipam.gi.adapters.searchListAdapter;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.Categories;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.model.Seller;
import gov.cipam.gi.model.States;

public class SearchResultsActivity extends BaseActivity {


    String mQuery;
    String mType;

    Database databaseInstance;
    SQLiteDatabase database;

    ExpandableListView searchResultListView;
    searchListAdapter searchResultAdapter;

    ArrayList<String> searchListHeaders=new ArrayList<>();

    Map<String,ArrayList> parentChildListMapping=new HashMap<>();

    ArrayList<Categories> categoryList=new ArrayList<>();
    ArrayList<States>stateList=new ArrayList<>();
    ArrayList<Product>productList=new ArrayList<>();
    ArrayList<Seller>sellerList=new ArrayList<>();

    ConstraintLayout constraintLayout;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_search_results);
            setUpToolbar(this);


            Intent intent=getIntent();
            Bundle bundle=intent.getExtras();
            mQuery=bundle.getString("query");
            mType=bundle.getString("type");

            constraintLayout=findViewById(R.id.search_not_found);
            constraintLayout.setVisibility(View.INVISIBLE);

            databaseInstance = new Database(this);
            database = databaseInstance.getReadableDatabase();
            fetchDataFromAllDB();
            if(searchListHeaders.size()==0){
                constraintLayout.setVisibility(View.VISIBLE);
            }
            searchResultAdapter=new searchListAdapter(this,searchListHeaders,parentChildListMapping);
            searchResultListView= findViewById(R.id.searchResultListView);
            searchResultListView.setAdapter(searchResultAdapter);

            for(int i=0;i<searchListHeaders.size();i++){
                searchResultListView.expandGroup(i,true);
            }
            searchResultListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    String clickedGroup=searchListHeaders.get(groupPosition);
                    if(clickedGroup.equals(Database.GI_PRODUCT)){

                        Product product=(Product) parentChildListMapping.get(clickedGroup).get(childPosition);

                        Intent intent=new Intent(SearchResultsActivity.this,ProductListActivity.class);
                        intent.putExtra("type",clickedGroup);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("ppp",product);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else if(clickedGroup.equals(Database.GI_CATEGORY)){
                        Categories categories=(Categories)parentChildListMapping.get(clickedGroup).get(childPosition);

                        Intent intent=new Intent(SearchResultsActivity.this,ProductListActivity.class);
                        intent.putExtra("type",clickedGroup);
                        intent.putExtra("value",categories.getName());
                        startActivity(intent);

                    }
                    else if(clickedGroup.equals(Database.GI_STATE)){

                        States states=(States) parentChildListMapping.get(clickedGroup).get(childPosition);

                        Intent intent=new Intent(SearchResultsActivity.this,ProductListActivity.class);
                        intent.putExtra("type",clickedGroup);
                        intent.putExtra("value",states.getName());
                        startActivity(intent);

                    }
                    else{

                    }
                    return true;
                }
            });

        }


    private void fetchDataFromAllDB() {
        String[] selectionArgs={mQuery+"%"};

        {
            Cursor cursor = database.query(Database.GI_PRODUCT_TABLE, null, Database.GI_PRODUCT_NAME + " LIKE ?", selectionArgs, null, null, null);
            while (cursor.moveToNext()) {
                String name, detail, category, state, dpurl, uid,history,description;

                name = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_NAME));
                detail = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DETAIL));
                category = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_CATEGORY));
                state = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_STATE));
                dpurl = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DP_URL));
                uid = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_UID));

                history=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_HISTORY));
                description=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DESCRIPTION));

                Product oneGI = new Product(name,dpurl,detail,category,state,description,history,uid);

                productList.add(oneGI);
            }
            if (productList.size() > 0) {
                searchListHeaders.add(Database.GI_PRODUCT);
                parentChildListMapping.put(Database.GI_PRODUCT, productList);
            }
        }

        {
            Cursor stateCursor = database.query(Database.GI_STATE_TABLE, null, Database.GI_STATE_NAME + " LIKE ?", selectionArgs, null, null, null, null);
            while (stateCursor.moveToNext()) {
                String name = stateCursor.getString(stateCursor.getColumnIndex(Database.GI_STATE_NAME));
                String dpurl = stateCursor.getString(stateCursor.getColumnIndex(Database.GI_STATE_DP_URL));

                States oneState = new States(name, dpurl);
                stateList.add(oneState);
            }
            if (stateList.size() > 0) {
                searchListHeaders.add(Database.GI_STATE);
                parentChildListMapping.put(Database.GI_STATE, stateList);
            }
        }

        {
            Cursor categoryCursor = database.query(Database.GI_CATEGORY_TABLE, null, Database.GI_CATEGORY_NAME + " LIKE ?", selectionArgs, null, null, null, null);
            while (categoryCursor.moveToNext()) {
                String name = categoryCursor.getString(categoryCursor.getColumnIndex(Database.GI_CATEGORY_NAME));
                String dpurl = categoryCursor.getString(categoryCursor.getColumnIndex(Database.GI_CATEGORY_DP_URL));

                Categories oneCategory = new Categories(name, dpurl);
                categoryList.add(oneCategory);
            }
            if (categoryList.size() > 0) {
                searchListHeaders.add(Database.GI_CATEGORY);
                parentChildListMapping.put(Database.GI_CATEGORY, categoryList);
            }
        }
        {   Cursor sellerCursor=database.query(Database.GI_SELLER_TABLE,null,Database.GI_SELLER_NAME+" LIKE ?",selectionArgs,null,null,null);
            while(sellerCursor.moveToNext()){

                String name,address,contact;
                Double lon,lat;
                name=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_NAME));
                address=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_ADDRESS));
                contact=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_CONTACT));
                lat=sellerCursor.getDouble(sellerCursor.getColumnIndex(Database.GI_SELLER_LAT));
                lon=sellerCursor.getDouble(sellerCursor.getColumnIndex(Database.GI_SELLER_LON));

                Seller oneSeller = new Seller(name, address, contact, lon, lat);
                sellerList.add(oneSeller);
            }
            if(sellerList.size()>0){
                searchListHeaders.add(Database.GI_SELLER);
                parentChildListMapping.put(Database.GI_SELLER,sellerList);
            }
        }

    }

        @Override
        protected int getToolbarID() {
            return R.id.product_search_toolbar;
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()){
                case android.R.id.home:
                    onBackPressed();
                    break;
            }
            return true;
        }
}
