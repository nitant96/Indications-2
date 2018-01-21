package gov.cipam.gi.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import gov.cipam.gi.activities.BaseActivity;
import gov.cipam.gi.activities.HomePageActivity;
import gov.cipam.gi.activities.IntroActivity;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.Categories;
import gov.cipam.gi.model.GIUniqueness;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.model.Seller;
import gov.cipam.gi.model.States;

/**
 * Created by NITANT SOOD on 23-12-2017.
 */

public class downloadThread implements Runnable {
    Thread thread;
    Context mContext;

    boolean download1,download2,download3;

    public static ArrayList<Categories> mCategoryList=new ArrayList<>();
    public static ArrayList<States> mStateList=new ArrayList<>();
    public static HashMap<String,ArrayList<Product>> stateMapping=new HashMap<>();
    public static HashMap<String,ArrayList<Product>> categoryMapping=new HashMap<>();
    public static HashMap<String,String> mCategoryMap=new HashMap<>();
    public static HashMap<String,String>  mStatesMap=new HashMap<>();

    ArrayList<Product> mainGIList=new ArrayList<>();

    private Firebase mRef1,mRef2,mRef3;

    Database databaseInstance;
    SQLiteDatabase database;

    public downloadThread(Context context) {
        this.mContext=context;
        this.thread = new Thread(this,"downloadThread");
        thread.start();
        databaseInstance = new Database(mContext);
        database = databaseInstance.getWritableDatabase();
    }

    @Override
    public void run() {

        Firebase.setAndroidContext(mContext);
        startDownload();
    }

    void startDownload(){
        mRef1 = new Firebase("https://gi-india.firebaseio.com/Giproducts");
        mRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot oneGIData : dataSnapshot.getChildren()) {
                    Product currentGI;
                    ArrayList<Seller> oneGISellersList=new ArrayList<>();
                    ArrayList<GIUniqueness> oneGIUniquenessList=new ArrayList<>();
                    currentGI = oneGIData.getValue(Product.class);
                    String currentUID=oneGIData.getKey();
                    currentGI.setUid(currentUID);
                    DataSnapshot sellerData=oneGIData.child("seller");
                    DataSnapshot uniquenessData=oneGIData.child("uniqueness");
                    for(DataSnapshot oneSellerData : sellerData.getChildren()){
                        Seller oneSeller=oneSellerData.getValue(Seller.class);
                        oneSeller.setUid(currentUID);
                        oneGISellersList.add(oneSeller);
                    }
                    for(DataSnapshot oneUniquenessData : uniquenessData.getChildren()){
                        GIUniqueness oneUniqueness=oneUniquenessData.getValue(GIUniqueness.class);
                        oneUniqueness.setUid(currentUID);
                        oneGIUniquenessList.add(oneUniqueness);
                    }
                    currentGI.setUniqueness(oneGIUniquenessList);
                    currentGI.setSeller(oneGISellersList);
                    mainGIList.add(currentGI);
                }
                for (int i = 0; i < mainGIList.size(); i++) {
                    String currentState = mainGIList.get(i).getState();
                    String currentCategory = mainGIList.get(i).getCategory();
                    ArrayList<Product> stateList = stateMapping.get(currentState);
                    ArrayList<Product> categoryList = categoryMapping.get(currentCategory);
                    if (stateList == null) {
                        stateList = new ArrayList<>();
                    }
                    if (categoryList == null) {
                        categoryList = new ArrayList<>();
                    }
                    stateList.add(mainGIList.get(i));
                    categoryList.add(mainGIList.get(i));
                    stateMapping.put(currentState, stateList);
                    categoryMapping.put(currentCategory, categoryList);
                }
                for (String oneStateName : stateMapping.keySet()) {
                    States oneState = new States();
                    oneState.setName(oneStateName);
                    ArrayList<Product> k = stateMapping.get(oneStateName);
                    oneState.setStateProductList(k);
                    mStateList.add(oneState);
                }
                for (String oneCategoryName : categoryMapping.keySet()) {
                    Categories oneCategory = new Categories();
                    oneCategory.setName(oneCategoryName);
                    ArrayList<Product> k = categoryMapping.get(oneCategoryName);
                    oneCategory.setCategoryProductList(k);
                    mCategoryList.add(oneCategory);
                }
                Toast.makeText(mContext, "download 1 success", Toast.LENGTH_SHORT).show();
//                HomePage.statesAdapter.notifyDataSetChanged();
//                HomePage.categoryAdapter.notifyDataSetChanged();
                download1 = true;
                shallStartDataLoading();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(mContext, "Download 1 cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        mRef2 = new Firebase("https://gi-india.firebaseio.com/States");
        mRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot oneStateData : dataSnapshot.getChildren()) {
                    States oneState = oneStateData.getValue(States.class);
                    mStatesMap.put(oneState.getName(), oneState.getDpurl());
                }
                Toast.makeText(mContext, "Download 2 Success", Toast.LENGTH_SHORT).show();
//                HomePage.statesAdapter.notifyDataSetChanged();
                download2 = true;
                shallStartDataLoading();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(mContext, "Download 2 Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        mRef3 = new Firebase("https://gi-india.firebaseio.com/Categories");
        mRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot oneCategoryData : dataSnapshot.getChildren()) {
                    Log.i("hi",oneCategoryData.toString());
                    Categories oneCategory = oneCategoryData.getValue(Categories.class);
                    mCategoryMap.put(oneCategory.getName(), oneCategory.getDpurl());
                }
                Toast.makeText(mContext, "Download 3 Success", Toast.LENGTH_SHORT).show();
//                HomePage.categoryAdapter.notifyDataSetChanged();
                download3 = true;
                shallStartDataLoading();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(mContext,firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void shallStartDataLoading(){
        if(download1 && download2 && download3){
            for(int i=0;i<mainGIList.size();i++){
                ContentValues contentValuesGI=new ContentValues();
                ContentValues contentValuesGISearch=new ContentValues();

                contentValuesGI.put(Database.GI_PRODUCT_NAME,mainGIList.get(i).getName());
                contentValuesGI.put(Database.GI_PRODUCT_CATEGORY,mainGIList.get(i).getCategory());
                contentValuesGI.put(Database.GI_PRODUCT_DETAIL,mainGIList.get(i).getDetail());
                contentValuesGI.put(Database.GI_PRODUCT_DP_URL,mainGIList.get(i).getDpurl());
                contentValuesGI.put(Database.GI_PRODUCT_STATE,mainGIList.get(i).getState());
                contentValuesGI.put(Database.GI_PRODUCT_UID,mainGIList.get(i).getUid());
                contentValuesGI.put(Database.GI_PRODUCT_HISTORY,mainGIList.get(i).getHistory());
                contentValuesGI.put(Database.GI_PRODUCT_DESCRIPTION,mainGIList.get(i).getDescription());

                database.insert(Database.GI_PRODUCT_TABLE,null,contentValuesGI);

                contentValuesGISearch.put(Database.GI_SEARCH_NAME,mainGIList.get(i).getName());
                contentValuesGISearch.put(Database.GI_SEARCH_TYPE,Database.GI_PRODUCT);

                database.insert(Database.GI_SEARCH_TABLE,null,contentValuesGISearch);

                ArrayList<Seller> currentSellerList=mainGIList.get(i).getSeller();
                for(int j=0;j<currentSellerList.size();j++){
                    Seller oneSeller=currentSellerList.get(j);

                    ContentValues contentValuesSeller=new ContentValues();
                    ContentValues contentValuesSellerSearch=new ContentValues();

                    contentValuesSeller.put(Database.GI_SELLER_UID,mainGIList.get(i).getUid());
                    contentValuesSeller.put(Database.GI_SELLER_NAME,oneSeller.getName());
                    contentValuesSeller.put(Database.GI_SELLER_ADDRESS,oneSeller.getaddress());
                    contentValuesSeller.put(Database.GI_SELLER_CONTACT,oneSeller.getcontact());
                    contentValuesSeller.put(Database.GI_SELLER_LAT,oneSeller.getlat());
                    contentValuesSeller.put(Database.GI_SELLER_LON,oneSeller.getlon());

                    database.insert(Database.GI_SELLER_TABLE,null,contentValuesSeller);

                    contentValuesSellerSearch.put(Database.GI_SEARCH_NAME,oneSeller.getName());
                    contentValuesSellerSearch.put(Database.GI_SEARCH_TYPE,Database.GI_SELLER);

                    database.insert(Database.GI_SEARCH_TABLE,null,contentValuesSellerSearch);
                }

                ArrayList<GIUniqueness> currentUniquenessList=mainGIList.get(i).getUniqueness();
                for(int k=0;k<currentUniquenessList.size();k++){
                    GIUniqueness oneUniqueness=currentUniquenessList.get(k);

                    ContentValues contentValuesUniqueness=new ContentValues();

                    contentValuesUniqueness.put(Database.GI_UNIQUENESS_UID,oneUniqueness.getUid());
                    contentValuesUniqueness.put(Database.GI_UNIQUENESS_VALUE,oneUniqueness.getInfo());

                    database.insert(Database.GI_UNIQUENESS_TABLE,null,contentValuesUniqueness);
                }
            }
            for(int i=0;i<mStateList.size();i++){
                States currentState=mStateList.get(i);
                String dpurl=mStatesMap.get(currentState.getName());

                ContentValues contentValuesState=new ContentValues();
                ContentValues contentValuesStateSearch=new ContentValues();

                contentValuesState.put(Database.GI_STATE_NAME,currentState.getName());
                contentValuesState.put(Database.GI_STATE_DP_URL,dpurl);

                database.insert(Database.GI_STATE_TABLE,null,contentValuesState);

                contentValuesStateSearch.put(Database.GI_SEARCH_NAME,currentState.getName());
                contentValuesStateSearch.put(Database.GI_SEARCH_TYPE,Database.GI_STATE);

                database.insert(Database.GI_SEARCH_TABLE,null,contentValuesStateSearch);
            }
            for(int i=0;i<mCategoryList.size();i++){
                Categories currentCategory=mCategoryList.get(i);
                String dpurl=mCategoryMap.get(currentCategory.getName());

                ContentValues contentValuesCategory=new ContentValues();
                ContentValues contentValuesCategotySearch=new ContentValues();

                contentValuesCategory.put(Database.GI_CATEGORY_NAME,currentCategory.getName());
                contentValuesCategory.put(Database.GI_CATEGORY_DP_URL,dpurl);

                database.insert(Database.GI_CATEGORY_TABLE,null,contentValuesCategory);

                contentValuesCategotySearch.put(Database.GI_SEARCH_NAME,currentCategory.getName());
                contentValuesCategotySearch.put(Database.GI_SEARCH_TYPE,Database.GI_CATEGORY);

                database.insert(Database.GI_SEARCH_TABLE,null,contentValuesCategotySearch);
            }
        }
    }
}
