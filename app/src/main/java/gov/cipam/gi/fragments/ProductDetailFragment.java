package gov.cipam.gi.fragments;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Window;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gov.cipam.gi.activities.MapsActivity;
import gov.cipam.gi.R;
import gov.cipam.gi.activities.WebViewActivity;
import gov.cipam.gi.adapters.SellerListAdapter;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.GIUniqueness;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.model.Seller;
import gov.cipam.gi.utils.ChromeTabActionBroadcastReceiver;
import gov.cipam.gi.utils.CommonUtils;
import gov.cipam.gi.utils.CustomTabActivityHelper;
import gov.cipam.gi.utils.PaletteGenerate;

import static gov.cipam.gi.utils.Constants.EXTRA_URL;

/**
 * Created by karan on 12/14/2017.
 */

public class ProductDetailFragment extends Fragment implements SellerListAdapter.setOnSellerClickListener {
//    Seller seller;
//    SellerFirebaseAdapter sellerFirebaseAdapter;
//    DatabaseReference mDatabaseReference;

    TextView txtState,txtCategory,txtHistory,txtDesc;
    String name,address,contact;
    Double lon,lat;
    RecyclerView sellerRecyclerView;
    ArrayList<Seller> sellerList;
    ArrayList<GIUniqueness> uniquenessesList;
    SellerListAdapter sellerListAdapter;
    Database databaseInstance;
    SQLiteDatabase database;
    ImageView imageView;
    PaletteGenerate paletteGenerate;
    Product product;
    String[] currentProductUID;
    Toolbar toolbar;
    Window window;
    boolean isImagePreLoaded = false;
    public static Bitmap mBitmap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    public ProductDetailFragment() {
    }


    public static ProductDetailFragment newInstance(Product product, Bitmap bitmap) {

        Bundle args = new Bundle();
        args.putSerializable("nalin", product);
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(args);
        mBitmap=bitmap;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sellerList = new ArrayList<>();
        uniquenessesList=new ArrayList<>();
        databaseInstance = new Database(getContext());
        database = databaseInstance.getReadableDatabase();
        paletteGenerate=new PaletteGenerate();
        toolbar=((AppCompatActivity) getActivity()).findViewById(R.id.product_activity_toolbar);
//        if(mBitmap!=null) {
//            paletteGenerate.createPaletteAsync(mBitmap);
//        }
//        paletteGenerate.setToolbarColor(mBitmap,toolbar);
        populateSellerListFromDB();
        populateUniquenessFromDB();

        displayUniqueness();
        setHasOptionsMenu(true);

        sellerListAdapter = new SellerListAdapter(getContext(), sellerList, this);

        super.onCreate(savedInstanceState);
    }




    private void populateSellerListFromDB() {

//        String[] s={ProductDetailActivity.currentProduct.getUid()};

        Bundle b=getArguments();
        product=(Product)b.get("nalin");

        currentProductUID = new String[]{product.getUid()};
        Cursor sellerCursor=database.query(Database.GI_SELLER_TABLE,null,Database.GI_SELLER_UID+"=?",currentProductUID,null,null,null,null);

        while(sellerCursor.moveToNext()){

            name=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_NAME));
            address=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_ADDRESS));
            contact=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_CONTACT));
            lat=sellerCursor.getDouble(sellerCursor.getColumnIndex(Database.GI_SELLER_LAT));
            lon=sellerCursor.getDouble(sellerCursor.getColumnIndex(Database.GI_SELLER_LON));

            Seller oneSeller = new Seller(name, address, contact, lon, lat);

            sellerList.add(oneSeller);
        }
        sellerCursor.close();
    }

    void populateUniquenessFromDB(){
        Cursor uniquenessCursor=database.query(Database.GI_UNIQUENESS_TABLE,null,Database.GI_UNIQUENESS_UID+"=?",currentProductUID,null,null,null,null);

        while(uniquenessCursor.moveToNext()){
            String info=uniquenessCursor.getString(uniquenessCursor.getColumnIndex(Database.GI_UNIQUENESS_VALUE));

            GIUniqueness oneUniqueness=new GIUniqueness(info);

            uniquenessesList.add(oneUniqueness);
        }
        uniquenessCursor.close();
    }

    private void displayUniqueness() {
        for(int t=0;t<uniquenessesList.size();t++){
            GIUniqueness uniqueness=uniquenessesList.get(t);

            Toast.makeText(getContext(),uniqueness.getInfo(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem settingsAction=menu.findItem(R.id.action_settings_product_list);
        settingsAction.setVisible(false);
        MenuItem refreshOption=menu.findItem(R.id.menu_refresh);
        refreshOption.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        switch (id){
            case android.R.id.home:
                getActivity().getSupportFragmentManager()
                        .popBackStackImmediate();
                break;
            case R.id.action_location:
                startActivity(new Intent(getContext(),MapsActivity.class)
                        .putExtra("latitude",lat)
                        .putExtra("longitude",lon)
                        .putExtra("address",address));
                break;
            case R.id.action_url:

                String url="https://google.com";
                Uri uri=Uri.parse(url);
                openCustomChromeTab(uri);
                break;
        }
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sellerRecyclerView= view.findViewById(R.id.seller_recycler_view);
        imageView=getActivity().findViewById(R.id.productDetailImage);
        txtState=view.findViewById(R.id.detail_stateName);
        txtCategory=view.findViewById(R.id.detail_categoryName);
        txtHistory = view.findViewById(R.id.productHistory);
        txtDesc=view.findViewById(R.id.productDesc);

        sellerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        imageView.setVisibility(View.VISIBLE);
//        CommonUtils.loadImage(imageView,mBitmap,getActivity());

        if(mBitmap!=null) {
            imageView.setImageBitmap(mBitmap);
        }
        else{
            Picasso.with(getContext()).load(product.getDpurl()).fit().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap;
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    bitmap = drawable.getBitmap();
                    mBitmap=bitmap;
                    paletteGenerate.setToolbarColor(mBitmap,toolbar);
                }

                @Override
                public void onError() {

                }
            });
        }
        sellerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        sellerRecyclerView.setAdapter(sellerListAdapter);
        setData();
        Toast.makeText(getContext(),"HISTORY : "+product.getHistory(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(),"DESCRIPTION : "+product.getDescription(), Toast.LENGTH_SHORT).show();
    }

    private void openCustomChromeTab(Uri uri) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        // set toolbar colors
        intentBuilder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        intentBuilder.addMenuItem(getString(R.string.title_menu_1), createPendingIntent(ChromeTabActionBroadcastReceiver.ACTION_MENU_ITEM_1));
        intentBuilder.addMenuItem(getString(R.string.title_menu_2), createPendingIntent(ChromeTabActionBroadcastReceiver.ACTION_MENU_ITEM_2));

        // set action button
        intentBuilder.setActionButton(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "Action Button", createPendingIntent(ChromeTabActionBroadcastReceiver.ACTION_ACTION_BUTTON));

        // build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

        // call helper to open custom tab
        CustomTabActivityHelper.openCustomTab(getActivity(), customTabsIntent, uri, new CustomTabActivityHelper.CustomTabFallback() {
            @Override
            public void openUri(Activity activity, Uri uri) {
                // fall back, call open open webview
                openWebView(uri);
            }
        });
    }

    private void openWebView(Uri uri) {
        Intent webViewIntent = new Intent(getContext(), WebViewActivity.class);
        webViewIntent.putExtra(EXTRA_URL, uri.toString());
        startActivity(webViewIntent);
    }

    private PendingIntent createPendingIntent(int actionSource) {
        Intent actionIntent = new Intent(getContext(), ChromeTabActionBroadcastReceiver.class);
        actionIntent.putExtra(ChromeTabActionBroadcastReceiver.KEY_ACTION_SOURCE, actionSource);
        return PendingIntent.getBroadcast(getContext(), actionSource, actionIntent, 0);
    }

    private void setData(){
        txtDesc.setText(getResources().getString(R.string.long_text));
        txtHistory.setText(product.getDetail());
        txtCategory.setText(product.getCategory());
        txtState.setText(product.getState());

    }
    @Override
    public void onSellerClicked(View v, int Position) {

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        setRetainInstance(true);
    }

    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        window.setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
    }*/
}
