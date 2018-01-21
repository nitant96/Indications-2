package gov.cipam.gi.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.fragments.ProductDetailFragment;
import gov.cipam.gi.fragments.ProductListFragment;
import gov.cipam.gi.model.Product;

public class ProductListActivity extends BaseActivity{


    Database databaseInstance;
    SQLiteDatabase database;

    public static ArrayList<Product> subGIList = new ArrayList<>();

    public static String type, value;
    Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpFont();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setUpToolbar(this);

        databaseInstance = new Database(this);
        database = databaseInstance.getReadableDatabase();

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if(type.equals(Database.GI_CATEGORY) || type.equals(Database.GI_STATE)) {
            value = intent.getStringExtra("value");
            fetchGIFromDB();
        }
        else{
            Bundle bundle=intent.getExtras();
            mProduct=(Product) bundle.getSerializable("ppp");
        }




        //swipeRefreshLayout = findViewById(R.id.swipeRefreshProductList);

        if (savedInstanceState == null) {
            if(type.equals(Database.GI_CATEGORY) || type.equals(Database.GI_STATE)) {
                fragmentInflate(ProductListFragment.newInstance());
            }
            else{
                fragmentInflate(ProductDetailFragment.newInstance(mProduct,null));
            }
        }
        //swipeRefreshLayout.setOnRefreshListener(this);
        //swipeRefreshLayout.setColorSchemeResources(R.color.md_red_500, R.color.md_blue_500, R.color.md_green_500);

    }


    private void fetchGIFromDB() {
        subGIList.clear();
        Cursor cursor;
        String[] s={value};
        if(type.equals(Database.GI_STATE)){
            cursor=database.query(Database.GI_PRODUCT_TABLE,null,Database.GI_PRODUCT_STATE+"=?",s,null,null,null);
        }
        else{
            cursor=database.query(Database.GI_PRODUCT_TABLE,null,Database.GI_PRODUCT_CATEGORY+"=?",s,null,null,null);
        }

//        cursor=database.query(Database.GI_PRODUCT_TABLE,null,null,null,null,null,null);

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

    @Override
    protected int getToolbarID() {
        return R.id.product_activity_toolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings_product_list:
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            case android.R.id.home:
                Toast.makeText(this,"Back button clicked", Toast.LENGTH_SHORT).show();
                if(type.equals(Database.GI_PRODUCT)){
                    onBackPressed();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void fragmentInflate(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.product_list_frame_layout, fragment);
        fragmentTransaction.commit();
    }

}
